package com.example.fragment;

public class Photos {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    private final int mediaResource; // Resource ID for image or video
    private final String keyword; // Keyword for the photo
    private final int mediaType; // 0 for image, 1 for video

    public Photos(int mediaType, int mediaResource, String keyword) {
        this.mediaType = mediaType;
        this.mediaResource = mediaResource;
        this.keyword = keyword;
    }

    public int getMediaResource() {
        return mediaResource;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getMediaType() {
        return mediaType;
    }
}