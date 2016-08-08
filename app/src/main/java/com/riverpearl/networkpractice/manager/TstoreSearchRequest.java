package com.riverpearl.networkpractice.manager;

import com.google.gson.Gson;
import com.riverpearl.networkpractice.autodata.Tstore;
import com.riverpearl.networkpractice.autodata.TstoreResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Tacademy on 2016-08-08.
 */
public class TstoreSearchRequest extends NetworkRequest<Tstore> {

    private static final String TSTORE_SEARCH_URL = "http://apis.skplanetx.com/tstore/products?version=1&page=%d&count=%d&searchKeyword=%s&order=%s";
    public static final String SORT_ACCURACY = "R";
    public static final String SORT_LATEST = "L";
    public static final String SORT_DOWNLOAD = "D";

    String url;

    public TstoreSearchRequest(String keyword) {
        this(1, 10, keyword, SORT_ACCURACY);
    }

    public TstoreSearchRequest(int page, int count, String keyword) {
        this(page, count, keyword, SORT_ACCURACY);
    }

    public TstoreSearchRequest(int page, int count, String keyword, String sort) {
        try {
            url = String.format(TSTORE_SEARCH_URL, page, count, URLEncoder.encode(keyword, "utf-8"), sort);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("appKey","b9982d77-eefc-30ce-8e7e-25a52cc10bb6");
    }

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(url);
    }

    @Override
    protected Tstore parse(InputStream is) {
        Gson gson = new Gson();
        TstoreResult result = gson.fromJson(new InputStreamReader(is), TstoreResult.class);
        return result.getTstore();
    }
}
