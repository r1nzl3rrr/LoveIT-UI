package com.group03.loveit.models.post;

public class PostFilter {
    private Long userId;
    private Long genderId;
    private String keyword;
    private Long prefGenderId;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getGenderId() {
        return genderId;
    }
    public void setGenderId(Long genderId) {
        this.genderId = genderId;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Long getPrefGenderId() {
        return prefGenderId;
    }
    public void setPrefGenderId(Long prefGenderId) {
        this.prefGenderId = prefGenderId;
    }
}
