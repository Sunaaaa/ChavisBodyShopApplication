# ChavisBodyShop

![1570719347499](https://user-images.githubusercontent.com/39547788/66580603-a1f25080-ebb9-11e9-8c9f-45c4ad6c96e5.png)

<br>

## 정비소 전용 Application

### Application Icon

![1571383309414](https://user-images.githubusercontent.com/39547788/67074105-6b24c780-f1c3-11e9-9405-f8ae035795da.png)

<br>

### Application 초기 화면 (OpeningActivity)



### 정비소 등록 화면(RegistActivity)

- 입력한 데이터를 이용해 정비소를 등록하여, 정비소 어플리케이션을 사용할 수 있도록 한다.

- 필요한 데이터

  - 정비소 이름
  - 사용할 비밀번호
  - 주소 (도로명 주소)

- EditText가 아닌 배경 layout을 터치하면 키보드 숨기기가 가능하다.

  ```java
  final InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
          LinearLayout linearLayout = (LinearLayout)findViewById(R.id.reglayout);
  
  linearLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          imm.hideSoftInputFromWindow(res_name_ET.getWindowToken(), 0);
          imm.hideSoftInputFromWindow(reg_pw_ET.getWindowToken(),0);
          imm.hideSoftInputFromWindow(reg_pwCk_ET.getWindowToken(), 0);
          imm.hideSoftInputFromWindow(detailAddress_ET.getWindowToken(),0);
      }
  });
  ```

  

<br>

#### 등록 조건

1. 정비소 이름을 반드시 입력해야 한다. 
2. 비밀번호와 비밀번호 확인이 동일해야 한다. 
3. 2개의 spinner와 상세 주소 정보를 반드시 입력해야 한다.

- '정비소 등록'을 눌렀을 때 위의 조건이 충족되지 않았을 경우, 알림 Dialog를 띄어 사용자에게 알린다. 

  ```java
  // 가입 정보 불충족
  public void fillDataDialog(String a) {
      AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
      alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();     //닫기
          }
      });
      alert.setMessage(a);
      alert.show();
  }
  ```

  

<br>

#### 정비소 이름 작성 방법

- ㅇㅇ 정비소 

  <br>

#### 정비소 ID 설정 방식

- 정비소 이름과 주소로 정비소 별 고유 ID를 부여하여 ID 중복을 막는다. (서버 단에서 수행)

  - 예 > 서울 특별시 강남에 위치한 '행복정비소'의 ID : 2sg행복

    - 서울 : s
    - 강남 : g
    - 행복 정비소 : 행복
    - DB에 등록 시, 부여될 bodyshop_no 값으로 중복을 방지한다.

    

    <br>

#### 비밀번호 동일 여부 체크

- TextWatcher를 이용하여 비밀번호 동일 여부를 체크한다.
  - 비밀번호 란과 비밀번호 확인 란에 데이터를 채운다. 
    - 만약 비밀번호 확인 란의 정보가 비밀번호와 같은 경우, EditText가 기존의 색으로 변경된다. 
    - 만약 비밀번호 확인 란의 정보가 비밀번호와 다른 경우, EditText가 붉은 색으로 변경된다.  

<br>

#### 주소 선택 방법

- 정비소가 위치한 주소를 작성한다.

  1. 대분류로 특별시 / 도 를 선택한다.
  2. 소분류로 구 를 선택한다.
  3. 상세주소를 작성한다.

- Spinner를 구현하여 대분류별 소분류 아이템을 선택할 수 있다. 

  - 예1 > 첫번째 Spinner에서 '서울' 선택 시 강남구, 중락구 등 서울 지역만 보여준다.

  - 예2 > 첫번째 Spinner에서 '경기' 선택 시 수원시 권선구, 수원시 팔달구 등 수원 지역만 보여준다.

  - res 폴더 > values 폴더 > LocCategory.xml 파일

    - 도 관련 (전 지역)

      ```xml
      <resources>
          <string-array name = "spinner_do">
              <item>서울</item>
              <item>경기</item>
              <item>인천</item>
              <item>강원</item>
              <item>대전</item>
              <item>세종</item>
              <item>충남</item>
              <item>충북</item>
              <item>부산</item>
              <item>울산</item>
              <item>경남</item>
              <item>경북</item>
              <item>대구</item>
              <item>광주</item>
              <item>전남</item>
              <item>전북</item>
              <item>제주</item>
          </string-array>
      </resources>
      ```

    - 구  관련 (서울)

      ```xml
      <string-array name = "spinner_do_seoul">
          <item>강남구</item>
          <item>강동구</item>
          
          .
          .
          .
          
          <item>종로구</item>
          <item>중구</item>
          <item>중랑구</item>
      </string-array>
      ```

      

<br>

#### 정비소 등록 (HTTP 통신)

- sendPost() : 성공 여부를 저장하는 String 변수를 return하는 함수를 통해 서버와 HTTP 통신을 수행한다.

  - 정비소 이름, 비밀번호, 주소를 인자로 받는다.

- Thread를 생성해 sendPost 함수를 호출함으로써 HTTP 통신을 수행한다. ( try-catch문 필수 )

  ```java
  try {
      Thread wThread = new Thread() {
          public void run() {
              try {
                  result = sendPost(name, pw, address);
              } catch (Exception e) {
                  Log.i("RegistAcitivty_HERE", e.toString());
              }
          }
      };
      wThread.start();
  
      try {
          wThread.join();
      } catch (Exception e) {
          Log.i("RegistAcitivty_Thread",  e.toString());
      }
  } catch (Exception e) {
      Log.i("RegistAcitivty_Thread",  e.toString());
  }
  ```

  



<br>

##### 정비소 등록 

- '정비소 등록'을 눌렀을 때 정비소 등록에 필요한 조건이 충족되었을 경우, 서버에게 사용자가 제공한 데이터를 전송한다.

  - url : http://70.12.115.73:9090/Chavis/Bodyshop/regist.do

  - Method : POST

  - Proerty

    - Content-type : application/json
    - Connection : Keep-Alive
    - charset : utf-8

  - 전송 데이터 

    - 서버에 전송할 데이터는 Map <String, String> 타입의 변수에 Key - Value 값으로 저장한다. 
    - Jackson 라이브러리를 활용해 Map을 Json으로 변환한다. 
    - 변환한 데이터는 OutputStreamWriter를 통해 서버에게 전송한다.

    ```java
    URL url = new URL("http://70.12.115.73:9090/Chavis/Bodyshop/regist.do");
    
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Connection", "Keep-Alive");
    conn.setRequestProperty("charset", "utf-8");
    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
    
    Map<String, String> map = new HashMap<String, String>();
    
    map.put("name", name);
    map.put("pw", pw);
    map.put("address", address);
    
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(map);
    
    osw.write(json);
    osw.flush();
    ```

    <br>

##### 정비소 등록 결과 

- 등록할 데이터를 전송한 뒤, 서버로 부터 정비소 등록 결과 ( 성공 ("SUCCESS") 혹은 실패 ("FAIL") ) 값을 받는다.

  - BufferedReader를 통해 읽어온 값을 response라는 String 변수에 담아 return 한다. 

    ```java
    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    receiveData = response.toString();
    in.close();
    return receiveData;
    ```

  - 정비소 등록 여부에 맞는 Dialog를 띄운다.

    ```java
    // 정비소 등록 성공 여부
    if (result.equals("SUCCESS")) {
    	// 정비소 등록 성공
        makeDialog();
    } else {
    	// 정비소 등록 실패
        registSuccessDialog(result);
    }
    ```

    



<br>

- 등록이 완료되면,  Dialog를 띄우고 로그인 화면으로 이동한다.

  - 정비사 ID는 서버에서 생성하기 때문에, 반드시 사용자에게 알려야 한다.
    - Dialog Message를 통해 사용자가 알도록 한다.

  ```java
  // 회원 가입 성공
  public void registSuccessDialog(String result) {
      AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
      alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              Intent intent = new Intent();
              ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.LoginActivity");
              intent.setComponent(componentName);
              startActivity(intent);
              dialog.dismiss();     //닫기
          }
      });
      alert.setMessage("아이디 " + result + " 로 회원가입이 완료되었습니다." + "\n" + " 로그인 페이지로 이동합니다.");
  
      alert.show();
  }
  ```

  

- 등록에 실패하면,  Dialog를 띄어 로그인 화면으로 이동 또는 Dialog를 종료한다.

  ```java
  // 회원 가입 실패
  public void makeDialog() {
      AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
      alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();     //닫기
          }
      });
      alert.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              Intent intent = new Intent();
              ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.LoginActivity");
              intent.setComponent(componentName);
              startActivity(intent);
              dialog.dismiss();     //닫기
          }
      });
      alert.setMessage("회원가입에 실패했습니다. " + "\n" +  "다시 회원가입 하시겠습니까?");
      alert.show();
  }
  ```

  

<br>

<br>

### 정비소 로그인 (LoginActivity)

- 서버에 의해 부여된 ID와 정비소 등록 시 설정한 비밀번호로 로그인 한다.

- 기능

  - 로그인

    - '로그인 버튼'을 누르면, Thread를 생성하여 sendPost() 함수를 호출한다.

      ```java
      try {
        Thread wThread = new Thread() {
              public void run() {
                try {
                      dto = sendPost(userId.getText().toString(), userpw.getText().toString());
                } catch (Exception e) {
                      Log.i("LoginAcitivty_HERE", e.toString());
                }
              }
        };
          wThread.start();
      
          try {
            wThread.join();
          } catch (Exception e) {
            Log.i("LoginAcitivty_ERROR", e.toString());
          }
      } catch (Exception e) {
          Log.i("LoginAcitivty_ERROR", e.toString());
      }
      ```

    - sendPost() : 서버와의 HTTP 통신을 통해 로그인 정보를 전달하고, 로그인 결과를 받는다.

      - 로그인 정보 데이터 보내기 

        - JSON 형식
        - id : 사용자가 입력한 id
        - pw : 사용자가 입력한 pw

      - 로그인 결과 

        - BodyShopDTO 객체를 받는다.

          ```java
          private BodyShopDTO sendPost(String id, String pw) throws Exception {
          
              Log.i("sendPost", "sendPost 들어왔다 1" );
          
              String receivedata;
              String sendMsg;
          
              URL url = new URL("http://70.12.115.73:9090/Chavis/Bodyshop/login.do");
          
              HttpURLConnection conn = (HttpURLConnection) url.openConnection();
              conn.setRequestMethod("POST");
              //        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
              conn.setRequestProperty("Content-Type", "application/json");
              conn.setRequestProperty("Connection", "Keep-Alive");
              conn.setRequestProperty("charset", "utf-8");
              OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
          
              Map<String, String> map = new HashMap<String, String>();
          
              map.put("id", id);
              map.put("pw", pw);
          
              ObjectMapper mapper = new ObjectMapper();
              String json = mapper.writeValueAsString(map);
          
              osw.write(json);
              osw.flush();
          
              int responseCode = conn.getResponseCode();
              BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
              String inputLine;
              StringBuffer response = new StringBuffer();
              while ((inputLine = in.readLine()) != null) {
                  response.append(inputLine);
              }
              receivedata = response.toString();
              in.close();
              BodyShopDTO myObject = mapper.readValue(receivedata, new TypeReference<BodyShopDTO>() {
              });
          
              SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
              SharedPreferences.Editor editor = preferences.edit();
              Gson gson = new Gson();
              String bodyshopjson = gson.toJson(myObject);
              editor.putString("myObject", bodyshopjson);
              editor.commit();
          
          
              return myObject;
          }
          ```

          <br>

        - 만약 Bodyshop_id가 "NO" 가 아닌 경우, 로그인 성공을 뜻한다.

          만약 Bodyshop_id가 "NO" 인 경우, 로그인 실패를 뜻한다.

          ```java
          if (dto.getBodyshop_id().equals("NO")) {
              SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
              SharedPreferences.Editor editor = preferences.edit();
              editor.clear();
              editor.commit();
              makeDialog();
          } else {
          
              // 서비스 실행
              Intent i = new Intent();
              ComponentName sComponentName = new ComponentName("com.example.myapplication", "com.example.myapplication.BodyShopService");
              i.setComponent(sComponentName);
              startService(i);
          
              Intent intent = new Intent();
              ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.ReservationStatusActivity");
              intent.setComponent(componentName);
              intent.putExtra("data", dto);
              startActivity(intent);
              Log.i("LOGIN", dto.getBodyshop_id());
              Log.i("msi", "로그인 성공!!");
          }
          ```

      <br>

      - ID와 비밀번호가 맞는 경우, 로그인에 성공한다.

        - 자동 로그인 기능 추가

          - 어플리케이션이 삭제되거나, 로그아웃을 하면 로그인 정보 내역이 삭제된다.

            ```java
            if (dto.getBodyshop_id().equals("NO")) {
                SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                // 로그인 실패 다이얼로드 띄우기
                makeDialog();
            } else {
            
                // 로그인 성공 ! 서비스 실행 & 예약 현황 화면으로 이동
                Intent i = new Intent();
                ComponentName sComponentName = new ComponentName("com.example.myapplication", "com.example.myapplication.BodyShopService");
                i.setComponent(sComponentName);
                startService(i);
            
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.ReservationStatusActivity");
                intent.setComponent(componentName);
                intent.putExtra("data", dto);
                startActivity(intent);
            }
            ```

            

      - ID와 비밀번호가 틀린 경우, 로그인에 실패한다.

      <br>

  - 아이디 / 비밀번호 찾기

    - 아이디 찾기

      - 정비소 등록 시 입력한 정비소 이름을 이용해 아이디를 찾는다.

    - 비밀번호 찾기

      - ID와 정비소 이름을 이용해 비밀번호를 찾는다.

      <br>

  - 정비소 등록

    - 정비소 등록이 필요한 경우, 정비소 등록 화면으로 이동한다.

<br>

<br>



### 아이디 찾기 화면 (FindIDActivity)

- 정비소의 ID는 서버에서 생성하기 때문에, 사용자에게  익숙하지 않다. 

  그래서 아이디 찾기 기능이 필수이다.

- 기능 

  - 아이디 찾기

    - 정비소 등록 시 입력한 정비소 이름을 이용해 아이디를 찾는다.

    - '아이디 찾기'를 누르면 아이디 찾기 기능을 수행하는 Thread를 실행한다.

      ```java
      FindIdRunnable findIdRunnable = new FindIdRunnable(fidname.getText().toString(), handler);
      Thread thread = new Thread(findIdRunnable);
      thread.start();
      ```

      

#### 아이디 찾기 Runnable 

- 정비소 이름과 Handler를 멤버로 갖는 Runnable 클래스를 선언한다.

  ```java
  private String name;
  private Handler handler;
  
  public FindIdRunnable(String name, Handler handler) {
      this.name = name;
      this.handler = handler;
  }
  ```

  <br>

  - 서버에서 아이디 또는 비밀번호 찾기를 구별하기 위해 

    아이디를 찾는 경우에는 bodyshop_id="NO" 를 설정하고

    비밀번호를 찾는 경우에는 bodyshop_id=[사용자가 작성한 ID]로 설정한다.

    ```java
    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
    
    Map<String, String> map = new HashMap<String, String>();
    
    map.put("bodyshop_name", name);
    map.put("bodyshop_id", "NO");
    
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(map);
    
    osw.write(json);
    osw.flush();
    ```

  - 서버로부터 받은 결과 데이터를 Bundle과 Message에 담아 Handler를 통해 Dialog를 생성하는 함수를 호출한다.

    ```java
    Bundle bundle = new Bundle();
    bundle.putString("result", result);
    Message message = new Message();
    message.setData(bundle);
    handler.sendMessage(message);
    ```

    <br>

    - 정비소 이름으로 등록된 정비소가 있는 경우, 정비소의 ID 를 Return한다.
      -  findSuccessDialog() : 사용자의 정비소 ID를 알리고, 로그인 화면으로 이동하는 Dialog

    - 정비소 이름으로 등록된 정비소가 없는 경우, "NO" 를 Return 한다.

      -  findFailDialog() : 아이디 찾기를 실패했음을 알리고, 재 입력 여부를 묻는 Dialog

      ```java
      final Handler handler = new Handler(){
          @Override
          public void handleMessage(@NonNull Message msg) {
              super.handleMessage(msg);
              Bundle bundle = msg.getData();
              String result = bundle.getString("result");
              Log.i("RESULT__", result);
              if (!result.equals("\"NO\"")){
                  findSuccessDialog(result);
              } else {
                  findFailDialog(result);
              }
          }
      };
      ```



# 수정하기

### 비밀번호 찾기 화면 (FindPwActivity)

- 

- 기능 

  - 아이디 찾기

    - 정비소 등록 시 입력한 정비소 이름을 이용해 아이디를 찾는다.

    - '아이디 찾기'를 누르면 아이디 찾기 기능을 수행하는 Thread를 실행한다.

      ```java
      FindIdRunnable findIdRunnable = new FindIdRunnable(fidname.getText().toString(), handler);
      Thread thread = new Thread(findIdRunnable);
      thread.start();
      ```

      

#### 비밀번호 찾기 Runnable 

- 정비소 이름과 Handler를 멤버로 갖는 Runnable 클래스를 선언한다.

  ```java
  private String name;
  private Handler handler;
  
  public FindIdRunnable(String name, Handler handler) {
      this.name = name;
      this.handler = handler;
  }
  ```

  <br>

  - 서버에서 아이디 또는 비밀번호 찾기를 구별하기 위해 

    아이디를 찾는 경우에는 bodyshop_id="NO" 를 설정하고

    비밀번호를 찾는 경우에는 bodyshop_id=[사용자가 작성한 ID]로 설정한다.

    ```java
    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
    
    Map<String, String> map = new HashMap<String, String>();
    
    map.put("bodyshop_name", name);
    map.put("bodyshop_id", "NO");
    
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(map);
    
    osw.write(json);
    osw.flush();
    ```

  - 서버로부터 받은 결과 데이터를 Bundle과 Message에 담아 Handler를 통해 Dialog를 생성하는 함수를 호출한다.

    ```java
    Bundle bundle = new Bundle();
    bundle.putString("result", result);
    Message message = new Message();
    message.setData(bundle);
    handler.sendMessage(message);
    ```

    <br>

    - 정비소 이름으로 등록된 정비소가 있는 경우, 정비소의 ID 를 Return한다.
      -  findSuccessDialog() : 사용자의 정비소 ID를 알리고, 로그인 화면으로 이동하는 Dialog

    - 정비소 이름으로 등록된 정비소가 없는 경우, "NO" 를 Return 한다.

      -  findFailDialog() : 아이디 찾기를 실패했음을 알리고, 재 입력 여부를 묻는 Dialog

      ```java
      final Handler handler = new Handler(){
          @Override
          public void handleMessage(@NonNull Message msg) {
              super.handleMessage(msg);
              Bundle bundle = msg.getData();
              String result = bundle.getString("result");
              Log.i("RESULT__", result);
              if (!result.equals("\"NO\"")){
                  findSuccessDialog(result);
              } else {
                  findFailDialog(result);
              }
          }
      };
      ```

    



### 

### 정비소 예약 현황 확인 화면

- 해당 정비소에 등록된 예약 현황을 리스트로 확인한다.

- 기능

  - 예약내역 상세 확인 

    - 예약자
    - 예약된 차량 정보
    - 원격키 사용여부
    - 수리  여부 (수리 중, 수리 완료)

    <br>

  - 예약 내역 클릭 시, 차량 문 제어 화면 (CarKeyActivity) 으로 넘어간다.

<br>

<br>

### 차량 원격키 제어 화면

- 원격키 사용여부에 따라 원격키 버튼이 활성화 / 비활성화된다.

- 기능

  - 원격키 사용

    - 원격키 허용한 경우
      - 원격키 버튼 활성화
        - 차량 도어 제어 가능
    - 원격키 허용하지 않은 경우
      - 원격키 버튼 비활성화
        - 차량 도어 제어 불가능

    <br>

  - 수리 완료

    - 수리 목록 ( 타이어, 와이퍼, 냉각수, 엔진오일 ) 중 수리한 내역을 선택하여 수리한 정비사의 이름과 수리 완료 날짜 ( 현재 시간 )을 서버에 전송한다.

<br>

<br>
