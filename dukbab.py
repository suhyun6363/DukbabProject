import requests
from bs4 import BeautifulSoup

url = "https://www.duksung.ac.kr/diet/schedule.do"

try:
    response = requests.get(url)
    response.raise_for_status()  # Check if the request was successful

    soup = BeautifulSoup(response.content, 'html.parser')

    # 여기에서 원하는 작업을 수행할 수 있습니다.

except requests.exceptions.HTTPError as errh:
    print("HTTP Error:", errh)
except requests.exceptions.ConnectionError as errc:
    print("Error Connecting:", errc)
except requests.exceptions.Timeout as errt:
    print("Timeout Error:", errt)
except requests.exceptions.RequestException as err:
    print("Something went wrong:", err)
