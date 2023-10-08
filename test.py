import requests
from datetime import datetime, timedelta
import json

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
                diet_title = item.get("DIET_TITLE", "")
                sdow = item.get("SDOW", "")
                diet_item = DietItem(diet_date, diet_title, sdow)
                diet_items.append(diet_item)

            return diet_items
        else:
            print("API 요청이 실패하였습니다.")
            return None
    except requests.exceptions.RequestException as e:
        print(f"API 요청 중 오류 발생: {str(e)}")
        return None

if __name__ == '__main__':
    today = datetime.today()
    start_date = (today - timedelta(days=today.weekday())).strftime('%Y-%m-%d')
    end_date = (today + timedelta(days=4 - today.weekday())).strftime('%Y-%m-%d')

    diet_items = fetch_diet_schedule(start_date, end_date)
    if diet_items:
        # 데이터를 JSON 파일로 저장
        output_data = []
        for item in diet_items:
            output_data.append({
                "날짜": item.diet_date,
                "요일": item.sdow,
                "식단": item.diet_title
            })

        with open('diet_schedule.json', 'w', encoding='utf-8') as json_file:
            json.dump(output_data, json_file, ensure_ascii=False, indent=4)

        print("데이터를 JSON 파일로 저장하였습니다.")
    else:
        print("데이터를 가져오지 못했습니다.")