
package com.app.letuscs.models.postsModel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPosts implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("likes_count")
    @Expose
    private String likesCount;
    @SerializedName("likes_status")
    @Expose
    private Boolean likesStatus;
    @SerializedName("comment_count")
    @Expose
    private String commentCount;
    @SerializedName("image")
    @Expose
    private List<ModelPostsImage> image = null;
    @SerializedName("comment")
    @Expose
    private List<ModelPostsComment> comment = null;
    @SerializedName("user")
    @Expose
    private ModelPostsUser_ user;
    @SerializedName("likes")
    @Expose
    private List<ModelPostsLike> likes = null;

    private boolean isLiked;

    private final static long serialVersionUID = -5914398991582245171L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public Boolean getLikesStatus() {
        return likesStatus;
    }

    public void setLikesStatus(Boolean likesStatus) {
        this.likesStatus = likesStatus;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public List<ModelPostsImage> getImage() {
        return image;
    }

    public void setImage(List<ModelPostsImage> image) {
        this.image = image;
    }

    public List<ModelPostsComment> getComment() {
        return comment;
    }

    public void setComment(List<ModelPostsComment> comment) {
        this.comment = comment;
    }

    public ModelPostsUser_ getUser() {
        return user;
    }

    public void setUser(ModelPostsUser_ user) {
        this.user = user;
    }

    public List<ModelPostsLike> getLikes() {
        return likes;
    }

    public void setLikes(List<ModelPostsLike> likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
