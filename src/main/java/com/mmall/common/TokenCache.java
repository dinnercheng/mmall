package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;


public class TokenCache {


    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public  static String TOKEN_PREFIX = "token_";


    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(1000)
            .build(new CacheLoader<String, String>() {

                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key,value);

    }

    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if(value.equals("null")){
                return  null;
            }
            return  value;
        } catch (ExecutionException e) {
            logger.error("localCache error is "+e);
            e.printStackTrace();
        }
        return  null;
    }

}
