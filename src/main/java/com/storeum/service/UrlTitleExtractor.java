package com.storeum.service;

import com.storeum.exception.ResourceUnavailableException;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UrlTitleExtractor {

    public String extract(String url) {
        try {
            return Jsoup.connect(url).get().title();
        } catch (IOException e) {
            throw new ResourceUnavailableException(String.format("Cannot extract title from url=%s", url));
        }
    }
}
