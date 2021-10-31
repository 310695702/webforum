package com.kcbs.webforum.model.pojo;

import java.io.Serializable;

public class Banner implements Serializable {
    private Long id;

    private Long postId;

    private String recommendImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getRecommendImage() {
        return recommendImage;
    }

    public void setRecommendImage(String recommendImage) {
        this.recommendImage = recommendImage == null ? null : recommendImage.trim();
    }
}