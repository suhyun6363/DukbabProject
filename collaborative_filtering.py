import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

# 10명의 사용자가 10개의 음식 중에서 선호하는 음식 (사용자 ID, 음식 ID)
user_food_choices = np.array([
    [1, 3, 7, 10],  # 사용자 1이 3, 7, 10번 음식을 선택
    [2, 1, 4, 9],  # 사용자 2가 1, 4, 9번 음식을 선택
    [3, 2, 6, 8],  # 사용자 3이 2, 6, 8번 음식을 선택
    # ... 이하 사용자들의 음식 선택 데이터 계속
    [10, 1, 5, 10]  # 사용자 10이 1, 5, 10번 음식을 선택
])

# 사용자-음식 매트릭스 생성 (10명의 사용자, 10개의 음식)
num_users = 10
num_foods = 10
user_food_matrix = np.zeros((num_users, num_foods))

# 사용자-음식 선택 정보를 매트릭스에 반영
for choice in user_food_choices:
    user_id, food_ids = choice[0], choice[1:]
    user_food_matrix[user_id - 1, food_ids - 1] = 1  # 선택한 음식의 인덱스를 1로 표시

# 사용자 간의 코사인 유사성 계산
user_similarity = cosine_similarity(user_food_matrix)

# 추천 대상 사용자 선택 (예시로 1번 사용자를 선택)
target_user_id = 1
target_user_choices = user_food_matrix[target_user_id - 1]

# 유사한 사용자들의 음식 선택 정보를 이용하여 추천 계산
similarities = user_similarity[target_user_id - 1]
predicted_choices = np.dot(similarities, user_food_matrix)

# 이미 선택한 음식은 제외하고 추천
non_chosen_foods = np.where(target_user_choices == 0)[0]
recommendations = [(food_id + 1, predicted_choices[food_id]) for food_id in non_chosen_foods]
recommendations.sort(key=lambda x: x[1], reverse=True)

if recommendations:
    top_recommendation = recommendations[0]
    print(f"사용자 1번에게 추천하는 음식: 음식 {top_recommendation[0]}")
else:
    print("더 이상 추천할 음식이 없습니다.")
