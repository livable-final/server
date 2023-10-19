package com.livable.server.restaurant.domain;

import java.util.Random;

public interface RandomGenerator<T> {

    default int getRandomNumber(int end) {
        return getRandomNumber(0, end);
    }

    default int getRandomNumber(int start, int end) {
        return new Random().nextInt(end) + start;
    }

    T getRandom(int end);
    T getRandom(int start, int end);
}
