from src import response_assistant,speak,audio_event

if __name__ == '__main__':
  
    for text in response_assistant():
        print(f"Received: {text}")
        audio_event.clear()
        speak(text)
        audio_event.set()
        print("Recording again...")