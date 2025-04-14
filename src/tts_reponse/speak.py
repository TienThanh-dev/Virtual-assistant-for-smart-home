import pyttsx3
from gtts import gTTS
import pygame
from ..utils import check_internet,audio_event

engine = pyttsx3.init()
engine.setProperty('rate', 150)

def speak_pyttsx3(text):
    engine.say(text)
    engine.runAndWait()

def speak_google_tts(text):
    pygame.mixer.init()
    tts = gTTS(text=text, lang='en')  
    if pygame.mixer.music.get_busy():
        pygame.mixer.music.stop()
    tts.save("./sound_notification/voice.mp3")
    pygame.mixer.music.load("./sound_notification/voice.mp3")
    pygame.mixer.music.play()
    while pygame.mixer.music.get_busy(): 
        continue
    # Đảm bảo rằng không có gì còn hoạt động
    pygame.mixer.music.stop()
    pygame.mixer.quit()

def speak(text):
    audio_event.clear()
    if check_internet():
        speak_google_tts(text)
    else:
        speak_pyttsx3(text)
    audio_event.set()
