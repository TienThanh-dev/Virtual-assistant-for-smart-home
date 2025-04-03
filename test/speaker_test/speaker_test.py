import pyttsx3
import time

print("Hello")

text = "Hello, how are you? I want to go to the market"

# Đo thời gian khởi tạo engine
start_time_init = time.time()
engine = pyttsx3.init()
end_time_init = time.time()

# Đo thời gian thiết lập tốc độ nói
start_time_set = time.time()
engine.setProperty('rate', 150)  # Tốc độ nói
end_time_set = time.time()

# Đo thời gian xử lý `say`
start_time_say = time.time()
engine.say(text)
end_time_say = time.time()

# Đo thời gian chạy `runAndWait`
start_time_wait = time.time()
engine.runAndWait()
end_time_wait = time.time()

# In thời gian thực thi của từng bước
print(f"Thời gian khởi tạo engine: {end_time_init - start_time_init:.6f} giây")
print(f"Thời gian thiết lập tốc độ nói: {end_time_set - start_time_set:.6f} giây")
print(f"Thời gian xử lý `say`: {end_time_say - start_time_say:.6f} giây")
print(f"Thời gian chạy `runAndWait`: {end_time_wait - start_time_wait:.6f} giây")

# process_queue = multiprocessing.Queue()
# process = multiprocessing.Process(target=tts, args=(process_queue,))
# process.start()


