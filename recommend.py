# 사용자기반 협업필터링.ver
from flask import Flask, request, jsonify
from collections import defaultdict
from heapq import nlargest

app = Flask(__name__)

# 1부터 40까지의 숫자중 랜덤으로 작성한겨
# 교수님께 여쭤볼거!!!!
# users 너무 많아지면 전송하는 숫자를 변경해도 받는 숫자가 잘 안바뀌는듯?
# 방법이 있을까요 or 그냥 유저숫자를 줄일까요

users = {
    'user1': [8, 15, 17, 20, 22],
    'user2': [3, 6, 7, 14, 33],
    'user3': [1, 5, 12, 16, 25],
    'user4': [2, 9, 13, 19, 31],
    'user5': [4, 8, 21, 27, 35],
    'user6': [5, 11, 14, 18, 36],
    'user7': [21, 22, 23, 24, 37],
    'user8': [6, 12, 24, 30, 39],
    'user9': [32, 34, 36, 38, 40],
    'user10': [12, 13, 15, 28, 32]


}

# 선택한 숫자를 제외한 모든 숫자 목록 생성
all_numbers = set(range(1, 41))


# 사용자의 추천 숫자를 저장할 딕셔너리
recommendations = defaultdict(list)

# 각 사용자에 대해 유사성 계산 및 추천 숫자 찾기
for user, numbers in users.items():
    remaining_numbers = all_numbers - set(numbers)
    similarities = defaultdict(int)
    for other_user, other_numbers in users.items():
        if other_user != user:
            for number in other_numbers:
                if number in remaining_numbers:
                    similarities[number] += 1
    top_recommendations = nlargest(3, similarities, key=similarities.get)
    recommendations[user] = top_recommendations


@app.route('/filtering', methods=['POST'])
def get_recommendations():
    try:
        # 클라이언트에서 POST로 전송한 숫자 데이터를 받아옵니다.
        numbers = set(request.json)

        # 입력받은 숫자를 제외한 모든 숫자 목록
        remaining_numbers = all_numbers - numbers

        # 추천 숫자를 계산합니다.
        similarities = defaultdict(int)
        for number in remaining_numbers:
            for user_numbers in users.values():
                if number in user_numbers:
                    similarities[number] += 1
        top_recommendations = nlargest(3, similarities, key=similarities.get)

        # 추천 숫자를 JSON 형태로 응답합니다.
        response = {
            "numbers": top_recommendations
        }
        return jsonify(response)
    except Exception as e:
        print(str(e))
        return jsonify({"error": "Internal Server Error"}), 500


if __name__ == '__main__':
    app.run(host='192.168.219.101', port=5001, debug=True)
