package com.jyong.coolweather.util;

/**
 * Created by 加勇 on 2015/9/7.
 */
public interface HttpCallbackListener {
    void onFinish(String request);

    void onError(Exception e);
}
