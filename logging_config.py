import logging

def setup_logging():
    logging.basicConfig(
        level=logging.INFO,  # Mức độ log chung (có thể thay đổi thành DEBUG khi cần)
        format="%(asctime)s [%(levelname)s] %(message)s",
        handlers=[
            logging.StreamHandler(),                # In log ra console
            logging.FileHandler("app_log.txt")       # Ghi log vào file
        ]
    )
