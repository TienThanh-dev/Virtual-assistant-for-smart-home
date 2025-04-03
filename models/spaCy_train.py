from spacy.cli.train import train

config_path = "./models/config.cfg"
output_path = "./models/spaCy_output"

train(config_path, output_path)

print("✅ Hoàn thành huấn luyện với tất cả tập dữ liệu!")



# import spacy

# # Load model đã train
# nlp = spacy.load("./models/spaCy_output/model-best")  # Đảm bảo đường dẫn đúng


# # Câu test
# text = "Turn on the living room light"
# text2 = "Power on the living room light"
# text3 = "Switch off the small bedroom light at twenty zero zero"
# text4 = "Turn off the public bathroom light at eight o'clock in the evening"
# text5 = "Turn off the ventilation at half past eight pm"
# text6 = "Turn off the master bathroom light at twenty forty five"
# # Dự đoán
# doc = nlp(text)
# doc2 = nlp(text2)
# doc3 = nlp(text3)
# doc4 = nlp(text4)
# doc5 = nlp(text5)
# doc6 = nlp(text6)
# # Hiển thị kết quả
# print("📌 Văn bản:", text)
# print("🔍 Entities:", [(ent.text, ent.label_) for ent in doc.ents])
# print("📌 Văn bản 2:", text2)
# print("🔍 Entities 2:", [(ent.text, ent.label_) for ent in doc2.ents])
# print("📌 Văn bản 3:", text3)
# print("🔍 Entities 3:", [(ent.text, ent.label_) for ent in doc3.ents])
# print("📌 Văn bản 4:", text4)
# print("🔍 Entities 4:", [(ent.text, ent.label_) for ent in doc4.ents])
# print("📌 Văn bản 5:", text5)
# print("🔍 Entities 5:", [(ent.text, ent.label_) for ent in doc5.ents])
# print("📌 Văn bản 6:", text6)
# print("🔍 Entities 6:", [(ent.text, ent.label_) for ent in doc6.ents])


 