
package com.app.letuscs.models.postDetailModel;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPostDetalUser implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("is_verified_email")
    @Expose
    private String isVerifiedEmail;
    @SerializedName("is_verified_contact_no")
    @Expose
    private String isVerifiedContactNo;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("is_password_change_required")
    @Expose
    private String isPasswordChangeRequired;
    @SerializedName("facebook_id")
    @Expose
    private Object facebookId;
    @SerializedName("google_id")
    @Expose
    private Object googleId;
    @SerializedName("continuous_claim_unit")
    @Expose
    private String continuousClaimUnit;
    @SerializedName("last_claim")
    @Expose
    private String lastClaim;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    private final static long serialVersionUID = 7281424521117737169L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsVerifiedEmail() {
        return isVerifiedEmail;
    }

    public void setIsVerifiedEmail(String isVerifiedEmail) {
        this.isVerifiedEmail = isVerifiedEmail;
    }

    public String getIsVerifiedContactNo() {
        return isVerifiedContactNo;
    }

    public void setIsVerifiedContactNo(String isVerifiedContactNo) {
        this.isVerifiedContactNo = isVerifiedContactNo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getIsPasswordChangeRequired() {
        return isPasswordChangeRequired;
    }

    public void setIsPasswordChangeRequired(String isPasswordChangeRequired) {
        this.isPasswordChangeRequired = isPasswordChangeRequired;
    }

    public Object getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Object facebookId) {
        this.facebookId = facebookId;
    }

    public Object getGoogleId() {
        return googleId;
    }

    public void setGoogleId(Object googleId) {
        this.googleId = googleId;
    }

    public String getContinuousClaimUnit() {
        return continuousClaimUnit;
    }

    public void setContinuousClaimUnit(String continuousClaimUnit) {
        this.continuousClaimUnit = continuousClaimUnit;
    }

    public String getLastClaim() {
        return lastClaim;
    }

    public void setLastClaim(String lastClaim) {
        this.lastClaim = lastClaim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}
