package com.reven.timer;

import android.databinding.ObservableField;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 2016/5/23.
 * Data
 */
public class Data {
    public final ObservableField<String> shuffle = new ObservableField<>();
    public final ObservableField<String> timer = new ObservableField<>();
    public final ObservableField<String> ao5 = new ObservableField<>();
    public final ObservableField<String> ao12 = new ObservableField<>();
    public final ObservableField<Boolean> timing = new ObservableField<>();
    public ArrayList<Integer> scores = new ArrayList<>();
    public String shuffleLast;

    public Data() {
        this.ao5.set("AO5 -");
        this.ao12.set("AO12 -");
        this.timing.set(false);
    }
}
