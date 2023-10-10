from flask import Flask, request, jsonify
from collections import defaultdict
from heapq import nlargest

app = Flask(__name__)


# 숫자를 조작해야할까
users = {
    'user1': [1, 2, 3, 4, 5],
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

@app.route('/filtering', methods=['POST'])
def get_recommendations():
    try:
        # 클라이언트에서 POST로 전송한 사용자의 선호 숫자를 받아옵니다.
        user_preferences = set(request.json)

        # 유사한 사용자들 찾기
        similarities = defaultdict(int)
        for user, preferences in users.items():
            common_preferences = set(preferences) & user_preferences
            similarities[user] = len(common_preferences)

        # 유사도를 기반으로 유사한 사용자들을 내림차순으로 정렬합니다.
        similar_users = nlargest(3, similarities, key=similarities.get)

        # 추천 숫자
        recommended_numbers = []
        for user in similar_users:
            recommended_numbers.extend(users[user])

        # 사용자가 숫자제외, 추천 숫자 중 상위 3개
        top_recommendations = nlargest(3, set(recommended_numbers) - user_preferences)


        response = {
            "numbers": top_recommendations
        }
        return jsonify(response)
    except Exception as e:
        print(str(e))
        return jsonify({"error": "Internal Server Error"}), 500

if __name__ == '__main__':
    app.run(host='192.168.219.101', port=5001, debug=True)
