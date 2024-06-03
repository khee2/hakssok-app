## 앱 이름
학쏙 : 학교 주변 맛집 정보 한눈에 쏙​

## 기획의도 
단과대의 음식점 제휴를 이용하는 과정에서 느낄 수 있는 불편함 해결 
흩어진 정보(음식점 정보 및 리뷰)에 대한 번거로움을 줄이기 위함

## 기획 목적 
단과대의 음식점 제휴를 이용하는 과정에서 느낄 수 있는 불편함 해결 
흩어진 정보(음식점 정보 및 리뷰)에 대한 번거로움을 줄이기 위함

## 차별점

학교 대상 음식점 애플리케이션: 음식점에 관련된 애플리케이션은 많지만 학교를 대상으로 한 음식점 서비스는 찾기 어려움
단과대 제휴 여부, 음식점 정보, 리뷰 동시 확인 가능: 커뮤니티에 흩어져 있는 학교 주변 음식점 정보를 얻기에 용이함
단과대, 카테고리별로 음식점 정보 제공: 15개의 단과대와 5개의 카테고리(양식, 중식, 일식, 한식, 카페/주류)에 따라 제공

## 아키텍처
![그림1](https://github.com/khee2/new-mobilesw/assets/124848492/8134a2e7-b9f5-4b2f-989c-e48af747b624)

## 구현 화면 
- 스플래쉬 화면 / 로그인 화면
  
![스플레쉬 화면](https://github.com/khee2/new-mobilesw/assets/124848492/525e41cc-b3a3-44da-a5db-5c65b7f65a9e)
![로그인 (3)](https://github.com/khee2/new-mobilesw/assets/124848492/8c376ae3-3e33-4777-98db-31955059ac20)

- 대학선택 화면 / 홈화면
  
![스플레쉬 화면 (2)](https://github.com/khee2/new-mobilesw/assets/124848492/3eca33ca-a279-4e3b-8069-2304e3a8fba3)
![홈화면 (1)](https://github.com/khee2/new-mobilesw/assets/124848492/7a916dbb-b484-4792-830a-3587d43c0c3e)

- 맛집 모아보기 / 제휴 모아보기
  
![맛집 모아보기 (1)](https://github.com/khee2/new-mobilesw/assets/124848492/a140d955-0fe2-446c-b9c1-0a6f0c517575)
![제휴 모아보기](https://github.com/khee2/new-mobilesw/assets/124848492/128ee537-6428-499f-af3b-1ecb4dcb3709)

- 가게 리뷰 보기 / 리뷰 등록 / 내 리뷰 관리
  
![가게 정보   리뷰 (1)](https://github.com/khee2/new-mobilesw/assets/124848492/326e8977-8efa-4a61-af7e-a299a4836778)
![리뷰 관리; 오른족 스와이프 (2)](https://github.com/khee2/new-mobilesw/assets/124848492/1f0669b7-32e5-45ce-af97-5b65a7cede0c)
![리뷰 등록](https://github.com/khee2/new-mobilesw/assets/124848492/695930c1-6ded-4726-a4e5-7ade238dc105)

- 프로필 화면
  
![프로필 화면](https://github.com/khee2/new-mobilesw/assets/124848492/29176f95-5d3b-41cf-a7b0-13e5d3b8b790)

## 사용 기술 
Firebase Storage: 사진, 오디오, 동영상 등의 콘텐츠를 빠르고 쉽게 저장할 수 있도록 설계된 저장 서비스. 사용자 리뷰에서 사진 저장을 위해 사용
Firebase Database: 모바일 및 웹 앱용 데이터를 저장, 동기화, 쿼리할 수 있게 해주는 NoSQL 기반 문서 데이터베이스. 사용자 정보와 리뷰, 가게 정보 저장을 위해 사용
Google Login API: 사용자 가입 또는 로그인 시 인증 서비스를 웹, 앱에 간편하게 추가 가능. 사용자 인증 토큰을 이용하여 프로필, 리뷰 관리에 사용
Google Map API: 모바일 앱, 웹에 Google 지도를 삽입하거나 데이터를 가져올 수 있는 서비스. 음식점 선택 시 해당 음식점의 위치를 지도에 마커로 표시함 


