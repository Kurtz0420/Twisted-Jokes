package com.redditjokes.twistedjokes.api.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OurJoke {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("custom_ordering")
    @Expose
    private int custom_ordering;

    @SerializedName("about")
    @Expose
    private String about;

    @SerializedName("build_up")
    @Expose
    private String build_up;

    @SerializedName("delivery")
    @Expose
    private String delivery;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("thumb_slug")
    @Expose
    private String thumb_slug;

    @SerializedName("full_slug")
    @Expose
    private String full_slug;

    @SerializedName("gif_slug")
    @Expose
    private String gif_slug;

    @SerializedName("nsfw")
    @Expose
    private boolean nsfw;

    @SerializedName("religious")
    @Expose
    private boolean religious;

    @SerializedName("political")
    @Expose
    private boolean political;

    @SerializedName("thumbs_ups")
    @Expose
    private int thumbs_ups;

    @SerializedName("thumbs_downs")
    @Expose
    private int thumbs_downs;


    @SerializedName("timestamp")
    @Expose
    private String timestamp;


    public OurJoke() {
    }


    public OurJoke(String id, int custom_ordering, String about, String build_up, String delivery, String type, String thumb_slug, String full_slug, String gif_slug, boolean nsfw, boolean religious, boolean political, int thumbs_ups, int thumbs_downs, String timestamp) {
        this.id = id;
        this.custom_ordering = custom_ordering;
        this.about = about;
        this.build_up = build_up;
        this.delivery = delivery;
        this.type = type;
        this.thumb_slug = thumb_slug;
        this.full_slug = full_slug;
        this.gif_slug = gif_slug;
        this.nsfw = nsfw;
        this.religious = religious;
        this.political = political;
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

    public int getCustom_ordering() {
        return custom_ordering;
    }

    public void setCustom_ordering(int custom_ordering) {
        this.custom_ordering = custom_ordering;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public String getThumb_slug() {
        return thumb_slug;
    }

    public void setThumb_slug(String thumb_slug) {
        this.thumb_slug = thumb_slug;
    }

    public String getFull_slug() {
        return full_slug;
    }

    public void setFull_slug(String full_slug) {
        this.full_slug = full_slug;
    }

    public String getGif_slug() {
        return gif_slug;
    }

    public void setGif_slug(String gif_slug) {
        this.gif_slug = gif_slug;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public boolean isReligious() {
        return religious;
    }

    public void setReligious(boolean religious) {
        this.religious = religious;
    }

    public boolean isPolitical() {
        return political;
    }

    public void setPolitical(boolean political) {
        this.political = political;
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
        return "OurJoke{" +
                "id='" + id + '\'' +
                ", custom_ordering=" + custom_ordering +
                ", about='" + about + '\'' +
                ", build_up='" + build_up + '\'' +
                ", delivery='" + delivery + '\'' +
                ", type='" + type + '\'' +
                ", thumb_slug='" + thumb_slug + '\'' +
                ", full_slug='" + full_slug + '\'' +
                ", gif_slug='" + gif_slug + '\'' +
                ", nsfw=" + nsfw +
                ", religious=" + religious +
                ", political=" + political +
                ", thumbs_ups=" + thumbs_ups +
                ", thumbs_downs=" + thumbs_downs +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
