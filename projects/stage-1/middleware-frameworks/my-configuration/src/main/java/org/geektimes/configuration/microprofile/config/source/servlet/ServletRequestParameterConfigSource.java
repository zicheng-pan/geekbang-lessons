/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.geektimes.configuration.microprofile.config.source.EnumerableConfigSource;
import org.geektimes.configuration.microprofile.config.source.servlet.initializer.ServletRequestThreadLocalListener;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Collections.emptyEnumeration;

/**
 * {@link ServletRequest}'s Parameters {@link ConfigSource}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */

public class ServletRequestParameterConfigSource extends EnumerableConfigSource {


    public ServletRequestParameterConfigSource() {
        super("Request Parameters", 1000);
    }

    protected Supplier<Enumeration<String>> namesSupplier() {
        return () -> {
            HttpServletRequest request = request();
            return request == null ? emptyEnumeration() : request.getParameterNames();
        };
    }

    protected Function<String, String> valueResolver() {
        return this::getParameterValue;
    }

    private String getParameterValue(String parameterName) {
        // return String Array
        // return String Array
        String[] parameterValues = request().getParameterValues(parameterName);
        return String.join(",", parameterValues);
    }

    private HttpServletRequest request() {
        return ServletRequestThreadLocalListener.getRequest();
    }
}
