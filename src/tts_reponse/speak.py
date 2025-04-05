import pyttsx3
import os
from gtts import gTTS
import pygame
import time

engine = pyttsx3.init()
engine.setProperty('rate', 150)



def check_internet():
    response = os.system("ping -n 1 google.com")
    return response == 0

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
    # if os.path.exists("./sound_notification/voice.mp3"):
    #     os.remove("./sound_notification/voice.mp3")

def speak(text):
    if check_internet():
        speak_google_tts(text)
    else:
        speak_pyttsx3(text)

