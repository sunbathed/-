import requests
import os

# 1. Download Background Image
bg_url = "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?ixlib=rb-4.0.3&auto=format&fit=crop&w=2000&q=80"
save_dir = "d:/OneDrive/桌面/二手书交易系统/frontend/public/picture"
if not os.path.exists(save_dir):
    os.makedirs(save_dir)

print("Downloading login background...")
try:
    resp = requests.get(bg_url, timeout=15)
    if resp.status_code == 200:
        with open(os.path.join(save_dir, "login-bg.jpg"), 'wb') as f:
            f.write(resp.content)
        print("Saved login-bg.jpg")
    else:
        print(f"Failed to download bg: {resp.status_code}")
except Exception as e:
    print(f"Error downloading bg: {e}")

# 2. Download 30 Book Covers
covers = {
    # Tech
    "python-crash-course.jpg": "https://covers.openlibrary.org/b/isbn/9781593276034-L.jpg",
    "design-patterns.jpg": "https://covers.openlibrary.org/b/isbn/9780201633610-L.jpg",
    "clean-code.jpg": "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg",
    "refactoring.jpg": "https://covers.openlibrary.org/b/isbn/9780201485677-L.jpg",
    "intro-to-algo.jpg": "https://covers.openlibrary.org/b/isbn/9780262033848-L.jpg",
    "head-first-java.jpg": "https://covers.openlibrary.org/b/isbn/9780596009205-L.jpg",
    "effective-java.jpg": "https://covers.openlibrary.org/b/isbn/9780134685991-L.jpg",
    "art-of-computer-programming.jpg": "https://covers.openlibrary.org/b/isbn/9780201896831-L.jpg",
    "mythical-man-month.jpg": "https://covers.openlibrary.org/b/isbn/9780201835953-L.jpg",
    "pragmatic-programmer.jpg": "https://covers.openlibrary.org/b/isbn/9780201616224-L.jpg",
    "code-complete.jpg": "https://covers.openlibrary.org/b/isbn/9780735619678-L.jpg",
    "deep-learning.jpg": "https://covers.openlibrary.org/b/isbn/9780262035613-L.jpg",
    "k8s-up-and-running.jpg": "https://covers.openlibrary.org/b/isbn/9781492046530-L.jpg",
    "docker-deep-dive.jpg": "https://covers.openlibrary.org/b/isbn/9781521822807-L.jpg",
    "go-programming.jpg": "https://covers.openlibrary.org/b/isbn/9780134190440-L.jpg",
    
    # Novels / Literature
    "three-body.jpg": "https://covers.openlibrary.org/b/isbn/9780765377067-L.jpg", # Three Body Problem
    "to-live.jpg": "https://covers.openlibrary.org/b/isbn/9781400031863-L.jpg", # Yu Hua - To Live
    "hundred-years-solitude.jpg": "https://covers.openlibrary.org/b/isbn/9780060883287-L.jpg",
    "kite-runner.jpg": "https://covers.openlibrary.org/b/isbn/9781594631931-L.jpg",
    "ordinary-world.jpg": "https://covers.openlibrary.org/b/isbn/9787530216824-L.jpg", # 假设有，或者用类似的
    "miracles-of-namiya.jpg": "https://covers.openlibrary.org/b/isbn/9780349140117-L.jpg",
    "white-night.jpg": "https://covers.openlibrary.org/b/isbn/9780349139364-L.jpg", # Keigo Higashino
    "catcher-in-the-rye.jpg": "https://covers.openlibrary.org/b/isbn/9780316769488-L.jpg",
    "great-gatsby.jpg": "https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg",
    "1984.jpg": "https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg",
    
    # Art / Design / Others
    "design-of-everyday-things.jpg": "https://covers.openlibrary.org/b/isbn/9780465050659-L.jpg",
    "dont-make-me-think.jpg": "https://covers.openlibrary.org/b/isbn/9780321965516-L.jpg",
    "grid-systems.jpg": "https://covers.openlibrary.org/b/isbn/9783721201451-L.jpg",
    "history-of-art.jpg": "https://covers.openlibrary.org/b/isbn/9780714833552-L.jpg",
    "about-face.jpg": "https://covers.openlibrary.org/b/isbn/9781118766576-L.jpg"
}

for filename, url in covers.items():
    try:
        filepath = os.path.join(save_dir, filename)
        if os.path.exists(filepath) and os.path.getsize(filepath) > 1000:
            print(f"Skipping {filename} (exists)")
            continue
            
        print(f"Downloading {filename}...")
        response = requests.get(url, timeout=10)
        if response.status_code == 200:
            with open(filepath, 'wb') as f:
                f.write(response.content)
            print(f"Saved {filename}")
        else:
            print(f"Failed to download {filename}: Status {response.status_code}")
    except Exception as e:
        print(f"Error downloading {filename}: {e}")
