import pyttsx3
import os
from gtts import gTTS
import time
import pygame

engine = pyttsx3.init()
engine.setProperty('rate', 150)

def check_internet():
    response = os.system("ping -n 1 google.com")
    return response == 0

def speak_pyttsx3(text):
    engine.say(text)
    engine.runAndWait()

def speak_google_tts(text):
    tts = gTTS(text=text, lang='en')  # Lang 'vi' cho ti?ng Vi?t
    tts.save("temp.mp3")
    pygame.mixer.init()
    pygame.mixer.music.load("temp.mp3")
    pygame.mixer.music.play()
    while pygame.mixer.music.get_busy(): 
        time.sleep(1)

def speak(text):
    if check_internet():
        speak_google_tts(text)
    else:
        speak_pyttsx3(text)

