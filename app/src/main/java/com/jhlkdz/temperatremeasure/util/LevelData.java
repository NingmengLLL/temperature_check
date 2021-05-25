package com.jhlkdz.temperatremeasure.util;

public class LevelData {

    private float max;
    private float min;
    private float average;

    public LevelData() {
    }

    public LevelData(float max, float min, float average) {
        this.max = max;
        this.min = min;
        this.average = average;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }
}
