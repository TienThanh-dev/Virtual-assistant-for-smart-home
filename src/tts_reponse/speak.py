import pyttsx3
import os
from gtts import gTTS
import time
import pygame

engine = pyttsx3.init()
engine.setProperty('rate', 150)

pygame.mixer.init()

def check_internet():
    response = os.system("ping -n 1 google.com")
    return response == 0

def speak_pyttsx3(text):
    engine.say(text)
    engine.runAndWait()

def speak_google_tts(text):
    tts = gTTS(text=text, lang='en')  # Lang 'vi' cho ti?ng Vi?t
    tts.save("./sound_notification/voice.mp3")
    pygame.mixer.music.load("./sound_notification/voice.mp3")
    pygame.mixer.music.play()
    while pygame.mixer.music.get_busy(): 
        continue

def speak(text):
    if check_internet():
        speak_google_tts(text)
    else:
        speak_pyttsx3(text)

