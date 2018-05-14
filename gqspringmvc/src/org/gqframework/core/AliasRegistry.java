package org.gqframework.core;

public interface AliasRegistry {

    void registerAlias(String name,String alias);

    void removeAlias(String alias);

    boolean isAlias(String name);

    String[] getAlias(String name);
}
