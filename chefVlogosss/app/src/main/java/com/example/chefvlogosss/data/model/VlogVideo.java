package com.example.chefvlogosss.data.model;

import java.util.Objects;

public class VlogVideo {
    private String userId;
    private String link;
    private String title;
    private String vlogId;

    public VlogVideo() {
    }

    public VlogVideo(String userId, String link, String title, String vlogId) {
        this.userId = userId;
        this.link = link;
        this.title = title;
        this.vlogId = vlogId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVlogId() {
        return vlogId;
    }

    public void setVlogId(String vlogId) {
        this.vlogId = vlogId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VlogVideo vlogVideo = (VlogVideo) o;

        if (!Objects.equals(userId, vlogVideo.userId)) return false;
        if (!Objects.equals(link, vlogVideo.link)) return false;
        if (!Objects.equals(title, vlogVideo.title)) return false;
        return Objects.equals(vlogId, vlogVideo.vlogId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, link, title, vlogId);
    }

}
