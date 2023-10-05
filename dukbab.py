import requests
from bs4 import BeautifulSoup

url = "https://www.duksung.ac.kr/diet/schedule.do"

html = requests.get(url, verify=False)
print(url)
if html.status_code != 200:
    print('페이지를 불러오는데 문제가 발생했습니다.')
else:
    soup = BeautifulSoup(html.text, 'html.parser')
    menus = soup.select(' div.table-responsive >  #schedule-table > tbody > tr')  # 메뉴 정보가 있는 테이블의 CSS 선택자를 사용하여 데이터 추출
    print("추출된 list :", len(menus))  
    print("내용 :", menus)  
    
    #customer_container
    #customer_container > div.table-responsive

    #schedule-table > tbody > tr:nth-child(1) > td:nth-child(2)

    results = []
    for menu in menus:
        # 각 행에서 메뉴 정보 추출
        td_list = menus.find_all('td')
        monday = td_list[0].text
        tuesday = td_list[1].text
        wednesday = td_list[2].text
        thursday = td_list[3].text
        friday = td_list[4].text


        # 추출한 정보를 리스트에 추가
        results.append({
            '월요일': monday,
            '화요일': tuesday,
            '수요일': wednesday,
            '목요일': thursday,
            '금요일': friday

        })

    # 결과 출력
    for result in results:
        print(result)

# 결과를 활용하여 필요한 처리를 진행하실 수 있습니다.
