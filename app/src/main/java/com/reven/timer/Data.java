package com.reven.timer;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 2016/5/23.
 * Data
 */
public class Data {
    public final ObservableField<String> shuffle = new ObservableField<>();
    public final ObservableField<String> timer = new ObservableField<>();
    public final ObservableField<String> pb = new ObservableField<>();
    public final ObservableField<String> ao5 = new ObservableField<>();
    public final ObservableField<String> ao12 = new ObservableField<>();
    public final ObservableBoolean counting = new ObservableBoolean();
    public ArrayList<Integer> scores = new ArrayList<>();
    public String shuffleLast;

    public Data() {
        this.pb.set("PB -");
        this.ao5.set("AO5 -");
        this.ao12.set("AO12 -");
        this.counting.set(false);
    }
}
