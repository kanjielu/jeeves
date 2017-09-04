package com.cherry.jeeves.utils.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

//https://stackoverflow.com/a/12840202/2364882
class StatefullRestTemplate extends RestTemplate {
    private final HttpContext httpContext;

    StatefullRestTemplate(HttpContext httpContext) {
        super();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.httpContext = httpContext == null ? new BasicHttpContext() : httpContext;
        StatefullHttpComponentsClientHttpRequestFactory statefullHttpComponentsClientHttpRequestFactory
                = new StatefullHttpComponentsClientHttpRequestFactory(httpClient, httpContext);
        super.setRequestFactory(statefullHttpComponentsClientHttpRequestFactory);
    }

    HttpContext getHttpContext() {
        return httpContext;
    }

    class StatefullHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {
        private final HttpContext httpContext;

        StatefullHttpComponentsClientHttpRequestFactory(HttpClient httpClient, HttpContext httpContext) {
            super(httpClient);
            this.httpContext = httpContext;
        }

        @Override
        protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
            return this.httpContext;
        }
    }
}