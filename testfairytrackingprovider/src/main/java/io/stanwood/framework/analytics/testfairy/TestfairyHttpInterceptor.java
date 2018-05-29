package io.stanwood.framework.analytics.testfairy;

import android.support.annotation.NonNull;

import com.testfairy.TestFairy;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp interceptor to log HTTP calls to Testfairy.
 * <p>
 * For integration guidelines and details check out the <a href="https://docs.testfairy.com/Android/Log_Network.html">Testfairy documentation</a>.
 * <p>
 * WHEN ADAPTING THIS CLASS ALWAYS ALSO CHECK THE NO-OP VARIANT!
 */
public class TestfairyHttpInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        long startTimeMillis = System.currentTimeMillis();
        RequestBody requestBody = request.body();
        Long requestSize = requestBody != null ? requestBody.contentLength() : 0;
        Response response;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            long endTimeMillis = System.currentTimeMillis();
            TestFairy.addNetworkEvent(request.url().uri(), request.method(), -1, startTimeMillis, endTimeMillis, requestSize, -1, e.getMessage());
            throw e;
        }

        long endTimeMillis = System.currentTimeMillis();
        ResponseBody responseBody = response.body();
        long responseSize = responseBody != null ? responseBody.contentLength() : 0;
        TestFairy.addNetworkEvent(request.url().uri(), request.method(), response.code(), startTimeMillis, endTimeMillis, requestSize, responseSize, null);
        return response;
    }
}
