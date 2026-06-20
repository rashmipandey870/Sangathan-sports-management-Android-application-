package com.example.sangathanapp.ui.registration;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.example.sangathanapp.R;

public class SportModel implements Parcelable {

    @SerializedName("_id")
    private String mongoId;

    @SerializedName("sportId")
    private int id;
    
    private String name;
    private String description;
    private String date;
    private String venue;
    private String level;

    @SerializedName("maxParticipants")
    private int maxPlayers;

    @SerializedName("participantsCount")
    private int registeredPlayers;

    private int iconResId;

    @SerializedName("iconName")
    private String iconName;

    private int year;
    private String coordinatorName;
    private String coordinatorEmail;
    private String coordinatorPhone;

    public SportModel() {
    }

    public SportModel(int id,
                      String name,
                      String description,
                      String date,
                      String venue,
                      String level,
                      int maxPlayers,
                      int registeredPlayers,
                      int iconResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.venue = venue;
        this.level = level;
        this.maxPlayers = maxPlayers;
        this.registeredPlayers = registeredPlayers;
        this.iconResId = iconResId;
    }

    protected SportModel(Parcel in) {
        mongoId = in.readString();
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        date = in.readString();
        venue = in.readString();
        level = in.readString();
        maxPlayers = in.readInt();
        registeredPlayers = in.readInt();
        iconResId = in.readInt();
        iconName = in.readString();
        year = in.readInt();
        coordinatorName = in.readString();
        coordinatorEmail = in.readString();
        coordinatorPhone = in.readString();
    }

    public static final Creator<SportModel> CREATOR = new Creator<SportModel>() {
        @Override
        public SportModel createFromParcel(Parcel in) {
            return new SportModel(in);
        }

        @Override
        public SportModel[] newArray(int size) {
            return new SportModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mongoId);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(venue);
        dest.writeString(level);
        dest.writeInt(maxPlayers);
        dest.writeInt(registeredPlayers);
        dest.writeInt(iconResId);
        dest.writeString(iconName);
        dest.writeInt(year);
        dest.writeString(coordinatorName);
        dest.writeString(coordinatorEmail);
        dest.writeString(coordinatorPhone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // GETTERS & SETTERS
    public String getMongoId() { return mongoId; }
    public void setMongoId(String mongoId) { this.mongoId = mongoId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public int getRegisteredPlayers() { return registeredPlayers; }
    public void setRegisteredPlayers(int registeredPlayers) { this.registeredPlayers = registeredPlayers; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getCoordinatorName() { return coordinatorName; }
    public void setCoordinatorName(String coordinatorName) { this.coordinatorName = coordinatorName; }

    public String getCoordinatorEmail() { return coordinatorEmail; }
    public void setCoordinatorEmail(String coordinatorEmail) { this.coordinatorEmail = coordinatorEmail; }

    public String getCoordinatorPhone() { return coordinatorPhone; }
    public void setCoordinatorPhone(String coordinatorPhone) { this.coordinatorPhone = coordinatorPhone; }

    public int getIconResId() {
        if (iconResId != 0) return iconResId;
        if (iconName == null) return R.drawable.ic_default;
        
        String nameLower = iconName.toLowerCase();
        if (nameLower.contains("cricket")) {
            return R.drawable.ic_cricket;
        } else if (nameLower.contains("football")) {
            return R.drawable.ic_football;
        } else if (nameLower.contains("badminton")) {
            return R.drawable.ic_badminton;
        } else if (nameLower.contains("volleyball")) {
            return R.drawable.ic_volleyball;
        } else if (nameLower.contains("kabaddi")) {
            return R.drawable.ic_kabaddi;
        } else if (nameLower.contains("carrom") || nameLower.contains("carramboard")) {
            return R.drawable.ic_carramboard;
        } else if (nameLower.contains("basketball")) {
            return R.drawable.ic_basketball;
        } else {
            return R.drawable.ic_default;
        }
    }
    public void setIconResId(int iconResId) { this.iconResId = iconResId; }
}
