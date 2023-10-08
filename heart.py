from flask import Flask, jsonify
from collections import defaultdict
from heapq import nlargest

app = Flask(__name__)

# 10명의 사용자가 선택한 숫자 목록을 나타내는 딕셔너리
users = {
    'user1': [2, 5, 10, 15, 20],
    'user2': [1, 3, 8, 12, 25],
    'user3': [4, 7, 14, 18, 29],
    'user4': [5, 6, 13, 19, 30],
    'user5': [7, 8, 14, 15, 22],
    'user6': [9, 10, 11, 12, 25],
    'user7': [12, 14, 16, 24, 27],
    'user8': [2, 5, 13, 28, 29],
    'user9': [2, 12, 15, 22, 24],
    'user10': [1, 8, 13, 18, 24]


}

# 선택한 숫자를 제외한 모든 숫자 목록 생성
all_numbers = set(range(1, 31))

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

@app.route('/filtering')
def index():
    # user1에 대한 추천 숫자만 JSON으로 반환
    user1_recommendations = recommendations.get('user1', [])
    response = {
        "username": "user1",
        "numbers": user1_recommendations
    }
    return jsonify(response)

if __name__ == '__main__':
    app.run(host='192.168.219.101', port=5001, debug=True)
