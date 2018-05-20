package org.gqframework.beans.factory.parsing;

import org.gqframework.core.io.Resource;
import org.gqframework.lang.Nullable;

public class ReaderContext {
    private final Resource resource;

    public ReaderContext(Resource resource){
        this.resource = resource;
    }

    public final Resource getResource(){
        return this.resource;
    }

    public void error(String message, @Nullable Object source, @Nullable ParseState parseState) {
        error(message, source, parseState, null);
    }

    public void error(String message, @Nullable Object source, @Nullable ParseState parseState, @Nullable Throwable cause) {
//        Location location = new Location(getResource(), source);
//        this.problemReporter.error(new Problem(message, location, parseState, cause));
    }
}
