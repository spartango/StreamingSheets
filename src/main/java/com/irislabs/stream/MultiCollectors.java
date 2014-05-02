package com.irislabs.stream;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Author: spartango
 * Date: 5/2/14
 * Time: 5:30 PM.
 */
public class MultiCollectors {
    private static Set<Collector.Characteristics> characteristics = EnumSet.of(Collector.Characteristics.UNORDERED,
                                                                               Collector.Characteristics.IDENTITY_FINISH);

    public static <T, K> Collector<T, ?, Multimap<K, T>> toMultimap(Function<? super T, ? extends K> keyMapper) {
        return toMultimap(keyMapper, Function.identity());
    }

    public static <T, K, U> Collector<T, ?, Multimap<K, U>> toMultimap(Function<? super T, ? extends K> keyMapper,
                                                                       Function<? super T, ? extends U> valueMapper) {
        return new Collector<T, Multimap<K, U>, Multimap<K, U>>() {
            @Override public Supplier<Multimap<K, U>> supplier() {
                return LinkedListMultimap::create;
            }

            @Override public BiConsumer<Multimap<K, U>, T> accumulator() {
                return (map, entry) -> map.put(keyMapper.apply(entry), valueMapper.apply(entry));
            }

            @Override public BinaryOperator<Multimap<K, U>> combiner() {
                return (firstMap, secondMap) -> {
                    firstMap.putAll(secondMap);
                    return firstMap;
                };
            }

            @Override public Function<Multimap<K, U>, Multimap<K, U>> finisher() {
                return Function.identity();
            }

            @Override public Set<Characteristics> characteristics() {
                return characteristics;
            }
        };
    }


}
