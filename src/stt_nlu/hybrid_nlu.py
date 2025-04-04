from .speech_recognition import recognize_speech
from ..config import MODEL_PATH_SPA, MODEL_PATH_TFIDF_SVM
import json
import spacy
import joblib
from rapidfuzz import process
# Load model đã train
nlp = spacy.load(MODEL_PATH_SPA)
svm_model = joblib.load(MODEL_PATH_TFIDF_SVM)
with open("./src/stt_nlu/synonyms_mapping.json", "r", encoding="utf-8") as file:
    mapping = json.load(file)
# Mapping
def map_fuzz_entity(entities, mapping,threshold=80):
    keys=list(mapping.keys())
    for entity in entities:
        best_match = process.extractOne(entity[0], keys, score_cutoff=threshold)
        print("Best match:", best_match)
        if best_match:
            entity[0] = best_match[0]
        if entity[0] in mapping:
            entity[0] = mapping.get(entity[0], entity[0])
    return [(text, label) for text,label in entities]

# Dự đoán
def predict_intent():
    for text in recognize_speech():
        # text="activate lights in the living room at twenty three zero zero weekends only"
        doc = nlp(text)
        # Trả về kết quả
        yield svm_model.predict([text])[0], map_fuzz_entity([[ent.text, ent.label_] for ent in doc.ents], mapping)

# text="activate lights in the ling room at twty thre zero zero weends only"
# text2="Switch off the small bedom light at tweny zero zeo"
# doc = nlp(text2)
# print(svm_model.predict([text])[0], [[ent.text, ent.label_] for ent in doc.ents])
# entities=[[ent.text, ent.label_] for ent in doc.ents]

# print("Entities:", map_entity(entities, mapping))


        