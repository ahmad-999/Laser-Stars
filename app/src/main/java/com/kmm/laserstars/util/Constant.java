package com.kmm.laserstars.util;

public class Constant {
    public static final String VIDEO_URL = "video_url";
    public static final String IMAGE_URL = "url";
    public static final String DESIGNS = "desgins";
    public static final String DISTRIBUTOR = "distributor";
    public static final String SELECTED_IDS = "ids";
    public static final String SELECTED_NAMES = "names";
    public static final String SELECTED_DESIGNS_TITLE = "الوسوم المختارة : ";

    private static final int ACCEPTED_WIDTH = 120;
    private static final int ACCEPTED_HEIGHT = 330;

    public static boolean CHECK_IMAGE_DIM(int height, int width) {
        return height == ACCEPTED_HEIGHT && width == ACCEPTED_WIDTH;
    }
}
