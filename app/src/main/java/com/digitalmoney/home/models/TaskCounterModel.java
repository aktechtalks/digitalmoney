package com.digitalmoney.home.models;

/**
 * Created by shailesh on 23/11/17.
 */

public class TaskCounterModel {

    private String totalInpression;
    private String successInpression;
    private String installCount;
    private String successInstall;

    public TaskCounterModel() {
    }


    @Override
    public String toString() {
        return "TaskCounterModel{" +
                "totalInpression='" + totalInpression + '\'' +
                ", successInpression='" + successInpression + '\'' +
                ", installCount='" + installCount + '\'' +
                ", successInstall='" + successInstall + '\'' +
                '}';
    }

    public TaskCounterModel(String totalInpression, String successInpression, String installCount, String successInstall) {
        this.totalInpression = totalInpression;
        this.successInpression = successInpression;
        this.installCount = installCount;
        this.successInstall = successInstall;
    }

    public String getTotalInpression() {
        return totalInpression;
    }

    public void setTotalInpression(String totalInpression) {
        this.totalInpression = totalInpression;
    }

    public String getSuccessInpression() {
        return successInpression;
    }

    public void setSuccessInpression(String successInpression) {
        this.successInpression = successInpression;
    }

    public String getInstallCount() {
        return installCount;
    }

    public void setInstallCount(String installCount) {
        this.installCount = installCount;
    }

    public String getSuccessInstall() {
        return successInstall;
    }

    public void setSuccessInstall(String successInstall) {
        this.successInstall = successInstall;
    }


}
