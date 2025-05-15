import json
import itertools
import spacy
from spacy.tokens import DocBin
import psutil
import random

target_count = 300000  # Số lượng câu lệnh mong muốn
with open(r'.\DATN\dataset\dataset_snipsNLU.json', 'r', encoding='utf-8') as f:
    dataset = json.load(f)

intent_names = ["turn_on_device", "turn_off_device","hello","what_time","house_status","open","close","nothing"]
training_data = []
while len(training_data) < target_count:
    # Chọn một intent ngẫu nhiên từ danh sách intent_names
    intent_name = random.choice(intent_names)
    if intent_name not in dataset['intents']:
        continue

    utterances = dataset['intents'][intent_name]['utterances']
    devices = [synonym for entry in dataset['entities'].get('device', {}).get('data', []) for synonym in entry["synonyms"]] or [None]
    times = [synonym for entry in dataset['entities'].get('hour', {}).get('data', []) for synonym in entry["synonyms"]] or [None]
    frequencies = [synonym for entry in dataset['entities'].get('frequency', {}).get('data', []) for synonym in entry["synonyms"]] or [None]
    utterance = random.choice(utterances)
    text_template = utterance['text']
    contains_time = "{hour}" in text_template
    contains_frequency = "{frequency}" in text_template
    contains_device = "{device}" in text_template 
    text = text_template
    entities = []

    if contains_device:
        device=random.choice(devices) if devices else None
    if contains_time:
        time=random.choice(times) if times else None
    if contains_frequency:
        frequency=random.choice(frequencies) if frequencies else None
    if devices or times or frequencies:
        if contains_device and device:
            start_idx = text.find("{device}")
            text = text.replace("{device}", device)
            entities.append({"start": start_idx, "end": start_idx + len(device), "label": "device"})
        if contains_time and time:
            start_idx = text.find("{hour}")
            text = text.replace("{hour}", time)
            entities.append({"start": start_idx, "end": start_idx + len(time), "label": "time"})
        if contains_frequency and frequency:
                start_idx = text.find("{frequency}")
                text = text.replace("{frequency}", frequency)
                entities.append({"start": start_idx, "end": start_idx + len(frequency), "label": "frequency"})
    if training_data[-1:] == [] or training_data[-1:][0]["text"] != text:
            training_data.append({
                "text": text,
                "intent": intent_name,
                "entities": entities
            })
nlp=spacy.blank("en")
nb_file=0#đánh dấu số lượng file spacy
doc_bin = DocBin()
for item in training_data:
    text = item["text"]
    entities = item["entities"]
    doc = nlp.make_doc(text)
    ents = []
    for ent in entities:
        start, end, label = ent["start"], ent["end"], ent["label"]
        span = doc.char_span(start, end, label=label, alignment_mode="contract")
        if span is not None:
            ents.append(span)
    doc.ents = ents
    doc_bin.add(doc)
    # Lưu file để train spaCy
output_file = f"./DATN/dataset/nlu_datasets/spaCy_train.spacy"
doc_bin.to_disk(output_file)
# **Lưu file JSON**
output_file = r'C:\Users\MY PC\Desktop\DATN\DATN\dataset\nlu_datasets\training_data.json'
with open(output_file, 'w', encoding='utf-8') as f:
    json.dump(training_data, f, indent=4, ensure_ascii=False)
output_file = r'C:\Users\MY PC\Desktop\DATN\DATN\dataset\nlu_datasets\intern_training_data.json'
texts = [item["text"] for item in training_data]
labels = [item["intent"] for item in training_data]
data = {"texts": texts, "labels": labels}
with open(output_file, "w", encoding="utf-8") as f:
    json.dump(data, f, ensure_ascii=False, indent=4)

# devices = [entry for entry in dataset['entities'].get('device', {}).get('data', [])] or [None]
# times = [entry for entry in dataset['entities'].get('hour', {}).get('data', [])] or [None]
# frequencies = [entry for entry in dataset['entities'].get('frequency', {}).get('data', [])] or [None]
# synonym_to_value = {}
# for dict in [devices, times, frequencies]:
#     for entry in dict:
#         value = entry["value"]
#         for synonym in entry["synonyms"]:
#             synonym_to_value[synonym] = value
# with open("synonyms_mapping.json", "w", encoding="utf-8") as file:
#     json.dump(synonym_to_value, file, ensure_ascii=False, indent=4)