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
package org.geektimes.enterprise.inject.standard.producer;

import org.geektimes.enterprise.inject.standard.beans.StandardBeanManager;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Producer;
import javax.enterprise.inject.spi.ProducerFactory;
import java.lang.reflect.Method;

/**
 * {@link ProducerFactory} from the Producer {@link Method}
 *
 * @param <X> type of the bean containing the producer
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ProducerFieldFactory<X> implements ProducerFactory<X> {

    private final AnnotatedField<X> producerField;

    private final StandardBeanManager standardBeanManager;

    private final Bean<X> declaringBean;

    public ProducerFieldFactory(AnnotatedField<X> producerField, Bean<X> declaringBean, StandardBeanManager standardBeanManager) {
        this.producerField = producerField;
        this.standardBeanManager = standardBeanManager;
        this.declaringBean = declaringBean;
    }

    @Override
    public Producer createProducer(Bean bean) {
        return new AnnotatedFieldProducer(producerField, declaringBean, standardBeanManager);
    }
}
