import pigpio
import json
pi = pigpio.pi()
if not pi.connected:
    print("Không thể kết nối với pigpio daemon!")
with open("./src/device_controller/devices_mapping.json", "r", encoding="utf-8") as file:
    mapping = json.load(file)
def turn_on_device(device):
    try:
        pi.set_mode(mapping[device], pigpio.OUTPUT)
        pi.write(mapping[device],1)
    except Exception as e:
        print("Lỗi {e}")
def turn_off_device(device):
    try:
        pi.set_mode(mapping[device], pigpio.OUTPUT)
        pi.write(mapping[device],0)
    except Exception as e:
        print("Lỗi {e}")
def set_pwm(device, duty_cycle,frequency):
    if duty_cycle < 0:
        duty_cycle = 0
    elif duty_cycle > 100:
        duty_cycle = 100
    try:
        pi.set_mode(device, pigpio.OUTPUT)
        pi.set_PWM_frequency(device, frequency)
        # Lấy khoảng PWM phù hợp (giới hạn tối đa 10_000 để tránh lỗi)
        range_ = min(1_000_000 // frequency, 10_000)
        pi.set_PWM_range(device, range_)
        # Tính giá trị duty_cycle phù hợp với khoảng PWM
        pwm_value = int((duty_cycle / 100) * range_)
        pi.set_PWM_dutycycle(device, pwm_value)
    except Exception as e:
        print(f"Lỗi thiết lập PWM: {e}")
def uart_init(tx=14, rx=15, baud=9600):
    """
    - tx: Chân GPIO TX (mặc định GPIO 14).
    - rx: Chân GPIO RX (mặc định GPIO 15).
    """
    try:
        pi.set_mode(tx, pigpio.ALT5)
        pi.set_mode(rx, pigpio.ALT5)
        uart_handle = pi.serial_open("/dev/serial0", baud)
        return uart_handle
    except Exception as e:
        print(f"Lỗi mở UART: {e}")
        return None
    
def uart_send(handle, data):
    if handle is None:
        print("UART chưa được khởi tạo!")
        return
    try:
        pi.serial_write(handle, data.encode())
        print(f"Đã gửi: {data}")
    except Exception as e:
        print(f"Lỗi gửi UART: {e}")

def uart_receive(handle):
    if handle is None:
        print("UART chưa được khởi tạo!")
        return None
    try:
        count, data = pi.serial_read(handle)
        if count > 0:
            received_data = data.decode()
            print(f"Nhận được: {received_data}")
            return received_data
    except Exception as e:
        print(f"Lỗi nhận UART: {e}")
    return None
def uart_close(handle):
    if handle is not None:
        pi.serial_close(handle)
        print("UART đã đóng.")
        