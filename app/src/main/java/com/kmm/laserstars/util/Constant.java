package com.kmm.laserstars.util;

public class Constant {
    public static final String VIDEO_URL = "video_url";
    public static final String IMAGE_URL = "url";
    public static final String DESIGNS = "desgins";
    public static final String DISTRIBUTOR = "distributor";
    public static final String SELECTED_IDS = "ids";
    public static final String SELECTED_NAMES = "names";
    public static final String SELECTED_DESIGNS_TITLE = "الوسوم المختارة : ";

    private static  double ACCEPTED_WIDTH = -1;
    private static double ACCEPTED_HEIGHT = -1;

    public static void SET_ACCEPTED_DIM(double height, double width) {
        ACCEPTED_HEIGHT = height;
        ACCEPTED_WIDTH = width;
    }
    public static double getAcceptedWidth(){
        return ACCEPTED_WIDTH;
    }
    public static double getAcceptedHeight(){
        return ACCEPTED_HEIGHT;
    }


}
