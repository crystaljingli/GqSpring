/*
 * Copyright 2002-2017 the original author or authors.
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

package org.gqframework.beans.factory.xml;

import java.io.StringReader;

import org.gqframework.beans.factory.parsing.ReaderContext;
import org.gqframework.beans.factory.support.BeanDefinitionReader;
import org.gqframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlReaderContext extends ReaderContext{
    private XmlBeanDefinitionReader reader;

    public XmlBeanDefinitionReader getReader() {
        return reader;
    }

    public XmlReaderContext(Resource resource
            , XmlBeanDefinitionReader reader){
        super(resource);
        this.reader = reader;
    }
}
