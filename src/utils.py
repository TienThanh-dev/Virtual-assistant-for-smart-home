import queue
import sounddevice as sd
import threading
from .config import SAMPLERATE

# Hàng đợi để lưu dữ liệu âm thanh
q = queue.Queue()
audio_event=threading.Event()
audio_event.set()
def audio_callback(indata,frames, time, status):
    """Hàm callback để lấy dữ liệu từ micro"""
    if status:
        print(status, flush=True)
        
    if audio_event.is_set():
        q.put(bytes(indata))  # Đưa dữ liệu vào hàng đợi nếu đang ghi âm
    else:
        # Khi không ghi âm, làm rỗng hàng đợi
        while not q.empty():
            q.get()
    

def record_audio(samplerate=SAMPLERATE, blocksize=8000):
    """Hàm mở micro và lấy dữ liệu âm thanh"""
    with sd.RawInputStream(samplerate=samplerate, blocksize=blocksize, dtype="int16",
                           channels=1, callback=audio_callback):
        print("Nói vào mic...")
        while True:
            if audio_event.is_set():
                yield q.get()  # Chỉ lấy dữ liệu khi đang ghi âm
            
