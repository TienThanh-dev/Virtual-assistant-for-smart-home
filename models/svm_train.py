import json
import joblib
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.svm import SVC
from sklearn.linear_model import SGDClassifier
from sklearn.pipeline import Pipeline
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report
from joblib import parallel_backend
# 1️⃣ Tải dữ liệu huấn luyện (Giả sử file JSON chứa danh sách các câu lệnh)
with open(r"C:\Users\MY PC\Desktop\DATN\dataset\intern_training_data.json", "r", encoding="utf-8") as f:
    training_data = json.load(f)

# 2️⃣ Trích xuất văn bản và nhãn
texts = training_data["texts"]
labels = training_data["labels"]
# 3️⃣ Chia dữ liệu thành tập huấn luyện và kiểm tra (80% train - 20% test)
X_train, X_test, y_train, y_test = train_test_split(texts, labels, test_size=0.2, random_state=42,stratify=labels)

# 4️⃣ Tạo pipeline TF-IDF + SVM
pipeline = Pipeline([
    ("tfidf", TfidfVectorizer()),   # Chuyển đổi văn bản thành vector TF-IDF
    ("svm",SGDClassifier(loss="hinge", max_iter=20000, tol=1e-5, 
              early_stopping=True, validation_fraction=0.1, 
              n_iter_no_change=5, class_weight="balanced", 
              n_jobs=-1))  # Mô hình SVM với kernel tuyến tính
])

with parallel_backend("loky"):
    pipeline.fit(X_train, y_train)


# 6️⃣ Đánh giá mô hình trên tập kiểm tra
y_pred = pipeline.predict(X_test)
print("🎯 Độ chính xác:", accuracy_score(y_test, y_pred))
print("📊 Báo cáo phân loại:\n", classification_report(y_test, y_pred))
# 7️⃣ Lưu mô hình để sử dụng sau này
joblib.dump(pipeline, "intent_classifier.pkl")
print("✅ Mô hình đã được lưu vào 'intent_classifier.pkl'")
