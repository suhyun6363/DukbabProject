import requests
import socket
from datetime import datetime, timedelta
import time


class DietItem:
    def __init__(self, diet_date, diet_title, sdow):
        self.diet_date = diet_date
        self.diet_title = diet_title
        self.sdow = sdow


def fetch_diet_schedule(start_date, end_date):
    url = f"https://www.duksung.ac.kr/diet/ajax/scheduleList.do?startDate={start_date}&endDate={end_date}&cd=R01"

    try:
        response = requests.post(url)
        response.raise_for_status()
        data = response.json()

        if data.get("resultCode") == "0":
            diet_list = data.get("list", [])
            diet_items = []

            for item in diet_list:
                diet_date = item.get("DIET_DATE", "")
                sdow = item.get("SDOW", "")
                diet_title = item.get("DIET_TITLE", "")
                diet_item = DietItem(diet_date, diet_title, sdow)
                diet_items.append(diet_item)

            return diet_items
        else:
            print("API 요청이 실패하였습니다.")
            return None
    except requests.exceptions.RequestException as e:
        print(f"API 요청 중 오류 발생: {str(e)}")
        return None


def start_server():
    host = '192.168.219.101'  # 호스트 ip를 적어주세요
    port = 8080  # 포트번호를 임의로 설정해주세요

    server_sock = socket.socket(socket.AF_INET)
    server_sock.bind((host, port))
    server_sock.listen(1)
    print("기다리는 중..")
    out_data = int(10)

    while True:
        client_sock, addr = server_sock.accept()

        if client_sock:
            print('Connected by?!', addr)
            in_data = client_sock.recv(1024)
            print('rcv :', in_data.decode("utf-8"), len(in_data))

            while in_data:
                diet_items = fetch_diet_schedule(start_date, end_date)
                if diet_items:
                    for item in diet_items:
                        client_sock.send(
                            f"날짜: {item.diet_date}, 요일: {item.sdow}, 식단: {item.diet_title}".encode("utf-8"))
                        print(f"send: 날짜: {item.diet_date}, 요일: {item.sdow}, 식단: {item.diet_title}")
                else:
                    client_sock.send("식단 정보를 가져올 수 없습니다.".encode("utf-8"))
                    print("send: 식단 정보를 가져올 수 없습니다.")

                time.sleep(2)

        client_sock.close()
        server_sock.close()


if __name__ == '__main__':
    # 다음 주 월요일부터 일요일까지의 날짜 계산
    today = datetime.today()
    next_monday = today + timedelta(days=(7 - today.weekday()))
    next_friday = next_monday + timedelta(days=4)

    start_date = next_monday.strftime('%Y-%m-%d')
    end_date = next_friday.strftime('%Y-%m-%d')

    start_server()
