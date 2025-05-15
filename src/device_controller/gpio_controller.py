import pigpio
import json
class DeviceController:
    def __init__(self, mapping_path="./src/device_controller/devices_mapping.json"):
        self.pi = pigpio.pi()
        if not self.pi.connected:
            raise RuntimeError("Không thể kết nối với pigpio daemon!")

        with open(mapping_path, "r", encoding="utf-8") as file:
            self.mapping = json.load(file)
        self.uart_handle = None
        self._uart_buffer = ""
    def turn_on_device(self, device):
        try:
            self.pi.set_mode(self.mapping[device], pigpio.OUTPUT)
            self.pi.write(self.mapping[device], 1)
        except Exception as e:
            print(f"Lỗi bật thiết bị {device}: {e}")

    def turn_off_device(self, device):
        try:
            self.pi.set_mode(self.mapping[device], pigpio.OUTPUT)
            self.pi.write(self.mapping[device], 0)
        except Exception as e:
            print(f"Lỗi tắt thiết bị {device}: {e}")

    def set_pwm(self, device, duty_cycle, frequency):
        duty_cycle = max(0, min(100, duty_cycle))  # Giới hạn từ 0 đến 100
        try:
            self.pi.set_mode(device, pigpio.OUTPUT)
            self.pi.set_PWM_frequency(device, frequency)
            pwm_range = min(1_000_000 // frequency, 10_000)
            self.pi.set_PWM_range(device, pwm_range)
            pwm_value = int((duty_cycle / 100) * pwm_range)
            self.pi.set_PWM_dutycycle(device, pwm_value)
        except Exception as e:
            print(f"Lỗi thiết lập PWM: {e}")

    def uart_init(self, tx=14, rx=15, baud=9600):
        try:
            self.pi.set_mode(tx, pigpio.ALT5)
            self.pi.set_mode(rx, pigpio.ALT5)
            self.uart_handle = self.pi.serial_open("/dev/serial0", baud)
            return self.uart_handle
        except Exception as e:
            print(f"Lỗi mở UART: {e}")
            return None

    def uart_send(self, data):
        if self.uart_handle is None:
            print("UART chưa được khởi tạo!")
            return
        try:
            data = data.strip() + "\n"
            self.pi.serial_write(self.uart_handle, data.encode())
            print(f"Đã gửi: {data}")
        except Exception as e:
            print(f"Lỗi gửi UART: {e}")

    def uart_receive(self):
        if self.uart_handle is None:
            print("UART chưa được khởi tạo!")
            return None
        try:
            count, data = self.pi.serial_read(self.uart_handle)
            if count > 0:
                try:
                    decoded = data.decode('utf-8')
                except UnicodeDecodeError:
                    decoded = data.decode('utf-8', errors='ignore')

                self._uart_buffer += decoded

                if '\n' in self._uart_buffer:
                    lines = self._uart_buffer.split('\n')
                    complete_line = lines[0].strip()
                    self._uart_buffer = '\n'.join(lines[1:])
                    print(f"{complete_line}")
                    return complete_line
        except Exception as e:
            print(f"Lỗi đọc UART: {e}")
        return None

    def uart_close(self):
        if self.uart_handle is not None:
            self.pi.serial_close(self.uart_handle)
            print("UART đã đóng.")
dc=DeviceController()
try:
    dc.uart_init(baud=115200)
except:
    print("ERROR OPEN UART")
def gpio_controller():
    return dc