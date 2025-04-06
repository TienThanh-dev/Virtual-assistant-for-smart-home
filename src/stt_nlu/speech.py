import vosk
import json
import time
import threading
from ..config import MODEL_PATH_VN, MODEL_PATH_EN, SAMPLERATE
from ..utils import record_audio,check_internet
import speech_recognition as sr

def load_models():
    """Load mô hình Vosk"""
    model_en = vosk.Model(MODEL_PATH_EN)
    # model_vn = vosk.Model(MODEL_PATH_VN)
    # recognizer_vn = vosk.KaldiRecognizer(model_vn, SAMPLERATE)
    recognizer_en = vosk.KaldiRecognizer(model_en, SAMPLERATE)
    return recognizer_en

use_google = check_internet()  # trạng thái hiện tại
lock = threading.Lock()        # khóa đồng bộ dữ liệu giữa thread

def network_monitor():
    global use_google
    while True:
        new_status = check_internet()
        with lock:
            if new_status != use_google:
                use_google = new_status
                print("Chuyển sang:", "Google" if use_google else "Vosk")
        time.sleep(5)
# Bắt đầu thread kiểm tra mạng
threading.Thread(target=network_monitor, daemon=True).start()
def recognize_speech_loop():
    """Nhận diện giọng nói liên tục bằng Google hoặc Vosk tùy theo kết nối mạng"""
    recognizer = sr.Recognizer()
    mic = sr.Microphone(sample_rate=SAMPLERATE)
    if not hasattr(recognize_speech_loop, "recognizer_en"):
        recognize_speech_loop.recognizer_en = load_models()
    vosk_recognizer = recognize_speech_loop.recognizer_en
    
    while True:
        try:
            with lock:
                global use_google
                google_enabled = use_google
            if google_enabled:
                with mic as source:
                    recognizer.adjust_for_ambient_noise(source, duration=0.5)
                    print("[Google]")
                    try:
                        audio = recognizer.listen(source, timeout=5, phrase_time_limit=5)
                        result = recognizer.recognize_google(audio, language="en-US")
                        print("You said (Google):", result)
                        yield result
                    except sr.UnknownValueError:
                        print("Không thể nhận diện âm thanh.")
                    except sr.RequestError as e:
                        print(f"Google lỗi: {e}")
                        with lock:
                            use_google = False  # chuyển sang Vosk
            else:
                print("🎙️ [Vosk] Đang ghi âm...")
                for data in record_audio(SAMPLERATE):
                    if vosk_recognizer.AcceptWaveform(data):
                        result = json.loads(vosk_recognizer.Result()).get("text", "")
                        if result:
                            print("You said (Vosk):", result)
                            yield result
                            break
        except KeyboardInterrupt:
            print("Dừng nhận diện.")
            break
        except Exception as e:
            print(f"Lỗi không xác định: {e}")
