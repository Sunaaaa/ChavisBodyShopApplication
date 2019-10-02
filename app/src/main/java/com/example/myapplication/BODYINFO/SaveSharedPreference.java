package com.example.myapplication.BODYINFO;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_BODY_SHOP_NO = "username";
    static final String PREF_BODY_SHOP_ID = "username";
    static final String PREF_BODY_SHOP_PW = "username";
    static final String PREF_BODY_SHOP_NAME = "username";
    static final String PREF_BODY_SHOP_ADDRESS = "username";
    static final String PREF_BODY_SHOP_LAT = "username";
    static final String PREF_BODY_SHOP_LONG = "username";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 정비소 No 정보 저장
    public static void setBodyShopNo(Context ctx, String bodyShopId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_BODY_SHOP_NO, bodyShopId);
        editor.commit();
    }

    // 저장된 정비소 No 정보 가져오기
    public static String getBodyShopNo(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_BODY_SHOP_NO, "NO");
    }

    // 정비소 아이디 정보 저장
    public static void setBodyShopId(Context ctx, String bodyShopId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_BODY_SHOP_ID, bodyShopId);
        editor.commit();
    }

    // 저장된 정비소 아이디 정보 가져오기
    public static String getBodyShopId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_BODY_SHOP_ID, "NO");
    }

    // 정비소 비밀번호 정보 저장
    public static void setBodyShopPw(Context ctx, String bodyShopId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_BODY_SHOP_PW, bodyShopId);
        editor.commit();
    }

    // 저장된 정비소 비밀번호 정보 가져오기
    public static String getBodyShopPw(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_BODY_SHOP_PW, "NO");
    }

    // 정비소 이름 정보 저장
    public static void setBodyShopName(Context ctx, String bodyShopId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_BODY_SHOP_NAME, bodyShopId);
        editor.commit();
    }

    // 저장된 정비소 이름 정보 가져오기
    public static String getBodyShopName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_BODY_SHOP_NAME, "NO");
    }


    // 정비소 주소 정보 저장
    public static void setBodyShopAddress(Context ctx, String bodyShopId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_BODY_SHOP_ADDRESS, bodyShopId);
        editor.commit();
    }

    // 저장된 정비소 주소 정보 가져오기
    public static String getBodyShopAddress(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_BODY_SHOP_ADDRESS, "NO");
    }

    // 정비소 위도 정보 저장
    public static void setBodyShopLat(Context ctx, String bodyShopId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_BODY_SHOP_LAT, bodyShopId);
        editor.commit();
    }

    // 저장된 정비소 위도 정보 가져오기
    public static String getBodyShopLat(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_BODY_SHOP_LAT, "NO");
    }

    // 정비소 경도 정보 저장
    public static void setBodyShopLong(Context ctx, String bodyShopId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_BODY_SHOP_LONG, bodyShopId);
        editor.commit();
    }

    // 저장된 정비소 경도 정보 가져오기
    public static String getBodyShopLong(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_BODY_SHOP_LONG, "NO");
    }

    // 로그아웃
    public static void clearBodyInfo(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
