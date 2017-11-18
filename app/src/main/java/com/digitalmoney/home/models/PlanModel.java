package com.digitalmoney.home.models;

/**
 * Created by shailesh on 14/11/17.
 */

public class PlanModel {

    public PlanModel(String percent, String level) {
        this.percent = percent;
        this.level = level;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private String percent;
    private String level;

    @Override
    public String toString() {
        return "PlanModel{" +
                "percent='" + percent + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
