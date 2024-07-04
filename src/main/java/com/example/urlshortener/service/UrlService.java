package com.example.urlshortener.service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.util.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;

    public Url createShortUrl(String destinationUrl) {
        Url url = new Url();
        url.setDestinationUrl(destinationUrl);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiresAt(LocalDateTime.now().plusMonths(10));
        url = urlRepository.save(url); // Save to generate ID

        String shortUrl = Base62.encode(url.getId());
        url.setShortUrl(shortUrl);
        return urlRepository.save(url);
    }

    public Url updateShortUrl(String shortUrl, String newDestinationUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url != null) {
            url.setDestinationUrl(newDestinationUrl);
            return urlRepository.save(url);
        }
        return null;
    }

    public Url getDestinationUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    public Url updateExpiry(String shortUrl, int daysToAdd) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url != null) {
            url.setExpiresAt(url.getExpiresAt().plusDays(daysToAdd));
            return urlRepository.save(url);
        }
        return null;
    }
}