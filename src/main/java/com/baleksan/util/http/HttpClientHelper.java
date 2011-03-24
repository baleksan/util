package com.baleksan.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */

public class HttpClientHelper {
    private static Logger LOG = LogManager.getLogger(HttpClientHelper.class);

    public static HttpResponseBean executeGet(String url) throws IOException {
        return executeGet(url, Integer.MAX_VALUE);
    }

    public static HttpResponseBean executeGet(String url, int timeoutMsec) throws IOException {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutMsec);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMsec);
        HttpClient client = new DefaultHttpClient(httpParams);

        HttpGet get = new HttpGet(url);

        return client.execute(get, getHandler(HttpMethod.GET, url));
    }

    public static HttpResponseBean executePost(String url, String body, int timeoutMsec, String contentType) throws IOException {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutMsec);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMsec);
        HttpClient client = new DefaultHttpClient(httpParams);

        HttpPost post = new HttpPost(url);
        post.setHeader(HTTP.CONTENT_TYPE, contentType);
        post.setEntity(new StringEntity(body, HTTP.UTF_8));

        return client.execute(post, getHandler(HttpMethod.POST, url));
    }

    public static HttpResponseBean executeGet(String url, List<Cookie> cookies) throws IOException {
        return executeGet(url, cookies, Integer.MAX_VALUE);
    }

    public static HttpResponseBean executeGet(String url, List<Cookie> cookies, int timeoutMsec) throws IOException {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutMsec);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMsec);
        httpParams.setParameter("User-Agent", "Chrome/6.0.472.63");

        DefaultHttpClient client = new DefaultHttpClient(httpParams);

        CookieStore cookieStore = new BasicCookieStore();
        for (Cookie cookie : cookies) {
            cookieStore.addCookie(cookie);
        }
        client.setCookieStore(cookieStore);

        client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
                CookiePolicy.RFC_2965);

        HttpGet get = new HttpGet(url);

        return client.execute(get, getHandler(HttpMethod.GET, url));
    }

    public static ResponseHandler<HttpResponseBean> getHandler(final HttpMethod method, final String url) {
        return new ResponseHandler<HttpResponseBean>() {
            public HttpResponseBean handleResponse(HttpResponse response) throws IOException {
                HttpResponseBean bean = new HttpResponseBean();
                bean.statusCode = response.getStatusLine().getStatusCode();
                bean.method = method;

                if (!bean.isSuccess()) {
                    LOG.error("Method failed: " + response.getStatusLine() + " url: " + url);
                }
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    bean.response = EntityUtils.toString(entity);
                }

                return bean;
            }
        };
    }

    public static HttpResponseBean executeParallelGet(List<String> urls, Executor executor, int timeoutMsec)
            throws InterruptedException {
        List<SingleRequestSubmitter> solvers = new ArrayList<SingleRequestSubmitter>();
        for (String url : urls) {
            solvers.add(new SingleRequestSubmitter(url, timeoutMsec));
        }

        return submitSolvers(executor, solvers);
    }

    private static HttpResponseBean submitSolvers(Executor executor, List<SingleRequestSubmitter> solvers)
            throws InterruptedException {
        CompletionService<HttpResponseBean> ecs = new ExecutorCompletionService<HttpResponseBean>(executor);
        int n = solvers.size();
        List<Future<HttpResponseBean>> futures = new ArrayList<Future<HttpResponseBean>>(n);
        HttpResponseBean result = null;
        try {
            for (Callable<HttpResponseBean> s : solvers) {
                futures.add(ecs.submit(s));
            }
            for (int i = 0; i < n; ++i) {
                try {
                    HttpResponseBean bean = ecs.take().get();
                    result = bean;
                    if (bean.isSuccess()) {
                        break;
                    }
                } catch (ExecutionException ignore) {
                }
            }
        } finally {
            for (Future<HttpResponseBean> f : futures)
                f.cancel(true);
        }

        return result == null ? serviceUnavailableResponse() : result;
    }

    private static HttpResponseBean serviceUnavailableResponse() {
        HttpResponseBean bean = new HttpResponseBean();
        bean.statusCode = HttpStatus.SC_SERVICE_UNAVAILABLE;
        bean.method = HttpMethod.GET;

        return bean;
    }

    private static HttpResponseBean timeoutResponse(Exception ex) {
        HttpResponseBean bean = new HttpResponseBean();
        bean.statusCode = HttpStatus.SC_GATEWAY_TIMEOUT;
        bean.method = HttpMethod.GET;
        bean.response = ex.getLocalizedMessage();

        return bean;
    }

    private static HttpResponseBean internalServerErrorResponse(Exception ex) {
        HttpResponseBean bean = new HttpResponseBean();
        bean.statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        bean.method = HttpMethod.GET;
        bean.response = ex.getLocalizedMessage();

        return bean;
    }

    private static class SingleRequestSubmitter implements Callable<HttpResponseBean> {
        private String url;
        private int timeoutMsec;

        public SingleRequestSubmitter(String url, int timeoutMsec) {
            this.url = url;
            this.timeoutMsec = timeoutMsec;
        }

        @Override
        public HttpResponseBean call() throws Exception {
            try {
                return HttpClientHelper.executeGet(url, timeoutMsec);
            } catch (SocketTimeoutException ex) {
                return timeoutResponse(ex);
            } catch (Exception ex) {
                return internalServerErrorResponse(ex);
            }
        }
    }
}
