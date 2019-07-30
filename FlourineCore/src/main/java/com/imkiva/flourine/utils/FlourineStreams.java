package com.imkiva.flourine.utils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author kiva
 * @date 2019-07-30
 */
public class FlourineStreams {
    public static <A, B, R> Stream<R> zip(List<A> one, List<B> two, BiFunction<A, B, R> zipper) {
        int length = Math.min(one.size(), two.size());
        return IntStream.range(0, length).mapToObj(i -> zipper.apply(one.get(i), two.get(i)));
    }
}
