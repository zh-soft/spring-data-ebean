/*
 * Copyright 2008-2011 the original author or authors.
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
package org.springframework.data.ebean.repository.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.data.repository.config.RepositoryBeanDefinitionParser;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * Simple namespace handler for {@literal repositories} namespace.
 *
 * @author Xuegui Yuan
 */
public class EbeanRepositoryNameSpaceHandler extends NamespaceHandlerSupport {

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    public void init() {

        RepositoryConfigurationExtension extension = new EbeanRepositoryConfigExtension();
        RepositoryBeanDefinitionParser repositoryBeanDefinitionParser = new RepositoryBeanDefinitionParser(extension);

        registerBeanDefinitionParser("repositories", repositoryBeanDefinitionParser);
    }
}
