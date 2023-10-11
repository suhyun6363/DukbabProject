

from flask import Flask, jsonify
from flask_cors import CORS
from datetime import datetime, timedelta
import requests

app = Flask(__name__)

CORS(app)

class DietItem:
    def __init__(self, diet_date, diet_title, sdow):
        self.diet_date = diet_date
        self.diet_title = diet_title
        self.sdow = sdow

@app.route('/get_diet_schedule', methods=['GET'])
def get_diet_schedule():
    today = datetime.today()
    start_date = (today - timedelta(days=today.weekday())).strftime('%Y-%m-%d')
    end_date = (today + timedelta(days=4 - today.weekday())).strftime('%Y-%m-%d')

    diet_items = fetch_diet_schedule(start_date, end_date)
    if diet_items:
        output_data = []
        for item in diet_items:
            output_data.append({
                "날짜": item.diet_date,
                "요일": item.sdow,
                "식단": item.diet_title
            })
        return jsonify(output_data)
    else:
        return jsonify({"error": "데이터를 가져오지 못했습니다."})

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
    pass

# 172.20.8.37
if __name__ == '__main__':
    app.run(host='172.20.8.37', port=5000, debug=True)

    # 집 192.168.219.101
    # 학교 172.20.8.37
    
