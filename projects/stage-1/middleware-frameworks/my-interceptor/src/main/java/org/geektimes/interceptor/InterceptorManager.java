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
package org.geektimes.interceptor;

import org.geektimes.commons.lang.util.ClassLoaderUtils;
import org.geektimes.commons.util.ServiceLoaders;
import org.geektimes.interceptor.util.InterceptorUtils;

import javax.annotation.Priority;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.asList;
import static org.geektimes.commons.util.ServiceLoaders.loadSpi;

/**
 * The Mananger of {@link Interceptor}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public interface InterceptorManager {

    void registerInterceptorClass(Class<?> interceptorClass);

    default void registerInterceptorClasses(Class<?> interceptorClass, Class<?>... otherInterceptorClasses) {
        registerInterceptorClass(interceptorClass);
        registerInterceptorClasses(otherInterceptorClasses);
    }

    default void registerInterceptorClasses(Class<?>[] interceptorClasses) {
        registerInterceptorClasses(asList(interceptorClasses));
    }

    default void registerInterceptorClasses(Iterable<Class<?>> interceptorClasses) {
        interceptorClasses.forEach(this::registerInterceptorClass);
    }

    void registerInterceptor(Object interceptor);

    default void registerInterceptors(Object interceptor, Object... otherInterceptors) {
        registerInterceptor(interceptor);
        registerInterceptors(otherInterceptors);
    }

    default void registerInterceptors(Object[] interceptors) {
        registerInterceptors(asList(interceptors));
    }

    default void registerInterceptors(Iterable<?> interceptors) {
        interceptors.forEach(this::registerInterceptor);
    }

    default void registerDiscoveredInterceptors() {
        registerInterceptors(ServiceLoaders.load(Interceptor.class));
    }

    /**
     * Gets the {@linkplain InterceptorBinding interceptor bindings} of the interceptor.
     *
     * @return the instance of {@linkplain InterceptorBindings interceptor bindings}
     * @throws IllegalStateException See exception details on {@link InterceptorUtils#isInterceptorClass(Class)}
     */
    default InterceptorBindings getInterceptorBindings(Class<?> interceptorClass) throws IllegalStateException {
        return getInterceptorInfo(interceptorClass).getInterceptorBindings();
    }

    /**
     * Get the instance of {@link InterceptorInfo} from the given interceptor class
     *
     * @param interceptorClass the given interceptor class
     * @return non-null if <code>interceptorClass</code> is a valid interceptor class
     * @throws IllegalStateException See exception details on {@link InterceptorUtils#isInterceptorClass(Class)}
     */
    InterceptorInfo getInterceptorInfo(Class<?> interceptorClass) throws IllegalStateException;

    /**
     * Resolve the bindings of {@link javax.interceptor.Interceptor @Interceptor} instances upon
     * the specified {@link Method}
     *
     * @param method              the intercepted of {@linkplain Method method}
     * @param defaultInterceptors the default interceptors
     * @return a non-null read-only {@link Priority priority} {@link List list} of
     * {@link javax.interceptor.Interceptor @Interceptor} instances
     */
    default List<Object> resolveInterceptors(Method method, Object... defaultInterceptors) {
        return resolveInterceptors((Executable) method, defaultInterceptors);
    }

    /**
     * Resolve the bindings of {@link javax.interceptor.Interceptor @Interceptor} instances upon
     * the specified {@link Constructor}
     *
     * @param constructor         the intercepted of {@linkplain Constructor constructor}
     * @param defaultInterceptors the default interceptors
     * @return a non-null read-only {@link Priority priority} {@link List list} of
     * {@link javax.interceptor.Interceptor @Interceptor} instances
     */
    default List<Object> resolveInterceptors(Constructor<?> constructor, Object... defaultInterceptors) {
        return resolveInterceptors((Executable) constructor, defaultInterceptors);
    }

    /**
     * Resolve the bindings of {@link javax.interceptor.Interceptor @Interceptor} instances upon
     * the specified {@linkplain Method method} or {@link Constructor}
     * <p>
     * See Specification:
     * <p>
     * 5.2 Interceptor Ordering Rules
     * <p>
     * 5.2.1 Use of the Priority Annotation in Ordering Interceptors
     *
     * @param executable          the intercepted of {@linkplain Method method} or {@linkplain Constructor constructor}
     * @param defaultInterceptors the default interceptors
     * @return a non-null read-only {@link Priority priority} {@link List list} of
     * {@link javax.interceptor.Interceptor @Interceptor} instances
     */
    List<Object> resolveInterceptors(Executable executable, Object... defaultInterceptors);

    /**
     * <p>
     * Declares an annotation type as an {@linkplain javax.interceptor.Interceptor @Interceptor} binding type if you
     * wish to make an annotation an interceptor binding type without adding {@link InterceptorBinding} to it.
     * </p>
     *
     * @param interceptorBindingType
     */
    void registerInterceptorBindingType(Class<? extends Annotation> interceptorBindingType);

    default boolean isInterceptorBinding(Annotation annotation) {
        return isInterceptorBindingType(annotation.annotationType());
    }

    boolean isInterceptorBindingType(Class<? extends Annotation> annotationType);

    static InterceptorManager getInstance(ClassLoader classLoader) {
        return loadSpi(InterceptorManager.class, classLoader);
    }

    static InterceptorManager getInstance() {
        return getInstance(ClassLoaderUtils.getClassLoader(InterceptorManager.class));
    }
}
