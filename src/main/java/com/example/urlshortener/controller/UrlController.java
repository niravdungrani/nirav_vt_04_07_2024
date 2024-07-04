package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestBody Url url) {
        Url shortUrl = urlService.createShortUrl(url.getDestinationUrl());
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateShortUrl(@RequestParam String shortUrl, @RequestParam String destinationUrl) {
        Url updatedUrl = urlService.updateShortUrl(shortUrl, destinationUrl);
        if (updatedUrl != null) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get")
    public ResponseEntity<String> getDestinationUrl(@RequestParam String shortUrl) {
        Url url = urlService.getDestinationUrl(shortUrl);
        if (url != null) {
            return new ResponseEntity<>(url.getDestinationUrl(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/update_expiry")
    public ResponseEntity<Boolean> updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd) {
        Url updatedUrl = urlService.updateExpiry(shortUrl, daysToAdd);
        if (updatedUrl != null) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{shortenString}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortenString) {
        try {
            Url url = urlService.getDestinationUrl(shortenString);
            if (url != null) {
                response.sendRedirect(url.getDestinationUrl());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }
}