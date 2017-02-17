package com.example.zhsh.gaussblur;

/**
 * ===========================================
 * 作    者：zhsh
 * 版    本：1.0
 * 创建日期：2017/2/16.
 * 描    述：popwindow上增加的按钮
 * ===========================================
 */

public class PopWindowButtonInfo {


    private int imagesrc;
    private String text;
    private String action;

    public int getImagesrc() {
        return imagesrc;
    }

    public void setImagesrc(int imagesrc) {
        this.imagesrc = imagesrc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "PopWindowButtonBean [imagesrc=" + imagesrc + ", text=" + text
                + ", action=" + action + "]";
    }


}
