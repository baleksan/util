package com.baleksan.util;

import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Extends normal Enum valueOf mapping with aliases.
 *  @author <a href="mailto:cbreyman@yammer-inc.com" boris/>
 */
public class AliasManager {
    private static final Logger LOG = Logger.getLogger(AliasManager.class.getName());

    private static final ConcurrentHashMap<Object, Object> typeMap = new ConcurrentHashMap<Object, Object>();

    @SuppressWarnings("unchecked")
    public static <T> void registerAlias(T item, String alias) {
        Aliases<T> typeAliases = null;
        synchronized (typeMap) {
            Object mapItem = typeMap.get(item.getClass());
            typeAliases = (Aliases<T>) mapItem;
            if (typeAliases == null) {
                typeAliases = new Aliases<T>();
                typeMap.put(item.getClass(), typeAliases);
            }
        }
        typeAliases.register(item, alias);
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(String alias, Class<T> klass) {
        Object mapItem = typeMap.get(klass);
        Aliases<T> typeAliases = (Aliases<T>) mapItem;
        return typeAliases.lookup(alias);
    }

    private static class Aliases<T> {
        private final ConcurrentHashMap<String, T> aliases = new
                ConcurrentHashMap<String, T>();

        public void register(T item, String alias) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Alias %s[%s] <= %s\n", item.getClass().getName(), item.toString(), alias));
            }
            aliases.put(alias, item);
        }

        public T lookup(String alias) {
            return aliases.get(alias);
        }
    }
}
