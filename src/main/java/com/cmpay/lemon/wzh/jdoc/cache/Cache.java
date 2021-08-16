package com.cmpay.lemon.wzh.jdoc.cache;

import com.wzh.jdoc.entity.Model;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

/**
 * @author wzh
 * @since 2021/8/2
 */
@Component
public class Cache {
    private static final String CACHE_NAME = "apis";
    private static Map<String, Model> cache;

    @PostConstruct
    public void init() {
        cache = new ConcurrentReferenceHashMap<>();
    }

    /**
     * 放入缓存
     *
     * @param model model
     */
    public static void put(Model model) {
        if (cache == null) {
            cache = new ConcurrentReferenceHashMap<>();
        }
        cache.put(CACHE_NAME, model);
    }

    /**
     * 刷新缓存
     *
     * @param model model
     */
    public static void refresh(Model model) {
        if (cache == null) {
            cache = new ConcurrentReferenceHashMap<>();
        }
        cache.put(CACHE_NAME, model);
    }

    /**
     * 获取缓存
     *
     * @return model
     */
    public static Model get() {
        return cache == null ? null : cache.get(CACHE_NAME);
    }

    @PreDestroy
    public void destory() {
        cache.clear();
    }
}
