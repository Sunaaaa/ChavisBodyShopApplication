//package com.example.myapplication;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.example.myapplication.DTO.ReservationDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//
//public class MyReservationService extends Service {
//    String result = "";
//
//    class MyReservationRunnable implements Runnable{
//        private String keyword;
//        private String annotation;
//        private String param;
//
//        public MyReservationRunnable(String keyword) {
//            this.keyword = keyword;
//        }
//
//        @Override
//        public void run() {
//            String url = "http://70.12.115.72:9990/bookSearch/searchTitle?USER_KEYWORD=" + keyword;
////            String url = "http://70.12.115.72:9990/bookSearch/searchTitle?USER_KEYWORD=" + keyword;
//
//            // NetWork는 exception이 발생할 여지가 있기 때문에 tyr-catch로 예외처리를 해야 한다. (없으면 컴파일 에러 발생)
//            try{
//                // url을 URL 객체로 만들어 실제 url의 주소로 접속할 수 있다. (method 사용 가능)
//                URL urlObject = new URL(url);
//                HttpURLConnection con = (HttpURLConnection)urlObject.openConnection();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String input = "";
//                String result = "";
//                StringBuffer sb = new StringBuffer();
//
//                // input=br.readLine() : 서버가 보내주는 데이터를 다 읽음
//                while ((input=br.readLine())!= null){
//                    sb.append(input);
//                }
//                Log.i("DATA", sb.toString());
//                // 얻어온 결과 JSON 문자열을 Jackson Library를 이용해서 Java 객체 형태 (String[])로 변형
//                // 데이터를 다 읽어 들였으니 통로 (Stream)를 닫아요
//                br.close();
//
//                // Jackson Library를 이용하여 JSON을 원래 형태 (String[])로 변환
//                ObjectMapper mapper = new ObjectMapper();
//
//                ArrayList<ReservationDTO> mReservationList = mapper.readValue(input, );
//
//                // sb를 읽어서 Stringp[] 의 형태로 변형되어 resultArr로 떨어짐
//                String[] resultArr = mapper.readValue(sb.toString(), String[].class);
//
//
//            } catch (Exception e){
//                Log.i("DATA_ERROR", e.toString());
//            }
//
//
//        }
//    }
//
//
//    public MyReservationService() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//}
