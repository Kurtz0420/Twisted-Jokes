package com.redditjokes.twistedjokes.api.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserJoke {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("build_up")
    @Expose
    private String build_up;

    @SerializedName("delivery")
    @Expose
    private String delivery;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("slug")
    @Expose
    private String slug;


    @SerializedName("thumbs_ups")
    @Expose
    private int thumbs_ups;

    @SerializedName("thumbs_downs")
    @Expose
    private int thumbs_downs;


    @SerializedName("timestamp")
    @Expose
    private String timestamp;


    public UserJoke() {
    }



    public UserJoke(String id, String username, String email, String build_up, String delivery, String type, String slug, int thumbs_ups, int thumbs_downs, String timestamp) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.build_up = build_up;
        this.delivery = delivery;
        this.type = type;
        this.slug = slug;
        this.thumbs_ups = thumbs_ups;
        this.thumbs_downs = thumbs_downs;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBuild_up() {
        return build_up;
    }

    public void setBuild_up(String build_up) {
        this.build_up = build_up;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getThumbs_ups() {
        return thumbs_ups;
    }

    public void setThumbs_ups(int thumbs_ups) {
        this.thumbs_ups = thumbs_ups;
    }

    public int getThumbs_downs() {
        return thumbs_downs;
    }

    public void setThumbs_downs(int thumbs_downs) {
        this.thumbs_downs = thumbs_downs;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserJoke{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", build_up='" + build_up + '\'' +
                ", delivery='" + delivery + '\'' +
                ", type='" + type + '\'' +
                ", slug='" + slug + '\'' +
                ", thumbs_ups=" + thumbs_ups +
                ", thumbs_downs=" + thumbs_downs +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
