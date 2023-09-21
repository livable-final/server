package com.livable.server.restaurant.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomPageGenerator implements RandomGenerator<Pageable> {

    private static final int DEFAULT_PAGE_SIZE = 5;

    int getRandomNumber(int start, int end) {
        return new Random().nextInt(end) + start;
    }

    @Override
    public Pageable getRandom(int end) {
        return getRandom(1, end);
    }

    @Override
    public Pageable getRandom(int start, int end) {
        int randomNumber = getRandomNumber(start, end - DEFAULT_PAGE_SIZE);
        return PageRequest.of(randomNumber, DEFAULT_PAGE_SIZE);
    }
}
