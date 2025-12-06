import requests
import os

covers = {
    "python-crash-course.jpg": "https://covers.openlibrary.org/b/isbn/9781593276034-L.jpg",
    "design-patterns.jpg": "https://covers.openlibrary.org/b/isbn/9780201633610-L.jpg",
    "clean-code.jpg": "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg",
    "refactoring.jpg": "https://covers.openlibrary.org/b/isbn/9780201485677-L.jpg",
    "intro-to-algo.jpg": "https://covers.openlibrary.org/b/isbn/9780262033848-L.jpg",
    "head-first-java.jpg": "https://covers.openlibrary.org/b/isbn/9780596009205-L.jpg",
    "effective-java.jpg": "https://covers.openlibrary.org/b/isbn/9780134685991-L.jpg",
    "art-of-computer-programming.jpg": "https://covers.openlibrary.org/b/isbn/9780201896831-L.jpg",
    "mythical-man-month.jpg": "https://covers.openlibrary.org/b/isbn/9780201835953-L.jpg",
    "pragmatic-programmer.jpg": "https://covers.openlibrary.org/b/isbn/9780201616224-L.jpg"
}

save_dir = "d:/OneDrive/桌面/二手书交易系统/frontend/public/picture"
if not os.path.exists(save_dir):
    os.makedirs(save_dir)

for filename, url in covers.items():
    try:
        print(f"Downloading {filename}...")
        response = requests.get(url, timeout=10)
        if response.status_code == 200:
            with open(os.path.join(save_dir, filename), 'wb') as f:
                f.write(response.content)
            print(f"Saved {filename}")
        else:
            print(f"Failed to download {filename}: Status {response.status_code}")
    except Exception as e:
        print(f"Error downloading {filename}: {e}")
