package com.glorystudent.golflife.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gavin.J on 2017/10/24.
 */

public class EventBusMapUtil {
    public static Map<Integer, Integer> getIntMap(Integer key, Integer value){
        Map<Integer, Integer> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static Map<String, String> getStringMap(String key, String value){
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
