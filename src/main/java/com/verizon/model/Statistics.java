package com.verizon.model;

public class Statistics {

    private final String category;
    private final String title;
    private final Double videoLength;
    private final Double averageWatchTime;
    private final Integer numberOfViews;

    public Statistics(String category, String title, Double videoLength, Double averageWatchTime, Integer numberOfViews) {
        this.category = category;
        this.title = title;
        this.videoLength = videoLength;
        this.averageWatchTime = averageWatchTime;
        this.numberOfViews = numberOfViews;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public Double getVideoLength() {
        return videoLength;
    }

    public Double getAverageWatchTime() {
        return averageWatchTime;
    }

    public Integer getNumberOfViews() {
        return numberOfViews;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", videoLength=" + videoLength +
                ", averageWatchTime=" + averageWatchTime +
                ", numberOfViews=" + numberOfViews +
                '}';
    }
}
