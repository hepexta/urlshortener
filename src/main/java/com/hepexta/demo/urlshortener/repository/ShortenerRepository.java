package com.hepexta.demo.urlshortener.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShortenerRepository {

    private final ConcurrentHashMap<String, String> urlStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> reverseUrlStorage = new ConcurrentHashMap<>();
    private final AtomicLong index;
    private final String BASE_URL = "http://short.com/";
    private final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public ShortenerRepository(long initIndex) {
        index = new AtomicLong(initIndex);
    }

    public String getLong(String shortUrl) {
        return urlStorage.get(shortUrl);
    }

    public void init(Map<String, String> initStorage) {
        if (urlStorage.isEmpty()) {
            for (Map.Entry<String, String> entry : initStorage.entrySet()) {
                urlStorage.put(entry.getKey(), entry.getValue());
                reverseUrlStorage.put(entry.getValue(), entry.getKey());
                index.incrementAndGet();
            }

        }
    }

    public String shorten(String longUrl) {
        return reverseUrlStorage.computeIfAbsent(longUrl, url -> {
            long nextId = index.getAndIncrement();
            String shortUrl;
            do {
                shortUrl = encodeBase64(nextId);
            } while (urlStorage.putIfAbsent(shortUrl, url) != null);
            return shortUrl;
        });
    }

    private String encodeBase64(long id) {
        StringBuilder shortUrl = new StringBuilder();
        while (id > 0) {
            shortUrl.append(BASE62.charAt((int) (id % BASE62.length())));
            id /= BASE62.length();
        }
        return shortUrl.toString();
    }
}
