from flask import Flask,render_template
from collections import defaultdict
from heapq import nlargest
from flask import Flask, jsonify

app = Flask(__name__, template_folder='templates')


# 10명의 사용자가 선택한 숫자 목록을 나타내는 딕셔너리
users = {
    'user1': [2, 5, 10, 15, 20],
    'user2': [1, 3, 8, 12, 25],
    'user3': [5, 7, 14, 18, 29],
    # ... 나머지 사용자들의 숫자 목록
}

# 선택한 숫자를 제외한 모든 숫자 목록 생성
all_numbers = set(range(1, 31))

# 모든 사용자의 추천 숫자를 저장할 딕셔너리
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
    json_recommendations = {user: numbers for user, numbers in recommendations.items()}
    return jsonify(json_recommendations)


if __name__ == '__main__':
    app.run(host='192.168.219.101', port=5001, debug=True)
