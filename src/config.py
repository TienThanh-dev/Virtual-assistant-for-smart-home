import os

# Đường dẫn gốc của dự án (tính từ `config.py`)
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

MODEL_PATH_VN = os.path.join(BASE_DIR, "models", "vosk-model-vn")
MODEL_PATH_EN = os.path.join(BASE_DIR, "models", "vosk-model-en")
MODEL_PATH_TFIDF_SVM = os.path.join(BASE_DIR, "models", "intent_classifier.pkl")
MODEL_PATH_SPA = os.path.join(BASE_DIR, "models", "spaCy_output", "model-best")
RESPONSE_PARAGRAPHS = os.path.join(BASE_DIR,"dataset","response_data", "response_paragraphs.json")
SOUND_NOTIFICATION=os.path.join(BASE_DIR,"sound_notification")
SAMPLERATE = 16000
#PIN GPIO FOR DEVICE CONTROL
PIN_DEVICE_1 = 17
PIN_DEVICE_2 = 27
PIN_DEVICE_3 = 22
PIN_DEVICE_4 = 23
PIN_DEVICE_5 = 24
PIN_DEVICE_6 = 25
