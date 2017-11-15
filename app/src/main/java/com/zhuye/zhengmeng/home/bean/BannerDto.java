package com.zhuye.zhengmeng.home.bean;

/**
 * Created by XY on 2016/9/17.
 */
public class BannerDto {

    private int id;
    private String imageUrl;
    private String bannerTitle;


    public BannerDto(String bannerTitle, String imageUrl, int id, int courseId) {
        this.bannerTitle = bannerTitle;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

}
