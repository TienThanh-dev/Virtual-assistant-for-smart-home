from ..stt_nlu.hybrid_nlu import predict_intent
from ..firebase_API.load_n_up_data import pull_data
from datetime import datetime
import json
import random
from ..config import RESPONSE_PARAGRAPHS
with open(RESPONSE_PARAGRAPHS, "r", encoding="utf-8") as file:
    response_para = json.load(file)
def response_assistant():
    for intent, entities in predict_intent():
        if intent == "turn_off_device":
            if entities and entities[0][0]:
                speech_text = f"turned off {entities[0][0]}"
                if len(entities) > 1 and entities[1][0]:  
                    speech_text += f" at {entities[1][0]}"
                if len(entities) > 2 and entities[2][0]:  
                    speech_text += f" {entities[2][0]}"
                yield speech_text
        elif intent == "turn_on_device":
            if entities and entities[0][0]:
                speech_text = f"turned on {entities[0][0]}"
                if len(entities) > 1 and entities[1][0]:  
                    speech_text += f" at {entities[1][0]}"
                if len(entities) > 2 and entities[2][0]:  
                    speech_text += f" {entities[2][0]}"
                yield speech_text
        elif intent == "hello":
            paragraphs = random.choice(response_para["response_hello"]["text"])
            yield paragraphs
        elif intent == "what_time":
            now = datetime.now()  # Lấy thời gian hệ thống
            formatted_time = now.strftime("%H:%M")  # Lấy giờ, phút, giây
            formatted_date = now.strftime("%Y-%m-%d")  # Lấy ngày, tháng, năm
            formatted_weekday = now.strftime("%A")
            speech_text = f"Now it is {formatted_time} on {formatted_weekday}, {formatted_date}"
            yield speech_text
        elif intent == "house_status":
            data=pull_data("/ESP32CAM")
            if data:
                speech_text = f"Current gas level is {data['gas']} units, the temperature is {data['temp']} degrees, light intensity is {data['light']} lux."
                yield speech_text
            else:
                yield "Not connected database"
        elif intent == "stop_speak":
            yield "Stop"
        elif intent == "nothing":
            pass
        else:
            print("Intent not found")
            pass