import firebase_admin
from firebase_admin import credentials, db
# Khởi tạo Firebase
cred = credentials.Certificate(r".\src\firebase_API\esp32cam-dcfbf-firebase-adminsdk-fbsvc-2faf44798d.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://esp32cam-dcfbf-default-rtdb.asia-southeast1.firebasedatabase.app'
})

def push_data(path, data):
    ref = db.reference(path)
    ref.set(data)

def pull_data(path):
    ref = db.reference(path)
    data = ref.get()
    return data
def delete_data(path):
    ref = db.reference(path)
    ref.delete() 