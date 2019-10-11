package com.example.myapplication;

        import android.app.Service;
        import android.content.ComponentName;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Binder;
        import android.os.Bundle;
        import android.os.IBinder;
        import android.util.Log;

        import com.example.myapplication.DTO.BodyShopDTO;
        import com.google.gson.Gson;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.net.Socket;
        import java.util.concurrent.ArrayBlockingQueue;
        import java.util.concurrent.BlockingQueue;

public class BodyShopService extends Service {
    IBinder mBinder = new MyBinder();
    Socket socket;
    BufferedReader br;
    BlockingQueue blockingQueue = new ArrayBlockingQueue(10);
    PrintWriter out;
    BodyShopDTO bodyShopDTO;


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
                    Log.i("ChattingClient", "blocking queue send: " + msg);
                    out.println(msg);
                    out.flush();
                } catch (Exception e) {
                    Log.i("ChattingClientError", "blocking queue 문제 : " + e.toString());
                }
            }


        }
    }

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
                        Log.i("BodyShopService_KEY", msg[1]);
                        blockingQueue.add("Key#" + msg[1]);
                    } else if (msg[0].equals("asd")){

                    }

                }
            } catch (Exception e) {
                Log.i("BodyShopServiceError", "읽기 문제 : " + e.toString());
            }

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.i("service","서비스 시작");

    }

    class MyBinder extends Binder {
        BodyShopService getService(){
            return BodyShopService.this;
        }
    }

    public boolean connectServer(){
        boolean flag = false;

        return flag;
    }
    public void serverToClient(){

    }

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
            Log.i("BodyShopService" , "보낼때 프로토콜 문제 있음");
        }

    }

    public BodyShopService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

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

    @Override
    public void onDestroy() {

        super.onDestroy();
        // 서비스가 종료될 때 실행

    }

}
