package com.riverpearl.networkpractice.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Tacademy on 2016-08-08.
 */
public abstract class NetworkRequest<T> implements Runnable {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    // 데이터를 얻어오기 위한 URL을 정의하는 메소드
    public abstract URL getURL() throws MalformedURLException;
    // 얻어온 데이터를 처리하는 메소드
    protected abstract T parse(InputStream is) throws IOException;
    // 위 두 개는 abstract로 처리하여 하위 클래스에서 기능을 오버라이드 해준다.

    NetworkManager.OnResultListener<T> listener;

    public void setOnResultListener(NetworkManager.OnResultListener<T> listener) {
        this.listener = listener;
    }

    // 네트워크 요청 시 서버와 어떻게 값을 주고 받을지 정의하는 메소드들
    protected String getRequestMethod() {
        return GET;
    }

    public void setNetworkConfig(HttpURLConnection conn) {
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(10000);
    }

    protected void setRequestProperty(HttpURLConnection conn) {

    }

    protected void write(OutputStream output) {

    }

    // 네트워크 처리 결과가 success일 때 네트워크를 요청한 객체에 결과를 전달해준다.
    protected void process(InputStream input) throws IOException {
        T result = parse(input);
        sendSuccess(result);
    }

    T result;

    private void sendSuccess(T result) {
        this.result = result;
        NetworkManager.getInstance().sendSuccess(this);
    }

    public void sendSuccess() {
        if (listener != null) {
            listener.onSuccess(this, result);
        }
    }

    // 네트워크 처리 결과가 fail일 때 네트워크를 요청한 객체에 결과를 전달해준다.
    int code;
    String errorMessage;

    private void sendError(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
        NetworkManager.getInstance().sendFail(this);
    }

    public void sendFail() {
        if (listener != null)
            listener.onFail(this, code, errorMessage);
    }

    @Override
    public void run() {
        try {
            URL url = getURL();
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            setNetworkConfig(conn);
            String method = getRequestMethod();
            conn.setRequestMethod(method);

            if (method.equals(POST) || method.equals(PUT))
                conn.setDoOutput(true);

            setRequestProperty(conn);

            if (conn.getDoOutput())
                write(conn.getOutputStream());

            int code = conn.getResponseCode();

            if (code >= 200 && code < 300) {
                InputStream is = conn.getInputStream();
                process(is);
                return;
            } else {
                sendError(code, conn.getResponseMessage());
                return;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendError(-1, "exception");
    }
}
