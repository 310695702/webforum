package com.kcbs.webforum.model.pojo;

public class PostImages {
    private Long id;

    private Long postId;

    private String postImage;

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

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage == null ? null : postImage.trim();
    }
}