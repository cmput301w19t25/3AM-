package comnickdchee.github.a3am.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class of LatLng that gets the
 * that allows us to pull information directly from Firebase.
 */
public class PickupCoords implements Parcelable {
    private Double latitude;
    private Double longitude;

    public PickupCoords() {}

    public PickupCoords(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected PickupCoords(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<PickupCoords> CREATOR = new Creator<PickupCoords>() {
        @Override
        public PickupCoords createFromParcel(Parcel in) {
            return new PickupCoords(in);
        }

        @Override
        public PickupCoords[] newArray(int size) {
            return new PickupCoords[size];
        }
    };

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
