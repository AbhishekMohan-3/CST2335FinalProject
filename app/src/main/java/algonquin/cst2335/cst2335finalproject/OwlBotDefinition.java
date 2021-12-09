package algonquin.cst2335.finalprojectassignment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OwlBotDefinition implements Parcelable {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("definition")
    @Expose
    private String definition;
    @SerializedName("example")
    @Expose
    private String example;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("emoji")
    @Expose
    private String emoji;

    private long id;

    protected OwlBotDefinition(Parcel in) {
        type = in.readString();
        definition = in.readString();
        example = in.readString();
        imageUrl = in.readString();
        emoji = in.readString();
        id = in.readLong();
    }

    public static final Creator<OwlBotDefinition> CREATOR = new Creator<OwlBotDefinition>() {
        @Override
        public OwlBotDefinition createFromParcel(Parcel in) {
            return new OwlBotDefinition(in);
        }

        @Override
        public OwlBotDefinition[] newArray(int size) {
            return new OwlBotDefinition[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(definition);
        parcel.writeString(example);
        parcel.writeString(imageUrl);
        parcel.writeString(emoji);
        parcel.writeLong(id);
    }
}

