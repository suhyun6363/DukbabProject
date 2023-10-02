from bs4 import BeautifulSoup #HTML parsing을 위해 선언
import requests               #URL 요청 후 HTML 정보 획득을 위해 선언
import pandas as pd           #dataframe(2차원 표 구조) 형태의 자료 구조 사용을 위해 선언



#아래와 같은 column 을 갖는 dataframe 설정
#이유는 crawling할 대상 중 영양소 항목이 어떤건 있고, 어떤건 없어서 미리 dataframe 구조를 지정함.
df_AllMenuInfo = pd.DataFrame(columns=('열량Kcal', '포화지방g', '나트륨mg','탄수화물g','당g', '카페인mg', '단백질g'))

for page_num in range(1,4):  # page num(1, 2, 3)를 변경하면서 url 요청 후 crawling
    url = f'https://www.coffeebeankorea.com/menu/list.asp?page={page_num}&category=32&category2=1'
    response = requests.get(url)  # 해당 url 요청
    print(url)  # 반복처리되는지 확인하기 위해 프린트문 삽입
    if response.status_code == 200:  # url 요청 결과 정상 유무 확인
        soup = BeautifulSoup(response.content, 'html.parser')  # 응답으로 받은 html 파싱을 위해 bs4 객체로 변환/사용할 parser 지정

        #메뉴 정보가 포함된 li 태그 리스트로 반환
        menu_list = soup.select('#contents > div > div > ul > li') # 해당 page 내 menu list 관련 tag 획득

        for menu_item in menu_list:     # 각 메뉴(li 태그)에 대한 탐색
            #menu_item이  #contents > div > div > ul > li:nth-child(n)을 나타냄으로
            #     #contents > div > div > ul > li:nth-child(1) > dl > dt > span.kor
            # dl > dt > span.kor 여기서 접근
            menuName = menu_item.select_one('dl > dt > span.kor').string # 메뉴이름

            # 이 부분도 마찮가지...  div > dl 를 모두 가져온다...
            #     #contents > div > div > ul > li:nth-child(1) > div > dl.bg1
            nutritionList = menu_item.select('div > dl')                 # 영양소 리스트 tag 가져옴

            calorieNameList = []     # 영양소 명을 저장할 리스트
            calorieValueList = []    # 영양소 값을 저장할 리스트

            for nutritionItem in nutritionList:                          # 영양소 각 항목 별 정보 획득
                calorieValue = nutritionItem.select_one('dt').string     # 해당 영양소 값
                #calorieValue = str(calorieValue)                         # bs4통해 가져온 값이 navigablestring 타입이라 바로  정수 or 실수로 casting되지 않아 str()로 일단 변환
                #calorieValue = float(calorieValue)                       # 실수형으로 casting dataframe에 연속형(숫자) 데이터로 입력하기 위함.
                calorieName = nutritionItem.select_one('dd').text        # 영양소 명 가져옴
                calorieName = calorieName.replace(' ', '')               # 공백 제거
                calorieNameList.append(calorieName)                      # 해당 메뉴 하나당 dataframe으로 변환하기 위해 list 타입으로 생성
                calorieValueList.append(calorieValue)                    # 해당 메뉴 하나당 dataframe으로 변환하기 위해 list 타입으로 생성

            # 메뉴 하나당 메뉴명->index, 영양소명->column으로, 영양소 수치->data로 dataframe 생성
            df_menu = pd.DataFrame(index=[menuName], data=[calorieValueList], columns=calorieNameList)
            df_AllMenuInfo = pd.concat([df_AllMenuInfo, df_menu])  # 처음 만들었던 dataframe에 concat(결합방향 axis=0 초기값)

            # 값 확인 위해 출력해 봄
            print(menuName)
            print(calorieNameList)
            print(calorieValueList)


        #print(df_AllMenuInfo)

    else:
        print('error')


print('-------------------------------------------------')
print(df_AllMenuInfo)
# 파일로 저장해 봄
df_AllMenuInfo.to_csv('coffeeBeanMenuINfo.csv', encoding='cp949', mode='w', index=True) # index(메뉴명) 표시해서 저장


df_AllMenuInfo = df_AllMenuInfo.astype(float)  # dataframe 내 실수로 변경가능한 모든 항목에 대하여 float으로 변경
df_AllMenuInfo_dropna = df_AllMenuInfo.dropna(axis=0) # null / NaN 값이 포함된 항목 제거(열 방향)
print(df_AllMenuInfo_dropna)

print('신메뉴 평균 열량 : ', df_AllMenuInfo['열량Kcal'].mean()) # 평균 열량 산출
# 열량이 최대인 값을 먼저 찾고, 최대값과 일치하는 항목을 찾음
print(df_AllMenuInfo[df_AllMenuInfo['열량Kcal']==df_AllMenuInfo['열량Kcal'].max()])


