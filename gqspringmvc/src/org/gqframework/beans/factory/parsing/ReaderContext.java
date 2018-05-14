package org.gqframework.beans.factory.parsing;

import org.gqframework.core.io.Resource;

public class ReaderContext {
    private final Resource resource;

    public ReaderContext(Resource resource){
        this.resource = resource;
    }

    public final Resource getResource(){
        return this.resource;
    }
}
