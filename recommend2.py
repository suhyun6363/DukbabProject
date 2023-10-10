# 아이템기반 협업필터링.ver
from collections import defaultdict
from math import sqrt
from heapq import nlargest


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

# 사용자가 갖고 있는 아이템(숫자) 목록 생성
items_users = defaultdict(list)
for user, items in users.items():
    for item in items:
        items_users[item].append(user)

# 아이템 간의 유사성 계산 (코사인 유사성 사용)
item_similarities = defaultdict(dict)
for item, users_with_item in items_users.items():
    for other_item, other_users_with_item in items_users.items():
        if item != other_item:
            common_users = set(users_with_item) & set(other_users_with_item)
            similarity = len(common_users) / sqrt(len(users_with_item) * len(other_users_with_item))
            item_similarities[item][other_item] = similarity

# user1이 선택한 숫자
selected_numbers = users['user1']

# user1이 선택한 숫자와 유사한 상위 3개 아이템 찾기
similar_items = defaultdict(float)
for number in selected_numbers:
    for item, similarity in item_similarities[number].items():
        similar_items[item] += similarity

# user1이 선택한 숫자를 제외하고, 유사도를 기준으로 상위 3개 아이템 찾기
top_similar_items = nlargest(3, similar_items, key=similar_items.get)

# 결과 출력
print(f"user1이 선택한 숫자 {selected_numbers}와 유사한 상위 3개 아이템: {top_similar_items}")

