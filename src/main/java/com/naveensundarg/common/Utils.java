package com.naveensundarg.common;

import com.naveensundarg.shadow.prover.representations.value.Value;
import com.naveensundarg.shadow.prover.utils.CollectionUtils;
import com.naveensundarg.shadow.prover.utils.Reader;
import com.naveensundarg.shadow.prover.utils.Sets;

import java.util.*;
import java.util.function.Predicate;

public class Utils {
    public static Value readValueFromString(String s) {
        Value term = null;
        try {
            term = Reader.readLogicValueFromString(s);
        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }
        return term;
    }

    public static <T> List<T> setToList(Set<T> tSet){

        return new ArrayList<>(tSet);
    }

     public static <T> Set<T> listToSet(List<T> tList){

        return new HashSet<>(tList);
    }

    public static <T> boolean contains(T[] tArray, T t){

        return  Arrays.stream(tArray).anyMatch(Predicate.isEqual(t));
    }

    public static <U, V> Optional<Map<V,U>> reverseMap(Map<U,V> map){

        Map<V, U> reversedMap = CollectionUtils.newMap();

        for(Map.Entry<U, V> entry : map.entrySet()){

            if(reversedMap.containsKey(entry.getValue())){
                return  Optional.empty();
            } else {

                reversedMap.put(entry.getValue(), entry.getKey());
            }
        }

        return Optional.of(reversedMap);

    }

    public static <U, V> Map<V,Set<U>> reverseMapSet(Map<U,V> map){

        Map<V, Set<U>> reversedMap = CollectionUtils.newMap();

        for(Map.Entry<U, V> entry : map.entrySet()){

            if(!reversedMap.containsKey(entry.getValue())){

                reversedMap.put(entry.getValue(), Sets.newSet());
            }

            reversedMap.put(entry.getValue(), Sets.add(reversedMap.get(entry.getValue()), entry.getKey()));
        }

        return reversedMap;

    }

    public static <U,V> Map<U, V> mapWith(U  u, V v){

        Map<U, V> map = CollectionUtils.newMap();
        map.put(u, v);

        return map;

    }

    public static <U,V> Map<U, V> mapWith(U  u1, V v1, U u2, V v2){

        Map<U, V> map = CollectionUtils.newMap();
        map.put(u1, v1);
        map.put(u2, v2);

        return map;

    }
 }
