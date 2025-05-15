from src import response_assistant,speak
import pygame

if __name__ == '__main__':
    pygame.mixer.init()
    pygame.mixer.music.load("./sound_notification/open.wav")
    pygame.mixer.music.play()
    while pygame.mixer.music.get_busy(): 
        continue
    pygame.mixer.quit()
    for text in response_assistant():
        print(f"Received: {text}")
        speak(text)
        print("Recording again...")
        