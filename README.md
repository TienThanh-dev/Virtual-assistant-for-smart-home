# Trợ lý điều khiển nhà thông minh

Đây là dự án **Trợ lý điều khiển nhà thông minh** sử dụng các công nghệ nhận diện giọng nói (speech-to-text) và phát thông báo (text-to-speech) kết hợp với khả năng điều khiển các thiết bị trong nhà. Trợ lý sẽ hỗ trợ các chức năng như điều khiển đèn, quạt, và khóa cửa thông minh thông qua lệnh giọng nói.

## Các tính năng chính

### 1. Nhận diện giọng nói (Speech-to-Text)
Sử dụng mô hình **Vosk** cho **speech-to-text** để chuyển giọng nói thành văn bản, sau đó áp dụng các công nghệ **NLU (Natural Language Understanding)** để phân tích câu lệnh và thực hiện các hành động tương ứng.

### 2. Điều khiển các vật dụng trong nhà
Trợ lý có thể điều khiển các thiết bị điện trong nhà thông qua các lệnh giọng nói như:

- **Điều khiển đèn**:
  - "Turn on/off the living room lights" – Bật/Tắt đèn phòng khách
  - "Turn on/off the small bedroom light" – Bật/Tắt đèn phòng ngủ nhỏ
  - "Turn on/off the master bedroom lights" – Bật/Tắt đèn phòng ngủ chính
  - "Turn on/off the public bathroom light" – Bật/Tắt đèn phòng tắm công cộng
  - "Turn on/off the master bathroom light" – Bật/Tắt đèn phòng tắm chính

- **Điều khiển quạt**:
  - "Turn on/off ventilation" – Bật/Tắt quạt thông gió

- **Tình trạng nhà**:
  - "Home status" – Cung cấp tình trạng hiện tại của các thiết bị trong nhà

### 3. Phát thông báo (Text-to-Speech)
Trợ lý sử dụng **text-to-speech** để phát thông báo và trả lời các câu lệnh. Khi nhận được một yêu cầu, trợ lý sẽ sử dụng giọng nói để thông báo lại kết quả hoặc trạng thái của thiết bị.

### 4. Điều khiển khóa cửa thông minh
Trợ lý hỗ trợ điều khiển **khóa cửa thông minh** và trò chuyện với bên ngoài. Chức năng này có thể được mở rộng với nhận diện khuôn mặt để **cảnh báo người lạ** và **giám sát việc ra vào**:

- **Khóa cửa thông minh**: Điều khiển đóng/mở khóa cửa từ xa.
- **Nhận diện khuôn mặt**: Nếu có người lạ, hệ thống sẽ cảnh báo người quản lý và ghi lại hình ảnh.

## Cài đặt

### 1. Cài đặt các thư viện cần thiết

Để chạy dự án, bạn cần cài đặt các thư viện trong file requirement.txt:
```bash:
pip install -r requirement.txt
sudo apt install pigpio
```

### 2. Tải file mô hình vosk
Để chạy mô hình vosk cần tải mô hình ngôn ngữ trên [vosk model](https://alphacephei.com/vosk/models 'Link title')
- Chúng tôi sử dụng english model:
  + [vosk-model-small-en-us-0.15](https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip 'Link title') 
  + Ngoài ra bạn có thể sử dụng các mô hình tiếng anh khác tùy vào nhu cầu trên tiêu chí phù hợp với cấu hình máy tính của bạn
- Sau khi tải và unzip thì chuyển cả folder vào folder models trong folder dự án
