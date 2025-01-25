package com.hepexta.demo.urlshortener.service;

import com.hepexta.demo.urlshortener.repository.ShortenerRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;


class ShortenerServiceTest {

    ShortenerRepository shortenerRepository = new ShortenerRepository(9874563229185L);
    ShortenerService shortenerService = new ShortenerService(shortenerRepository);

    @Test
    public void ShortenerGetLongURL () {

        String shortUrl = "short";
        String expected = "longurl";

        shortenerRepository.init(Map.of(shortUrl, expected));
        assertEquals(expected, shortenerService.getLong(shortUrl));
    }

    @Test
    public void ShortenerTestShorten () {

        String longUrl = "very/long/url/veryvery/long";
        String longUrl2 = "very/long/url/veryvery/long2";

        String shorten = shortenerService.shorten(longUrl);
        String shorten2 = shortenerService.shorten(longUrl2);
        assertNotNull(shorten);
        assertNotNull(shorten2);
        assertNotEquals(shorten, shorten2);
    }

    @Test
    public void ShortenerTestShortenParallel () {

        String longUrl = "very/long/url/veryvery/long";
        String longUrl2 = "very/long/url/veryvery/long2";

        Callable<String> task1 = () -> {
            String shorten = shortenerService.shorten(longUrl);
            assertNotNull(shorten);
            assertEquals(longUrl, shortenerService.getLong(shorten));
            return shorten;
        };

        Callable<String> task2 = () -> {
            String shorten = shortenerService.shorten(longUrl2);
            assertNotNull(shorten);
            assertEquals(longUrl2, shortenerService.getLong(shorten));
            return shorten;
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        try {
            List<Future<String>> futures = executorService.invokeAll(Arrays.asList(task1, task2));
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
            fail();
        }
    }

}