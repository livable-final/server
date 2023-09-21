package com.livable.server.restaurant.domain;

import java.util.Random;

public interface RandomGenerator<T> {

    T getRandom(int end);
    T getRandom(int start, int end);
}
