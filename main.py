

import requests
from bs4 import BeautifulSoup
import pandas as pd
import time

result = []

url = "https://www.duksung.ac.kr/main.do"

response_html = requests.get(url, verify=False)   # 해당 page 요청
time.sleep(5)  # 5초 기다린 후 요청 보내기

print(response_html.status_code)    # 정상 유무 확인용 출력 200 이면 ok


soup = BeautifulSoup(response_html.content.decode('utf-8'), 'html.parser')  # response_html.text로 출력했더니 한글이 깨져 강제 decoding

tag_tbody = soup.find('tbody')  # 테이블 내 body

tr_list = soup.select('div > table > tbody > tr')  # tr tag list 획득.

# 크롤링된 데이터를 처리하는 코드를 추가하세요.
