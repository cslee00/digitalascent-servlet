package com.digitalascent.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.digitalascent.servlet.HttpRequests.headerValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class HttpRequestsTest {
    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        request = mock(HttpServletRequest.class);
        Map<String,String> headers = new HashMap<>();
        headers.put("foo1","bar1");
        headers.put("foo2","bar2");
        headers.put("foo2","bar3");

        headers.forEach((k,v) -> {
            when(request.getHeader(k)).thenReturn(v);
        });

        when(request.getHeaderNames()).thenReturn(Collections.enumeration(headers.keySet()));
    }

    @Test
    void optionalHeaderValue() {
        assertThat( headerValue(request, "foo")).get().isEqualTo("bar");
    }

    @Test
    void optionalHeaderValueMultiNames() {
        assertThat( headerValue(request, "missing", "foo")).get().isEqualTo("bar");
    }
}