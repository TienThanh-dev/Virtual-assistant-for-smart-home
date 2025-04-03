from .speech_recognition import recognize_speech
from ..config import MODEL_PATH_SPA, MODEL_PATH_TFIDF_SVM
import os
import spacy
import joblib

# Load model đã train
nlp = spacy.load(MODEL_PATH_SPA)
svm_model = joblib.load(MODEL_PATH_TFIDF_SVM)
# Dự đoán
def predict_intent():
    for text in recognize_speech():
        # text="activate lights in the living room at twenty three zero zero weekends only"
        doc = nlp(text)
        # Trả về kết quả
        yield svm_model.predict([text])[0], [(ent.text, ent.label_) for ent in doc.ents]
# doc = nlp(text)
# print(svm_model.predict([text])[0], [(ent.text, ent.label_) for ent in doc.ents])
# print("Intent:", svm_model.predict([text])[0])
# print(type(svm_model.predict([text])[0]))
# entities=[(ent.text, ent.label_) for ent in doc.ents]
# print("Entities:", entities[1][0])