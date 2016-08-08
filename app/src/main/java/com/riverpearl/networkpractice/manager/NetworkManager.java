package com.riverpearl.networkpractice.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tacademy on 2016-08-08.
 */
public class NetworkManager {

    // Network Manager는 한 개만 생성돼야 하므로 Singleton 패턴 이용한다.
    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) instance = new NetworkManager();

        return instance;
    }

    // ThreadPool을 만들고 한 번에 하나의 스레드만 접근할 수 있도록 한다.
    // NetworkManager는 Singleton 패턴 때문에 하나만 만들어지지만
    // 여기에 접근하는 스레드는 여러개가 될 수 있으므로 그렇다.
    Executor mExecutor;
    BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    private NetworkManager() {
        mExecutor = new ThreadPoolExecutor(3, 64, 3, TimeUnit.SECONDS, taskQueue);
    }

    private static final int MESSAGE_SUCCESS = 1;
    private static final int MESSAGE_FAIL = 2;

    // NetworkRequest에서 수행한 네트워크 처리 결과를 메인 스레드로 넘겨준다.
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            NetworkRequest<?> request = (NetworkRequest<?>)msg.obj;

            switch (msg.what) {
                case MESSAGE_SUCCESS :
                    request.sendSuccess();
                    break;
                case MESSAGE_FAIL :
                    request.sendFail();
                    break;
            }
        }
    };

    public void sendSuccess(NetworkRequest<?> request) {
        Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, request);
        mHandler.sendMessage(msg);
    }

    public void sendFail(NetworkRequest<?> request) {
        Message msg = mHandler.obtainMessage(MESSAGE_FAIL, request);
        mHandler.sendMessage(msg);
    }

    // 네트워크 처리 결과를 메인 스레드로 전달하기 위한 Listener
    public interface OnResultListener<T> {
        public void onSuccess(NetworkRequest<T> request, T result);
        public void onFail(NetworkRequest<T> request, int errorCode, String errorMsg);
    }

    public <T> void getNetworkData(NetworkRequest<T> request, OnResultListener<T> listener) {
        request.setOnResultListener(listener);
        mExecutor.execute(request); // 별도의 thread로 network request 처리
    }
}
