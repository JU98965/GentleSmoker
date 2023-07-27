package com.example.capstoneproject;

public class MainData {

    private String txtTitle;
    private String txtDate;

    public MainData(String txtTitle, String txtDate) {
        this.txtTitle = txtTitle;
        this.txtDate = txtDate;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public String getTxtDate() {
        return txtDate;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }
}
