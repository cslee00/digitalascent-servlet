/*
 * Copyright 2017-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digitalascent.servlet;

import com.digitalascent.common.concurrent.ExtraThreads;
import com.google.common.collect.ImmutableMap;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.digitalascent.common.base.LambdaCheckedExceptionRethrowers.rethrowingRunnable;


/**
 * Filter that exposes the current request URL and user principal as part of the thread name for
 * the duration of the call.
 */
public class ThreadNamingFilter extends AbstractFilter {
    @Override
    protected final void executeFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        Map<String, Object> context = createThreadContext(request);
        ExtraThreads.invokeWithThreadContext(context,
                rethrowingRunnable(() -> chain.doFilter(request, response)));
    }

    private Map<String, Object> createThreadContext(HttpServletRequest request) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("url", request.getRequestURL());
        context.put("principal", request.getUserPrincipal());
        customizeThreadContext( request, context );
        return ImmutableMap.copyOf(context);
    }

    /**
     * Override to add extra data to the request context, which will show up in the thread name.
     *
     *
     * @param request
     * @param context
     */
    protected void customizeThreadContext(HttpServletRequest request, Map<String, Object> context) {
        // EMPTY, for overriding
    }
}
