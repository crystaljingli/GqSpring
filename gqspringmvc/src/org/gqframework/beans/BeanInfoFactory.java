/*
 * Copyright 2002-2013 the original author or authors.
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

package org.gqframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

import org.gqframework.lang.Nullable;

public interface BeanInfoFactory {

	/**
	 * Return the bean info for the given class, if supported.
	 * @param beanClass the bean class
	 * @return the BeanInfo, or {@code null} if the given class is not supported
	 * @throws IntrospectionException in case of exceptions
	 */
	@Nullable
	BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException;

}
