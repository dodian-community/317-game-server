package net.dodian.http;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Data
@Component
public class HttpClient implements InitializingBean {
    private String server;
    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatus status;

    public <T> ResponseEntity<T> get(String uri, Class<T> type) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        return rest.exchange(server + uri, HttpMethod.GET, requestEntity, type);
    }

    public <T> ResponseEntity<T> post(String uri, String json, Class<T> type) {
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        return rest.exchange(server + uri, HttpMethod.POST, requestEntity, type);
    }

    public <T> ResponseEntity<T> put(String uri, String json, Class<T> type) {
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        return rest.exchange(server + uri, HttpMethod.PUT, requestEntity, type);
    }

    public <T> ResponseEntity<T> delete(String uri, Class<T> type) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        return rest.exchange(server + uri, HttpMethod.DELETE, requestEntity, type);
    }

    private void setStreamingMode(boolean enabled) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(enabled);

        this.getRest().setRequestFactory(requestFactory);
    }

    public void useStreamingMode() {
        this.setStreamingMode(true);
    }

    @Override
    public void afterPropertiesSet() {
        this.server = "http://localhost/";
        this.headers = new HttpHeaders();
        this.rest    = new RestTemplate();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
        this.rest.setErrorHandler(new ErrorHandler());
        this.setStreamingMode(false);
    }

    public static class ErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {

        }
    }
}
