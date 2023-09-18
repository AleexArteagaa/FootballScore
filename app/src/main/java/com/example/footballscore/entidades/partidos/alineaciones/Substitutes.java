package com.example.footballscore.entidades.partidos.alineaciones;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Substitutes implements Parcelable, Serializable {

    private Player player;
    private int bench;
    private int in;
    private int out;

    public Substitutes(Player player, int bench, int in, int out) {
        this.player = player;
        this.bench = bench;
        this.in = in;
        this.out = out;
    }

    protected Substitutes(Parcel in) {
        this.player = in.readParcelable(Player.class.getClassLoader());
        this.bench = in.readInt();
        this.in = in.readInt();
        this.out = in.readInt();
    }

    public static final Creator<Substitutes> CREATOR = new Creator<Substitutes>() {
        @Override
        public Substitutes createFromParcel(Parcel in) {
            return new Substitutes(in);
        }

        @Override
        public Substitutes[] newArray(int size) {
            return new Substitutes[size];
        }
    };

    public int getBench() {
        return bench;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) player, flags);
        dest.writeInt(bench);
        dest.writeInt(in);
        dest.writeInt(out);
    }
}
