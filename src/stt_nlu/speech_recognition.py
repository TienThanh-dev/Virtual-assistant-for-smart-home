import vosk
import json
from ..config import MODEL_PATH_VN, MODEL_PATH_EN, SAMPLERATE
from ..utils import record_audio

def load_models():
    """Load mô hình Vosk"""
    model_vn = vosk.Model(MODEL_PATH_VN)
    model_en = vosk.Model(MODEL_PATH_EN)
    recognizer_vn = vosk.KaldiRecognizer(model_vn, SAMPLERATE)
    recognizer_en = vosk.KaldiRecognizer(model_en, SAMPLERATE)
    return recognizer_vn, recognizer_en
def recognize_speech():
    """Nhận diện giọng nói và phân biệt ngôn ngữ"""
    if not hasattr(recognize_speech, "recognizer_en"):
        recognize_speech.recognizer_vn, recognize_speech.recognizer_en = load_models()
    recognizer_en = recognize_speech.recognizer_en
    for data in record_audio(SAMPLERATE):
        if recognizer_en.AcceptWaveform(data):
            result = json.loads(recognizer_en.Result()).get("text", "")
            if result:
                print("📢 You said:", result)
                yield result
        # elif recognizer_vn.AcceptWaveform(data):
        #     result = json.loads(recognizer_vn.Result()).get("text", "")
        #     if result:
        #         print("📢 Bạn nói:", result)
