# Team 3 - Book Rental (책 대여 서비스)
## 구현 Repository
1. 책 상품 관리: https://github.com/team34final/rentabook/tree/main/Book
2. 주문 관리: https://github.com/team34final/rentabook/tree/main/Rental
3. 배송 관리: https://github.com/team34final/rentabook/tree/main/Delivery
4. 고객 페이지: https://github.com/team34final/rentabook/tree/main/Viewer
5. 게이트웨이: https://github.com/team34final/rentabook/tree/main/gateway



## Table of Contents
* 서비스 시나리오
  * 시나리오 테스트 결과
* 분석/설계
* 구현
  * DDD의 적용
  * Request-Response 아키텍쳐 구현
  * 이벤트 드라이븐 아키텍처 구현
  * Gateway 적용
  * 동기식 호출
  * 비동기식 호출과 Eventual Consistency
* 운영
  * CI/CD 설정
  * Liveness
  * HPA 적용(Autoscale)
  
  


## 서비스 시나리오
### 기능적 요구사항
1. 고객이 책 대여를 신청한다.
2. 주문이 접수된다.
3. 요청한 책의 배송이 시작된다.
4. 고객이 책 대여를 취소할 수 있다.
5. 대여가 취소되면 배송이 중지된다.
6. 고객이 자신의 주문 정보를 확인할 수 있다.

### 비기능적 요구사항
1.	트랜잭션<br>
  i.	결제가 되지 않은 주문 건은 접수가 불가하다.<br>
  ii.	재고가 0인 경우 접수가 불가하다.
2.	장애격리<br>
  i.	배송관리 기능이 수행되지 않더라도 대여 접수가 가능하다.<br>
  ii.	대여 시스템에 부하가 걸리면 대여 신청을 보류한다.<br>
  iii.	주문 취소 시 재고 관리가 되지 않더라도 취소되어야 한다.
3.	성능<br>
  i.	고객이 자신의 주문 이력을 확인할 수 있다.


## 분석/설계
AS-IS 조직(Horizontally-Aligned) -> TO-BE 조직(Vertically-Aligned)



