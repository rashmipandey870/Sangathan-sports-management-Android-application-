package com.example.sangathanapp.ui.registration;

import android.os.Parcel;
import android.os.Parcelable;

public class SportModel implements Parcelable {

    private int id;
    private String name;
    private String description;
    private String date;
    private String venue;

    private String level;
    private int maxPlayers;
    private int registeredPlayers;
    private int iconResId;


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
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        date = in.readString();
        venue = in.readString();
        level = in.readString();
        maxPlayers = in.readInt();
        registeredPlayers = in.readInt();
        iconResId = in.readInt();
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(venue);
        dest.writeString(level);
        dest.writeInt(maxPlayers);
        dest.writeInt(registeredPlayers);
        dest.writeInt(iconResId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // GETTERS
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getVenue() { return venue; }
    public String getLevel() { return level; }
    public int getMaxPlayers() { return maxPlayers; }
    public int getRegisteredPlayers() { return registeredPlayers; }
    public int getIconResId() { return iconResId; }
}
