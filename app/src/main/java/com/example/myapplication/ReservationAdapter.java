package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.DTO.BodyShopDTO;
import com.example.myapplication.DTO.ReservationListDTO;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends BaseAdapter {

    private ArrayList<ReservationListDTO> list = new ArrayList<>();

    public ReservationAdapter() {

    }

    public void addItem(ReservationListDTO dto){
        list.add(dto);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ReservationListDTO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        // 출력할 View 객체를 생성
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 제작한 Custom 형태의 xml의 형태로 view 객체를 구성하게 된다.
            // 생성한 View 객체에 XML Layout을 설정
            view = inflater.inflate(R.layout.reservation_list, viewGroup, false);
        }

        // 출력한 View Component Reference 획득
        TextView reservation_time = (TextView) view.findViewById(R.id.reservation_time);
        TextView member_mname = (TextView) view.findViewById(R.id.member_mname);
        TextView cartype = (TextView) view.findViewById(R.id.cartype);
        TextView carid = (TextView) view.findViewById(R.id.carid);
        ImageView key_status = (ImageView) view.findViewById(R.id.key_status);
        ImageView repair_status = (ImageView) view.findViewById(R.id.repair_status);

        // 화면에 출력할 데이터를 가져와요
        ReservationListDTO dto = list.get(i);
        Log.i("DTO", dto.toString());


        try {

            Log.i("DTO", "통과 1");

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
                key_status.setImageResource(R.drawable.ing);
            } else {
                key_status.setImageResource(R.drawable.done);
            }
            Log.i("DTO", "이쁘게 다 꾸몄네유~~~");

        } catch (Exception e) {
            Log.i("LOG_KAKAOBOOK_ERROR", e.toString());
        }

        return view;
    }
}
