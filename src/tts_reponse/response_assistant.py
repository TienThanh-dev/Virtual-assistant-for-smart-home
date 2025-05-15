from ..stt_nlu.hybrid_nlu import predict_intent
from ..firebase_API.load_n_up_data import pull_data,push_data
from ..device_controller.gpio_controller import gpio_controller
from datetime import datetime
import json
import random
from ..config import RESPONSE_PARAGRAPHS
with open(RESPONSE_PARAGRAPHS, "r", encoding="utf-8") as file:
    response_para = json.load(file)
field_map = {
    "living room light": "livingRoomLight",
    "small bedroom light": "smallBedroomLight",
    "master bedroom light": "masterBedroomLight",
    "public bathroom light": "publicBathroomLight",
    "master bathroom light": "masterBathroomLight",
    "ventilation": "ventilation",
}
dc=gpio_controller()

def response_assistant():
    try:
        for intent, entities in predict_intent():
            if intent == "turn_off_device":
                if entities and entities[0][0]:
                    speech_text = f"turned off {entities[0][0]}"
                    if len(entities) > 1 and entities[1][0]:  
                        speech_text += f" at {entities[1][0]}"
                    if len(entities) > 2 and entities[2][0]:  
                        speech_text += f" {entities[2][0]}"
                    dc.turn_off_device(entities[0][0])
                    field_name = field_map.get(entities[0][0].lower())
                    push_data(f"/ESP32CAM/{field_name}", False)
                    yield speech_text
            elif intent == "turn_on_device":
                if entities and entities[0][0]:
                    speech_text = f"turned on {entities[0][0]}"
                    if len(entities) > 1 and entities[1][0]:  
                        speech_text += f" at {entities[1][0]}"
                    if len(entities) > 2 and entities[2][0]:  
                        speech_text += f" {entities[2][0]}"
                    dc.turn_on_device(entities[0][0])
                    field_name = field_map.get(entities[0][0].lower())
                    push_data(f"/ESP32CAM/{field_name}", True)
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
                if data:
                    speech_text = (
                        f"Current gas level is {data['gas']} units, toxic gas level is {data['toxicGas']} units, "
                        f"humidity is {data['humidity']} percent, and temperature is {data['temp']} degrees."
                    )
                    yield speech_text
                else:
                    print("Not connected database")
                    try:
                        data=dc.uart_receive()
                        data.split(",")
                        speech_text = (
                            f"Current gas level is {data['gas']} units, toxic gas level is {data['toxicGas']} units, "
                            f"humidity is {data['humidity']} percent, and temperature is {data['temp']} degrees."
                        )
                        yield speech_text
                    except:
                        dc.uart_close()
                        print("ERROR RECEIVE UART")
            elif intent == "open":
                try:
                    dc.uart_send("open")
                except:
                    dc.uart_close()
                    print("ERROR SEND UART")
            elif intent == "close":
                try:
                    dc.uart_send("close")
                except:
                    dc.uart_close()
                    print("ERROR SEND UART")
            else:
                print("Intent not found")
    except KeyboardInterrupt:
        print("Assistant stopped.")
    finally:
        dc.uart_close()
        print("UART closed.")          