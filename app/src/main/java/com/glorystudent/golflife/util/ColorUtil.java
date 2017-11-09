package com.glorystudent.golflife.util;

import android.graphics.Color;

/**
 * TODO 颜色工具类
 * Created by Gavin.J on 2017/11/9.
 */

public class ColorUtil {
    public static int stringToColor(String colorStr) {
        switch (colorStr){
            case "1,1,0,1":
                return Color.YELLOW;
            case "1,0,0,1":
                return Color.RED;
            case "0,1,0,1":
                return Color.GREEN;
            case "0,0,1,1":
                return Color.BLUE;
        }
        return Color.YELLOW;
    }
}
