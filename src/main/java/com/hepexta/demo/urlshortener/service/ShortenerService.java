package com.hepexta.demo.urlshortener.service;

import com.hepexta.demo.urlshortener.repository.ShortenerRepository;

public class ShortenerService {

    private final ShortenerRepository shortenerRepository;

    public ShortenerService(ShortenerRepository shortenerRepository) {
        this.shortenerRepository = shortenerRepository;
    }

    public String getLong(String shortUrl) {
        return shortenerRepository.getLong(shortUrl);
    }

    public String shorten(String longUrl) {
        return shortenerRepository.shorten(longUrl);
    }
}
