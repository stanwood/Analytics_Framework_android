package io.stanwood.framework.analytics.testfairy;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * OkHttp interceptor to log HTTP calls to Testfairy.
 * <p>
 * For integration guidelines and details check out the <a href="https://docs.testfairy.com/Android/Log_Network.html">Testfairy documentation</a>.
 */
public class TestfairyHttpInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
