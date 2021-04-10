package com.asuka.domain;

public class Image {
    private String userName;
    private String fileName;

    public Image(String userName, String fileName) {
        this.userName = userName;
        this.fileName = fileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Image{" +
                "userName='" + userName + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