### Event Storming
![이벤트스토밍 설계](https://user-images.githubusercontent.com/73535272/97375299-d12b4b00-18fd-11eb-9b0b-871b1e3588a2.JPG)
 
### MSA 구조
![msa구조](https://user-images.githubusercontent.com/73535272/97383342-b7dfca00-1910-11eb-8ec4-f4e19bf5440c.JPG)


## 구현
### DDD의 적용
분석/설계 단계에서 도출된 MSA는 총 4개로 다음과 같음
* 고객페이지(view)는 CQRS를 위한 서비스
1. 책 상품 관리<br>
![view_books](https://user-images.githubusercontent.com/73535272/97380457-6af8f500-190a-11eb-88df-3fbe4ef56860.jpg)
![view_books_1](https://user-images.githubusercontent.com/73535272/97380459-6c2a2200-190a-11eb-9473-5330f69cceae.JPG)
![view_books_2](https://user-images.githubusercontent.com/73535272/97380460-6cc2b880-190a-11eb-8f56-7c2f32a7b947.JPG)
![view_books_3](https://user-images.githubusercontent.com/73535272/97380463-6d5b4f00-190a-11eb-9bdf-a36fc2b26106.JPG)
2. 주문 관리<br>
![view_rentals](https://user-images.githubusercontent.com/73535272/97380482-7b10d480-190a-11eb-8dea-dec980fe395b.jpg)
3. 배송 관리<br>
![view_deliveries](https://user-images.githubusercontent.com/73535272/97380496-849a3c80-190a-11eb-8dcc-459be87a874f.jpg)
4. 고객 페이지<br>
![view](https://user-images.githubusercontent.com/73535272/97380515-8e23a480-190a-11eb-8e9f-b694a42f4f94.jpg)

### Request-Response 아키텍쳐 구현
1. Req-Res 호출<br>
![req-res_1_호출 소스](https://user-images.githubusercontent.com/73535272/97381900-64b84800-190d-11eb-991e-c18770bd9f04.JPG)

2. Req-Res 처리<br>
![req-res_2_처리 controller](https://user-images.githubusercontent.com/73535272/97381905-6a159280-190d-11eb-88b8-c2212245e8c4.JPG)

### 이벤트 드리븐 아키텍처 구현
1. Book @PostPersist에서 비동기 이벤트 생성 <br>
![비동기 Book](https://user-images.githubusercontent.com/12227092/97511051-752df880-19c9-11eb-9c9a-e3843334fbb0.JPG)

2. Rental @PostPersist에서 비동기 이벤트 생성<br>
![비동기호출 Rental](https://user-images.githubusercontent.com/12227092/97511059-7828e900-19c9-11eb-88e6-28a3d1eaa00f.JPG)

3. Delivery @PostPersist에서 비동기 이벤트 생성<br>
![비동기 Delivery](https://user-images.githubusercontent.com/12227092/97511060-79f2ac80-19c9-11eb-91b9-9d771308308b.JPG)

4. 각 Book, Rental, Delivery에서 @PostPersist 에서 생성된 이벤트 수신 <br>
![비동기호출-viewer에 저장](https://user-images.githubusercontent.com/12227092/97510662-9215fc00-19c8-11eb-8826-cfc19ff6773a.JPG)

### API 게이트웨이
1. Gateway 설정<br>
![gateway-설정](https://user-images.githubusercontent.com/12227092/97509615-20d54980-19c6-11eb-85fe-59a6a256368c.JPG)

2. Gateway rental  설정<br>
![Gateway_rental생성](https://user-images.githubusercontent.com/73535272/97379165-7f87be00-1907-11eb-9f6f-9f4ab3f7cb7e.JPG)

3. Gateway 정보조회<br>
![Gateway_정보조회](https://user-images.githubusercontent.com/73535272/97379181-8adae980-1907-11eb-80b2-1595781751d0.JPG)

4. Gateway 직접정보조회<br>
![Gateway_직접정보조회](https://user-images.githubusercontent.com/73535272/97379198-975f4200-1907-11eb-96f2-94b3a6e50938.JPG)


## 운영
### CI/CD설정
1. git 복사<br>
![CI_1_git 복사](https://user-images.githubusercontent.com/73535272/97382363-9251c100-190e-11eb-8ba7-94c13f603093.JPG)

2. maven package <br>
![CI_2_maven 패키지](https://user-images.githubusercontent.com/73535272/97382371-97af0b80-190e-11eb-90e3-29cd10060a38.JPG)

3. image 생성<br>
![CI_3_이미지 생성](https://user-images.githubusercontent.com/73535272/97382383-a1387380-190e-11eb-8b7b-67af74e02574.JPG)

4. pod deploy 생성<br>
![CI_4_pod_deploy 생성](https://user-images.githubusercontent.com/73535272/97382403-abf30880-190e-11eb-9e01-169dfa630300.JPG)

### Liveness
1. deploy.yaml 설정<br>
![Liveness_ deploy yaml](https://user-images.githubusercontent.com/12227092/97439664-ca3a2200-1969-11eb-9260-03676374e560.JPG)

2. Liveness가 스케쥴 돌면서 재기동함을 확인<br>
![Liveness_Restarts 확인](https://user-images.githubusercontent.com/12227092/97439856-0f5e5400-196a-11eb-848f-8f9b98575bd7.JPG)

3. rental 서비스가 동작중임을 확인<br>
![Liveness_ 동작중](https://user-images.githubusercontent.com/12227092/97439765-f5247600-1969-11eb-8633-22103708e3dd.JPG)

3. rent deploy에 대한 describe 확인<br>
![Liveness_ describe로 확인](https://user-images.githubusercontent.com/12227092/97439959-2dc44f80-196a-11eb-8d6b-799142c4521d.JPG)

### Readiness
1. rent 서비스로 seige로 부하를 주어 Availablity를 확인 (po, deploy에 설정된 autoscale 제거 후 실행함)
#siege -c255 -t120S -r10 --content-type "application/json" 'http://rent:8080/rental POST {"bookId":"5", "qty":1}'<br>
![Readiness_설정전 seige결과](https://user-images.githubusercontent.com/12227092/97448509-112d1500-1974-11eb-9217-f51688ba6dab.JPG)

약 성공률이 90%로 확인함

2. 올라간 rent 서비스에 Readiness 적용된 yaml 적용<br>
![Readiness_yaml](https://user-images.githubusercontent.com/12227092/97463350-0037d000-1983-11eb-85d8-502a69992bc0.JPG)


3. rent 서비스로 seige로 부하를 주어 Availablity를 확인
#siege -c255 -t120S -r10 --content-type "application/json" 'http://rent:8080/rental POST {"bookId":"5", "qty":1}'<br>
![Readiness_설정 후 seige결과](https://user-images.githubusercontent.com/12227092/97463074-b8b14400-1982-11eb-9682-edea8a0f895b.JPG)


### HPA 적용(Autoscale)
1. HPA 설정
#kubectl autoscale deploy rent --min=1 --max=10 --cpu-percent=15<br>
![HPA_1_실행전 상태](https://user-images.githubusercontent.com/12227092/97423776-1d54aa80-1953-11eb-82b8-03db56590378.JPG)

#kubectl get hpa rent -o yaml  로 설정 확인<br>
![HPA_0_yaml 설정](https://user-images.githubusercontent.com/12227092/97424249-cc918180-1953-11eb-9e4a-9a66c1968f3d.JPG)

2. HPA 실행<br>
시즈 사용한 부하 발생<br>
![HPA_2_siege 실행](https://user-images.githubusercontent.com/12227092/97423872-4412e100-1953-11eb-98b3-948b1f27bb84.JPG)

![HPA_3_실행 후 상태](https://user-images.githubusercontent.com/12227092/97423925-5856de00-1953-11eb-931c-e3fbf6bb5b5e.JPG)

rent의 POD가 10개까지 확장되어 생성됨을 확인

### Config Map 적용
1. Config Map 설정<br>
configMap.yaml으로 설정<br>
![configMap_yaml](https://user-images.githubusercontent.com/12227092/97428391-44ae7600-1959-11eb-8f76-9b03920a75e6.JPG)

#kubectl create -f configMap.yaml  -> configMap 생성<br>
#kubectl describe configmap superuser -> 추가한 configMap 정보 조회<br>
![configMap 설정](https://user-images.githubusercontent.com/12227092/97428626-9820c400-1959-11eb-806b-809b01044a84.JPG)

2. config Map 적용<br>
deploy.yaml에 해당 config Map 설정<br>
![ConfigMap_ deploy yaml](https://user-images.githubusercontent.com/12227092/97428733-c4d4db80-1959-11eb-942d-3848a7eb1fee.JPG)

application.yaml에 아래의 config Map 설정<br>
![ConfigMap_ application yaml](https://user-images.githubusercontent.com/12227092/97428874-f352b680-1959-11eb-8147-61d5bd1be8ee.JPG)

java단에 해당 config Map 적용<br>
![ConfigMap RentalController](https://user-images.githubusercontent.com/12227092/97437338-a75a3e80-1966-11eb-9101-1feb4ef4958d.JPG)

운영환경에서 실행확인<br>
![ConfigMap 실행 결과](https://user-images.githubusercontent.com/12227092/97437394-be009580-1966-11eb-9f8d-e5a07134a02e.JPG)


