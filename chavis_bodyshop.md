









# ChavisBodyShop

![1570719347499](https://user-images.githubusercontent.com/39547788/66580603-a1f25080-ebb9-11e9-8c9f-45c4ad6c96e5.png)

<br>

## 정비소 전용 Application

### Application Icon

![1571383309414](https://user-images.githubusercontent.com/39547788/67074105-6b24c780-f1c3-11e9-9405-f8ae035795da.png)

<br>

### Application 초기 화면 (OpeningActivity)



<br>

## 예약 정보 VO

### ReservationVO

- 설계한 데이터베이스와 같은 이름의 멤버를 선언한다.
  - 서버에서 전달받은 데이터를 Jackson 라이브러리로 한번에  값을 저장하기 위해서 가능한 데이터베이스의 필드명과 같은 변수명을 지정한다.
- 액티비티 전환 시, 예약 정보 객체를 전달하기 위해 해당 객체가 마샬링 가능하도록 Parcelable interface를 구현gksek.



<br>

<br>



## 회원 정보 등록 및 수정

### 정비소 등록 (RegistActivity)

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

### 아이디 찾기 (FindIDActivity)

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

- findSuccessDialog() : 사용자의 정비소 ID를 알리고, 로그인 화면으로 이동하는 Dialog

- 정비소 이름으로 등록된 정비소가 없는 경우, "NO" 를 Return 한다.

  - findFailDialog() : 아이디 찾기를 실패했음을 알리고, 재 입력 여부를 묻는 Dialog

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

<br>



### 비밀번호 찾기 화면 (FindPwActivity)

- 자동로그인 기능으로 비밀번호를 잊어버리는 경우가 많이 발생한다. 

  그래서 비밀번호 찾기 기능을 필수 이다.

- 기능 

  - 비밀번호 찾기

    - 정비소 등록 시 입력한 정비소 이름과 아이디를 이용해 비밀번호를 찾는다.

    - '비밀번호 찾기'를 누르면 비밀번호 찾기 기능을 수행하는 Thread를 실행한다.

      ```java
      FindPwRunnable findpwRunnable = new FindPwRunnable(fpwname.getText().toString(), fpwid.getText().toString() , handler);
      Thread thread = new Thread(findpwRunnable);
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
    map.put("bodyshop_id", id);
    
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

    - 정비소 이름과 아이디로 등록된 정비소가 있는 경우, 정비소의 비밀번호를 Return한다.

- findSuccessDialog() : 사용자의 정비소 비밀번호를 알리고, 로그인 화면으로 이동하는 Dialog

- 정비소 이름과 ID로 등록된 정비소가 없는 경우, "NO" 를 Return 한다.

  - findFailDialog() : 비밀번호 찾기를 실패했음을 알리고, 재 입력 여부를 묻는 Dialog

    ```java
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            if (result.equals("\"NO\"")){
                findFailDialog(result);
            } else {
                findSuccessDialog(result);
            }
        }
    };
    ```

    

<br>

<br>

## 회원 로그인

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

          - 해당 객체를 SharedPreferences에 저장한다.

            ```java
            private BodyShopDTO sendPost(String id, String pw) throws Exception {
            
                String receivedata;
                String sendMsg;
            
                URL url = new URL("http://70.12.115.73:9090/Chavis/Bodyshop/login.do");
            
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
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

        - 서비스를 수행하고 예약 내역 화면으로 이동한다. 

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

        - SharedPreferences의 모든 데이터를 지운다. 

      <br>

  - 아이디 / 비밀번호 찾기

    - 아이디 찾기

      - 정비소 등록 시 입력한 정비소 이름을 이용해 아이디를 찾는다.

    - 비밀번호 찾기

      - ID와 정비소 이름을 이용해 비밀번호를 찾는다.

      <br>

  - 정비소 등록

    - 정비소 등록이 필요한 경우, 정비소 등록 화면으로 이동한다.

  - '뒤로 가기' 2번 연속으로 터치하면 어플리케이션 종료

    - BackPressCloseHandler

<br>

<br>





## 예약 정보 확인

### 정비소 예약 현황 확인 화면 (ReservationStatusActivity)

- 해당 정비소에 등록된 예약 현황을 리스트로 확인한다.

- 기능

  - 정비소 정보 

    - SharedPreferences에 저장된 정비소 정보를 가져와 정비소 이름 Textview에 꾸며준다.

      ```java
      SharedPreferences preferences;
      preferences = getSharedPreferences("preferences", MODE_PRIVATE);
      String myObject = preferences.getString("myObject", "NO");
      
      Gson gson = new Gson();
      final BodyShopDTO bodyShopDTO = gson.fromJson(myObject, BodyShopDTO.class);
      
      shop_name.setText(bodyShopDTO.getBodyshop_name());
      
      ```

      

  - 예약 내역 정보 가져오기

    - MyReservationRunnable 을 통해 서버로부터 로그인된 정비소 앞으로 등록된 에약 내역들을 가져온다.

      ```java
      MyReservationRunnable myReservationRunnable = new MyReservationRunnable(bodyShopDTO.getBodyshop_no(), handler);
      Thread thread = new Thread(myReservationRunnable);
      thread.start();
      ```

    - #### MyReservationRunnable 

      - 해당 정비소의 no값을 서버에게 전달하면, 해당 정비소 앞으로 등록된 예약 내역 리스트를 받는다.

        - bodyshop_no 값 전달

          ```java
          OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
          
          Map<String, String> map = new HashMap<String, String>();
          
          map.put("bodyshop_no", id);
          
          ObjectMapper mapper = new ObjectMapper();
          String json = mapper.writeValueAsString(map);
          
          Log.i("MyReservationRunnable", "SERVER에게 보내는 데이터 : " + json);
          
          osw.write(json);
          osw.flush();
          ```

        - Jackson 라이브러리를 이용해 json array로 전송된 예약 내역 리스트를 ArrayList< ReservationListDTO > Type으로 변환하여 저장한다.

          ```java
          BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          
          while ((inputLine = in.readLine()) != null) {
              response.append(inputLine);
          }
          
          receivedata = response.toString();
          ArrayList<ReservationListDTO> myObject = mapper.readValue(receivedata, new TypeReference<ArrayList<ReservationListDTO>>() {
          });
          in.close();
          ```

        - 전달받은 예약 내역 리스트를 리스트뷰에 붙이기 위해 Bundle, Message, Handler를 사용한다.

          ```java
          Bundle bundle = new Bundle();
          bundle.putParcelableArrayList("reservation_list", myObject);
          
          Message message = new Message();
          message.setData(bundle);
          
          handler.sendMessage(message);
          ```

  

  - 예약내역 상세 확인

    - 커스텀 리스트뷰를 통해 예약 상세 내역을 확인할 수 있다. 

      - 예약자
      - 예약된 차량 정보
      - 원격키 사용여부
      - 수리  여부 (수리 중, 수리 완료)

    - Handler를 통해 서버로부터 전달받은 데이터를 커스텀 어댑터를 이용해 리스트뷰를 꾸민다.

      ```java
      handler = new Handler() {
          @Override
          public void handleMessage(@NonNull Message msg) {
              super.handleMessage(msg);
              Bundle bundle = msg.getData();
              ArrayList<ReservationListDTO> result = bundle.getParcelableArrayList("reservation_list");
              // 예약 내역 유무 확인 
              TextView nolist = (TextView) findViewById(R.id.nolist);
              if (result.size() == 0) {
                  nolist.setVisibility(View.VISIBLE);
              } else {
                  nolist.setVisibility(View.GONE);
      
                  adapter.removeAllList();
                  for (ReservationListDTO dto : result) {
                      adapter.addItem(dto);
                  }
      
              }
              listView.setAdapter(adapter);
              adapter.notifyDataSetChanged();
          }
      };
      ```

      

  - 예약 내역 유무에 따라 화면 구성을 달리하여, 예약 내역 유무를 사용자에게 알린다.

    - 만약 해당 정비소 앞으로 등록된 예약 내역이 없는 경우, Textview의 VISIBLE을 VISIBLE로 설정한다.

    - 만약 해당 정비소 앞으로 등록된 예약 내역이 있는 경우, Textview의 VISIBLE을 GONE으로 설정한다.

      - 해당 TextView의 공간이 없어짐

        ```java
        ArrayList<ReservationListDTO> result = bundle.getParcelableArrayList("reservation_list");
        
        TextView nolist = (TextView) findViewById(R.id.nolist);
        	// 예약 내역이 없는 경우
        if (result.size() == 0) {
            nolist.setVisibility(View.VISIBLE);
            Log.i("FIRST", "데이터 없음");
        } else {
        	// 예약 내역이 있는 경우
            nolist.setVisibility(View.GONE);
            Log.i("FIRST", "데이터 있음");
        
            adapter.removeAllList();
            for (ReservationListDTO dto : result) {
                adapter.addItem(dto);
            }
        
        ```

      - 예약 내역이 없는 경우의 실행화면

        ![1571831901187](https://user-images.githubusercontent.com/39547788/67391823-65bbe880-f5da-11e9-8159-03b6eefcce93.png)

      - 예약 내역이 있는 경우의 실행화면

        ![1571831922794](https://user-images.githubusercontent.com/39547788/67391824-65bbe880-f5da-11e9-9909-3d31fbbea0e4.png)

      <br>

    - 특정 예약 내역 클릭 시, 원격키 및 수리 관리 화면 (CarKeyActivity) 으로 넘어간다.

    - `startActivityForResult(intent, 5050)`

      - 원격키 및 수리 관련 작업이 성공적으로 마무리되면 수리 결과를 반영하기 위해 다시 해당 화면으로 돌아오도록 한다.

      - 해당 화면으로 돌아왔을 경우, 자동으로 `onActivityResult()` 함수가 콜백된다. 

        - MyReservationRunnable를 수행하여 수리 내역이 반영된 리스트를 출력한다.

          ```java
          @Override
          protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
              super.onActivityResult(requestCode, resultCode, data);
              MyReservationRunnable myReservationRunnable = new MyReservationRunnable(bodyShopDTO.getBodyshop_no(), handler);
              Thread thread = new Thread(myReservationRunnable);
              thread.start();
          }
          ```

          

  

  - 예약 내역 갱신

    - 버튼 

      ![1571830480903](https://user-images.githubusercontent.com/39547788/67391820-65235200-f5da-11e9-8ec9-0b124511d325.png)

      <br>

    - 위의 이미지 버튼을 눌러 새로운 예약 내역을 갱신할 수 있다.

      - 버튼을 누르면 서버로 부터 해당 정비소 앞으로 등록된 예약 정보를 가져오는 MyReservationRunnable가 수행된다. 

        ```java
        btn_getNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyReservationRunnable myReservationRunnable = new MyReservationRunnable(bodyShopDTO.getBodyshop_no(), handler);
                Thread thread = new Thread(myReservationRunnable);
                thread.start();
            }
        });
        ```

  - 로그아웃

    - 버튼

      ![1571830637271](https://user-images.githubusercontent.com/39547788/67391822-65bbe880-f5da-11e9-9cfa-aa98686adb9b.png)

      <br>

    - 위의 이미지 버튼을 눌러 사용자는 로그아웃을 할 수 있다. 

      - 버튼을 누르면 logOutDialog()를 호출한다.

        ```java
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutDialog();
            }
        });
        ```

      - logOutDialog() : 로그아웃 여부를 한번 더 물어본다. 

        - '네'을 누르면, SharedPreferences에 저장된 모든 내역을 삭제한다.

        - '아니요'를 누르면, Dialog 상자를 닫는다.

          ```java
          public void logOutDialog() {
              AlertDialog.Builder alert = new AlertDialog.Builder(ReservationStatusActivity.this);
              alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      SharedPreferences.Editor editor = preferences.edit();
                      editor.clear();
                      editor.commit();
                      Intent intent = new Intent();
                      ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.OpeningActivity");
                      intent.setComponent(componentName);
                      startActivity(intent);
                      dialog.dismiss();     //닫기
                  }
              });
              alert.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();     //닫기
                  }
              });
              alert.setMessage("로그아웃 하시겠습니까?");
              alert.show();
          }
          ```

  - '뒤로 가기'로 어플리케이션 종료하기

    ```java
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
    
    ```

    

<br>

<br>

### 예약 리스트 어댑터 (ReservationAdapter)

- 예약 리스트는 원격키 사용여부, 수리 여부 등 다양한 정보가 포함되어있다. 

  그래서 각 상황에 맞는 이미지를 보여, 한눈에 알기 쉽도록 한다. 

  (글이 아닌 이미지로 한눈에 파악할 수 있다. )

- 예약 리스트에 나타낼 정보

  - 예약 날짜 및 시간 (YYYY-MM-dd HH-mm)

  - 예약자 이름

  - 등록된 차의 종류

  - 등록된 차 번호

  - 원격키 사용 여부에 따른 이미지 구분

    - 원격키 사용 

      ![1571828481539](https://user-images.githubusercontent.com/39547788/67391813-648abb80-f5da-11e9-9c93-9145177af6f1.png)

    - 원격키 사용 안함

      ![1571828523864](https://user-images.githubusercontent.com/39547788/67391814-648abb80-f5da-11e9-8a57-c8c990ad8158.png)

  - 수리 여부에 따른 이미지 구분

    - 아직 수리가 완료되지 않은 예약

      ![1571828638865](https://user-images.githubusercontent.com/39547788/67391817-65235200-f5da-11e9-9fde-1f543d8ac58a.png)

      <br>

    - 수리가 완료된 예약 

      ![1571828632831](https://user-images.githubusercontent.com/39547788/67391816-648abb80-f5da-11e9-9a40-f4d6c9597d43.png)

      <br>

- 주요 메서드 

  - removeAllList()

    - 리스트뷰에 추가된 모든 list item을 삭제한다.

      ```java
      public void removeAllList(){
          list.clear();
      }
      ```

  - addItem()

    - 리스트뷰에 보이게 할 list item을 추가한다. 

      ```java
      public void addItem(ReservationListDTO dto){
          list.add(dto);
      }
      ```

  - getView()

    - 커스텀 list item layout에 예약 관련 정보를 채운다.

      ```java
      public View getView(int i, View view, ViewGroup viewGroup) {
      
          final Context context = viewGroup.getContext();
          final RecyclerView.ViewHolder viewHolder;
      
          // 출력할 View 객체를 생성
          if (view == null) {
              LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      
              // 제작한 Custom 형태의 xml의 형태로 view 객체를 구성하게 된다.
              // 생성한 View 객체에 XML Layout을 설정
              view = inflater.inflate(R.layout.reservation_list, viewGroup, false);
          }
      
          // 출력할 View Component Reference 획득
          TextView reservation_time = (TextView) view.findViewById(R.id.reservation_time);
          TextView member_mname = (TextView) view.findViewById(R.id.member_mname);
          TextView cartype = (TextView) view.findViewById(R.id.cartype);
          TextView carid = (TextView) view.findViewById(R.id.carid);
          ImageView key_status = (ImageView) view.findViewById(R.id.key_status);
          ImageView repair_status = (ImageView) view.findViewById(R.id.repair_status);
      
          // 화면에 출력할 데이터를 가져온다.
          ReservationListDTO dto = list.get(i);
      
          try {
      
              reservation_time.setText(dto.getReservation_time());
              member_mname.setText(dto.getMember_mname());
              cartype.setText(dto.getCar_type());
              carid.setText(" ( " + dto.getCar_id() + " ) ");
              String key_s = dto.getKey();
              if (key_s.equals("NO")){
                  key_status.setImageResource(R.drawable.nokey);
              } else {
                  key_status.setImageResource(R.drawable.key);
              }
              String repair_s = dto.getRepaired_time();
              if (repair_s.equals("NO")){
                  repair_status.setImageResource(R.drawable.ing);
              } else {
                  repair_status.setImageResource(R.drawable.done);
              }
      
          } catch (Exception e) {
              Log.i("Adapter ERROR_", e.toString());
          }
      
          return view;
      }
      ```

    - reservation_list

      - 커스텀 list item 구성 화면 예시

      ![1571829818311](https://user-images.githubusercontent.com/39547788/67391819-65235200-f5da-11e9-87fe-b362e28e704c.png)

      <br>



<br>

<br>







<br>

<br>



## 수리 정보 확인

### 차량 원격키 제어 화면 (CarKeyActivity)

- 원격키 사용여부에 따라 원격키 버튼이 활성화 / 비활성화된다.

- 기능

  - 원격키 사용

    - 원격키 허용한 경우
      - 원격키 버튼 활성화
        - 차량 도어 제어 가능
      - 수리 정보 관리 및 등록
        - 수리한 정비사 명
        - 수리 내역
      - 수리 완료 날짜
      - 실행화면

  - 원격키 허용하지 않은 경우

    - 원격키 버튼 비활성화
    - 차량 도어 제어 불가능
    - 수리한 정비사 이름을 화면에 보인다.
    - 실행화면

    <br>

  - 수리 여부

    - 수리 중 -> 수리 진행

      - 수리 완료 버튼을 누르면 수리 목록을 체크하는 대화 상자를 보인다.

        ```java
        AlertDialog.Builder dialog = new AlertDialog.Builder(CarKeyActivity.this);
        dialog.setTitle("수리 목록"). 
            // Dialog 내용 채우는 코드
            .create().show();
        
        ```

        

      - 수리 목록 ( 타이어, 와이퍼, 냉각수, 엔진오일 ) 중 수리한 내역을 선택한다.

        - 선택한 수리 목록을 selectedItems list에 담아 보관한다.

          ```java
          .setMultiChoiceItems(
              items,
              new boolean[]{false, false, false, false},
              new DialogInterface.OnMultiChoiceClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                      if (isChecked) {
                          selectedItems.add(items[which]);
                      } else {
                          selectedItems.remove(items[which]);
                      }
                  }
              })
          ```

          

      - 확인 버튼을 누르면 수리한 정비사의 이름과 수리 완료 날짜 ( 현재 시간 )을 서버에 전송하기 위해 Service를 바인딩 한다.

        - 서버에게 전송할 데이터 다듬기

          - repair_list : 수리 목록 사이에 "#"를 넣고, 마지막 "#"를 제거하기 위해 substring()을 수행한다.

          -  repaired_info : '예약 번호 / 수리 완료 시간 / 수리한 정비사 이름' 의 형태로 저장한다.

            ```java
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            if (selectedItems.size() == 0) {
                // 반드시 수리 목록은 1개 이상 선택되어야 한다.
                // 선택된 수리 목록이 없으면 선택하라는 대화상자를 보인다.
                makeDialog();
            } else {
                for (String ckitem : selectedItems) {
            
                    switch (ckitem) {
                        case "타이어":
                            repair_list += ("Tire" + "#");
                            break;
                        case "와이퍼":
                            repair_list += ("Wiper" + "#");
                            break;
                        case "냉각수":
                            repair_list += ("Cooler" + "#");
                            break;
                        case "엔진오일":
                            repair_list += ("EngineOil" + "#");
                            break;
                    }
                }
            
                selectedItems.clear();
                repair_list = repair_list.substring(0, repair_list.length() - 1);
                String repaired_time = sdf.format(date);
                repaired_info = reservationListDTO.getReservation_no() + "/" + repaired_time + "/" + editText.getText().toString();
                bodyShopService.clientToServer("RepairFinish", repair_list + "/" + repaired_info);
            }
            ```

        - Service 바인딩

          - Service의 clinetToServer () 를 사용하기 위해 Service를 바인딩한다.

            - Service 바인딩

              ```java
              BodyShopService bodyShopService;
              Boolean isService = false;
              
              @Override
              protected void onCreate(Bundle savedInstanceState) {
                  ServiceConnection conn = new ServiceConnection() {
                      @Override
                      public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                          BodyShopService.MyBinder mb = (BodyShopService.MyBinder) iBinder;
                          bodyShopService = mb.getService();
                          isService = true;
                      }
              
                      @Override
                      public void onServiceDisconnected(ComponentName componentName) {
                          isService = false;
                      }
                  };
                  Intent intent2 = new Intent(CarKeyActivity.this, BodyShopService.class);
                  bindService(intent2, conn, Context.BIND_ABOVE_CLIENT);
              
              }
              ```

            - clientToServer() 호출

              ```
              bodyShopService.clientToServer("RepairFinish", repair_list + "/" + repaired_info);
              ```

          - onNewIntent() 를 콜백과 동시에 수리 완료가 정상적으로 등록 되었는지 확인한다.

            - repairedResult 가 "Success" 인 경우, 예약 정보 확인 화면으로 이동한다.
            - repairedResult 가 "Fail" 인 경우, 정비목록 및 정비사명이 제대로 입력됬는지 확인하라는 대화 상자를 보인다.

        

    - 수리 완료

      - 원격키 버튼은 disable하도록 설정한다.

      - 수리한 정비사 이름을 화면에 보인다.

        ```java
        if (!reservationListDTO.getRepaired_person().equals("NO")) {
            editText.setText(reservationListDTO.getRepaired_person() + " 이/가 정비를 완료했습니다. ");
            editText.setEnabled(false);
            btn_getKey.setEnabled(false);
            btn_getKey.setBackgroundResource(R.drawable.disablekey);
            btn_repair_finish.setVisibility(View.GONE);
            pname.setVisibility(View.GONE);
        } 
        ```

        

  - '뒤로 가기'를 비활성화

    - 예약 목록보기 버튼을 누르면 예약 정보 확인하는 화면으로 이동한다.

      ```java
      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i = new Intent();
              ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.ReservationStatusActivity");
              i.setComponent(componentName);
              startActivity(i);
              finish();
      
          }
      });
      ```

      

<br>

<br>



## TCP 통신 Service

- 서버와 TCP 통신을 통해 서버에 데이터를 보내고 받는다.

- 프로토콜 정리

  - 원격키 사용

    **[ 서버에게 보내기]**

    - **CarOpen**#[예약한 회원번호 (getMember_no()) ]#[예약된 차량번호 (getCar_id()) ]

      - 현재 정비소에서 차 도어를 오픈할 차량에 대한 정보
        - 차키 이미지를 선택하면 서버에게 예약한 회원정보와 예약된 차량 번호를 전달한다.

    - **Key**#[원격키 값]

      - 서버에서 보낸 원격키를 받았다는 의미로 받은 즉시 바로 서버에게 돌여준다.

      <br>

    **[서버에게 받기]**

    - **Key**#[원격키 값]
      - 서버에서 원격키 검증을 위해 정비소에게 원격키 값을 전달한다.

  <br>

  - 수리완료 목록

    **[서버에게 보내기]**

    - **RepairFinish**/[수리목록]/[예약번호]/[수리완료시간]/[수리한 정비사명]

      - 수리목록

        - 수리한 내역을 #로 구분

          예 > 타이어#와이퍼

        

        <br>

    **[서버에게 받기]**

    - **RepairFinishResult**#[수리결과]
      - 수리 결과 (성공 또는 실패 여부)에 대한 정보
        - 수리 결과 ("Success" 또는 "Fail") 값을 CarKeyActivity에게 돌려준다.

- 서버 연결

  - Service가 Strat되어 호출되는 순간, 서버와 통신하는 Thread를 수행한다.

    ```java
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
    
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String myObject = preferences.getString("myObject", "NO");
    
        Gson gson = new Gson();
        bodyShopDTO = gson.fromJson(myObject, BodyShopDTO.class);
    
        ClientReceiveRunnable receiveRunnable = new ClientReceiveRunnable();
        ClientSendRunnable sendRunnable = new ClientSendRunnable(blockingQueue);
        Thread thread1 = new Thread(receiveRunnable);
        Thread thread2 = new Thread(sendRunnable);
        thread1.start();
        thread2.start();
    
        return super.onStartCommand(intent, flags, startId);
    }
    ```

    <br>

    - ClientReceiveRunnable

      - 서버에서 보내는 데이터를 받는다.

        - 로그인 후, 서비스를 시작하는 순간 서버에게 로그인된 정비소의 Bodyshop_no를 전달한다. 

          ```java
          class ClientReceiveRunnable implements Runnable{
          
              Intent receiveIntent = new Intent();
          
              @Override
              public void run() {
                  try {
                      try {
                          socket = new Socket("70.12.115.63", 6767);
                          Log.i("BodyShopService", "서버 연결 성공!!");
                          br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                          out = new PrintWriter(socket.getOutputStream());
                      } catch (Exception e) {
                          Log.i("BodyShopServiceError", "서버 연결 실패 : " + e.toString());
                      }
          
                      out.println("BodyShopNO#" + bodyShopDTO.getBodyshop_no());
                      out.flush();
                      String s = "";
          
                      while ((s = br.readLine()) != null) {
                          Log.i("BodyShopService", "서버로 받는 데이터 : " + s);
                          String msg[] = s.split("#");
                          if (msg[0].equals("Key")) {
                              blockingQueue.add("Key#" + msg[1]);
                          } else if (msg[0].equals("RepairFinishResult")){
                              receiveIntent = new Intent();
                              ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.CarKeyActivity");
                              receiveIntent.putExtra("repairedResult", msg[1]);
                              receiveIntent.setComponent(componentName);
                              receiveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                              receiveIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                              receiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              startActivity(receiveIntent);
                          }
          
                      }
                  } catch (Exception e) {
                      Log.i("BodyShopServiceError", "읽기 문제 : " + e.toString());
                  }
          
              }
          }
          ```

        

    - ClientSendRunnable

      - 서버에게 데이터를 보낸다.

        ```java
        class ClientSendRunnable implements Runnable {
            BlockingQueue blockingQueue;
        
            ClientSendRunnable(BlockingQueue blockingQueue) {
                this.blockingQueue = blockingQueue;
            }
        
            @Override
            public void run() {
                while (true) {
                    try {
                        String msg = (String) blockingQueue.take();
                        Log.i("BodyShopService", "blocking queue send: " + msg);
                        out.println(msg);
                        out.flush();
                    } catch (Exception e) {
                        Log.i("BodyShopServiceError", "blocking queue 문제 : " + e.toString());
                    }
                }
            }
        }
        ```

        

- Activity에서 Serviece 객체 사용하기

  - Bind

    - 프로세스랑 프로세스를 연결하여 통신가능하도록 만들어 놓은 객체

    - Bind 객체를 가지고 Service에 접근할 수 있다. 

    - Activity에서는 Bind 객체를 이용해 Service의 public으로 선언된 함수들을 호출할 수 있다. 

      - MyBinder 클래스

        - Service 객체를 제공한다.

          ```java
          IBinder mBinder = new MyBinder();
          
          class MyBinder extends Binder {
              BodyShopService getService(){
                  return BodyShopService.this;
              }
          }
          
          @Override
          public IBinder onBind(Intent intent) {
              return mBinder;
          }
          ```

        <br>

  - Activity에서 사용할 함수 정의

    - clientToServer()

      - 클라이언트 (안드로이드 어플)가 서버 (Java TCP 서버)에게 데이터 전송한다.

        ```java
        public void clientToServer(String protocol , String msg){
        
            if(protocol.equals("CarOpen")){
                Log.i("BodyShopService" , protocol);
                Log.i("BodyShopService" , msg);
                blockingQueue.add(protocol + "#" + msg);
            } else if (protocol.equals("RepairFinish")){
                Log.i("BodyShopService" , protocol);
                Log.i("BodyShopService" , msg);
                blockingQueue.add(protocol + "#" + msg);
            } else {
                Log.i("BodyShopService" , "프로토콜 문제 발생");
            }
        
        }
        ```

      

<br>

<br>





### Blocking Queue
