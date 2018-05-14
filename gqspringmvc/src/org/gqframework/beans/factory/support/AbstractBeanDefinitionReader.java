package org.gqframework.beans.factory.support;

import org.apache.log4j.Logger;
import org.gqframework.beans.factory.BeanDefinitionStoreException;
import org.gqframework.core.env.EnvironmentCapable;
import org.gqframework.core.io.Resource;
import org.gqframework.core.io.ResourceLoader;
import org.gqframework.core.io.support.ResourcePatternResolver;
import org.gqframework.lang.Nullable;

import java.io.IOException;
import java.util.Set;

public abstract class AbstractBeanDefinitionReader
        implements EnvironmentCapable, BeanDefinitionReader {

    protected final Logger logger = Logger.getLogger(getClass());

    private final BeanDefinitionRegistry registry;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry){
        this.registry = registry;
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    public int loadBeanDefinitions(String location, @Nullable Set<Resource> actualResources) throws BeanDefinitionStoreException {
        ResourceLoader resourceLoader = getResourceLoader();
        if (resourceLoader == null) {
            throw new BeanDefinitionStoreException(
                    "Cannot import bean definitions from location [" + location + "]: no ResourceLoader available");
        }

        if (resourceLoader instanceof ResourcePatternResolver) {
            // Resource pattern matching available.
            try {
                Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
                int loadCount = loadBeanDefinitions(resources);
                if (actualResources != null) {
                    for (Resource resource : resources) {
                        actualResources.add(resource);
                    }
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Loaded " + loadCount + " bean definitions from location pattern [" + location + "]");
                }
                return loadCount;
            }
            catch (IOException ex) {
                throw new BeanDefinitionStoreException(
                        "Could not resolve bean definition resource pattern [" + location + "]", ex);
            }
        }
        else {
            // Can only load single resources by absolute URL.
            Resource resource = resourceLoader.getResource(location);
            int loadCount = loadBeanDefinitions(resource);
            if (actualResources != null) {
                actualResources.add(resource);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Loaded " + loadCount + " bean definitions from location [" + location + "]");
            }
            return loadCount;
        }
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return null;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return null;
    }

    @Override
    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
        return 0;
    }

    @Override
    public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
        return 0;
    }

    @Override
    public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
        return 0;
    }

    @Override
    public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
        return 0;
    }
}