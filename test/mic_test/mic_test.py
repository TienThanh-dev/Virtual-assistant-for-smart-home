import queue
import sounddevice as sd
import wave

# Hàng đợi để lưu dữ liệu âm thanh
q = queue.Queue()

def audio_callback(indata, frames, time, status):
    """Hàm callback để lấy dữ liệu từ micro"""
    if status:
        print(status, flush=True)
    q.put(bytes(indata))

def record_audio(filename="./test/output.wav", samplerate=16000, blocksize=8000, duration=5):
    """Ghi âm và lưu vào file WAV"""
    with sd.RawInputStream(samplerate=samplerate, blocksize=blocksize, dtype="int16",
                           channels=1, callback=audio_callback):
        print("Bắt đầu ghi âm...")
        
        # Mở file WAV để ghi
        with wave.open(filename, 'wb') as wf:
            wf.setnchannels(1)  # Số kênh (mono)
            wf.setsampwidth(2)  # Độ rộng mẫu (16-bit = 2 byte)
            wf.setframerate(samplerate)  # Tần số lấy mẫu
            
            for _ in range(int(samplerate / blocksize * duration)):
                wf.writeframes(q.get())

        print(f"Ghi âm hoàn tất, đã lưu vào {filename}")

# Ghi âm trong 5 giây và lưu ra file output.wav
record_audio("output.wav", duration=5)
