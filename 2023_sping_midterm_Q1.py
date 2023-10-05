from bs4 import BeautifulSoup    # bs4 package의 BeautifulSoup 만 import
import requests                  # url 요청에 필요한 requests
import pandas as pd              # pandas dataframe 사용 목적
import datetime

# parsing할 html 요청에 필요한 url
url = 'https://www.duksung.ac.kr/contents/contents.do?ciIdx=3592&menuId=1167'

html = requests.get(url) # url 요청에 따른 html정보
if html.status_code != 200: # 정상적 획득 유무 확인
    print('status_code : ', html.status_code)

#result
soup = BeautifulSoup(html.text, 'html.parser') # html beatifulsoup으로 변환환
colleges = soup.select('#result > div')        # 단과대학 정보 획득을 위한 div tag들을 list로 반환
print("추출된 # of div list :", len(colleges))  # 일단 갯 수 확인

results = []

mainUrl = 'https://www.duksung.ac.kr'          # href 의 링크에 추가할 string 설정

for collegeItem in colleges[1:]:               # 7개 div 중 첫번째는 '대학'이라 pass 두번째 div 원하는 태그 찾기 위함.
    collegeName = collegeItem.find('p').string # p 태그 내 text 획득 (단과대학 명 변수로 지정)

    departmentsList = collegeItem.select('div > ul > li > a') # 학과/전공 정보 list로 반환

    for departmentItem in departmentsList:
        departmentName = departmentItem.string               # 단과대학 명
        departmentLink = departmentItem['href']              # link
        fullUrl = mainUrl + departmentLink                   # mainUrl과 결합
        print([collegeName, departmentName, fullUrl])        # 리스트로 만들어서 출력
        results.append([collegeName, departmentName, fullUrl]) # 개별 학과 정보 results에 append




departmentInfo_df= pd.DataFrame(results, columns=('단과대학', '학과명', '바로가기')) # dataframe으로 변환
departmentInfo_df.to_csv('덕성여대학과정보.csv', encoding='cp949', mode='w', index=False) # csv파일로 저장

# dataframe groupby 함수를 통해 '단과대학'으로 그룹핑하고 count()를 통해 해당 '학과명'column으로 그룹 개수
groupedDf = departmentInfo_df.groupby('단과대학')['학과명'].count()
print(groupedDf)  # 결과 출력




