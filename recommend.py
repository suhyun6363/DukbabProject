from flask import Flask, request, jsonify
from collections import defaultdict
from heapq import nlargest

app = Flask(__name__)

"""
1번 가게의 메뉴 : 1,2
2번 가게의 메뉴 : 3,4,5,6,7,8
3번 가게의 메뉴 : 9~25
4번 가게의 메뉴 : 26~30
5번 가게의 메뉴 : 31~40
"""



# 기존 사용자 데이터와 메뉴 구성
users = {
    'user1': [1, 3, 9, 26, 31],
    'user2': [2, 4, 10, 27, 32],
    'user3': [5, 11, 28, 33, 6],
    'user4': [12, 29, 34, 7, 13],
    'user5': [30, 35, 8, 14, 15],
    'user6': [36, 16, 17, 18, 19],
    'user7': [20, 21, 22, 23, 24],
    'user8': [25, 37, 38, 39, 40],
    'user9': [1, 3, 4, 9, 27],
    'user10': [3, 4, 8, 9, 40]
}

# 각 가게의 메뉴 구성
menu = {
    1: [1,2],
    2: [3, 4, 5, 6, 7, 8],
    3: list(range(9, 25 + 1)),
    4: list(range(26, 30 + 1)),
    5: list(range(31, 40 + 1))
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

        # 사용자가 선택한 숫자는 제외하고 추천 숫자 중 상위 3개
        top_recommendations = nlargest(3, set(recommended_numbers) - user_preferences)

        # 나머지 두 개의 숫자를 해당 가게의 다른 숫자에서 선택하여 추가
        remaining_recommendations = 5 - len(top_recommendations)
        if remaining_recommendations > 0:
            most_common_menu = max(menu, key=lambda x: len(set(menu[x]) & user_preferences))
            additional_recommendations = list(set(menu[most_common_menu]) - user_preferences)
            top_recommendations.extend(nlargest(remaining_recommendations, additional_recommendations))

        # 사용자가 선택한 숫자가 해당 가게의 모든 메뉴에 포함되어 있다면, 다른 가게에서 상위 5개의 숫자를 추천
        if len(top_recommendations) < 5:
            for store, menu_items in menu.items():
                if store != most_common_menu:
                    additional_recommendations = list(set(menu_items) - user_preferences)
                    top_recommendations.extend(nlargest(5 - len(top_recommendations), additional_recommendations))

        response = {
            "numbers": list(top_recommendations)
        }
        return jsonify(response)
    except Exception as e:
        print(str(e))
        return jsonify({"error": "Internal Server Error"}), 500

if __name__ == '__main__':
    app.run(host='172.20.8.37', port=5001, debug=True)

    # 집 192.168.219.101
    # 학교 172.20.8.37
