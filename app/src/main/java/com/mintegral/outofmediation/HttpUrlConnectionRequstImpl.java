package com.mintegral.outofmediation;

import com.mintegral.mediation.network.BaseRequest;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * BaseRequest实现
 * 使用HttpUrlConnection
 */
public class HttpUrlConnectionRequstImpl implements BaseRequest<String> {
    private String baseUrl = "https://api.map.baidu.com/place/v2/search";
    private HashMap<String, String> paramsMap;

    public HttpUrlConnectionRequstImpl(HashMap<String, String> params) {
        paramsMap = params;
    }

    @Override
    public String request() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            if (paramsMap != null && !paramsMap.isEmpty()) {
                stringBuilder.append("?");
                for (String key : paramsMap.keySet()) {
                    stringBuilder.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    stringBuilder.append("&");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }

            String requestUrl = baseUrl + stringBuilder.toString();
            URL url = new URL(requestUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5 * 1000);
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.setUseCaches(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Connection", "Keep-Alive");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = urlConnection.getInputStream().read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                baos.close();
                urlConnection.getInputStream().close();
                byte[] byteArray = baos.toByteArray();

                return new String(byteArray);
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
