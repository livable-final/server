package com.livable.server.restaurant.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class RandomPageGenerator implements RandomGenerator<Pageable> {

    private static final int DEFAULT_PAGE_SIZE = 5;

    @Override
    public Pageable getRandom(int end) {
        return getRandom(0, end);
    }

    @Override
    public Pageable getRandom(int start, int end) {
        int randomNumber = getRandomNumber(start, end - DEFAULT_PAGE_SIZE);
        return PageRequest.of(randomNumber, DEFAULT_PAGE_SIZE);
    }
}
