package com.reven.timer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.reven.timer.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final static String[] STEPS = new String[]{"R ", "R'", "U ", "U'", "F ", "F'", "L ", "L'", "B ", "B'", "D ", "D'"};
    private Data data;
    private Handler handler = new Handler();
    private int timer = 0;
    private Runnable runnable = new Runnable() {
        public void run() {
            timer += 1;
            // update every 100 milliseconds
            if (timer % 10 == 0) updateTimer();

            handler.postDelayed(this, 10);
        }
    };

    private void updateTimer() {
        data.timer.set(getTimer(timer));
    }

    private String getTimer(int time) {
        StringBuilder sb = new StringBuilder();
        if (time < 100) {
            sb.append("0.");
        }
        appendTimer(sb, time);
        return sb.toString();
    }

    private void appendTimer(StringBuilder sb, int time) {
        if (time < 100) {
            sb.append(time % 100 / (data.timing.get() ? 10 : 1));
        } else if (time < 60 * 100) {
            sb.append(time / 100).append(".");
            appendTimer(sb, time % 100);
        } else if (time < 60 * 60 * 100) {
            sb.append(time / (60 * 100)).append(":");
            appendTimer(sb, time % (60 * 100));
        } else {
            sb.append(time / (60 * 60 * 100)).append(":");
            appendTimer(sb, time % (60 * 60 * 100));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        data = new Data();
        shuffle();
        binding.setData(data);
    }

    private int average(int amount) {
        int size = data.scores.size();
        if (size < amount) {
            return 0;
        }

        ArrayList<Integer> last = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            last.add(data.scores.get(size - amount + i));
        }
        Collections.sort(last);
        int sum = 0;
        for (int i = 1; i < amount - 1; i++) {
            sum += last.get(i);
        }
        return sum / (amount - 2);
    }

    public void onClickLast(View view) {
        if (TextUtils.isEmpty(data.shuffleLast)) {
            return;
        }

        if (data.shuffle.get().equals(data.shuffleLast)) {
            return;
        }
        data.shuffle.set(data.shuffleLast);
    }

    private void shuffle() {
        List<String> asList = Arrays.asList(STEPS);
        ArrayList<String> shuffle = new ArrayList<>(asList);
        for (int i = 0; i < 12; i++) {
            int index = (int) Math.floor(Math.random() * 12);
            shuffle.add(STEPS[index]);
        }
        Collections.shuffle(shuffle);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            String step = shuffle.get(i);
            sb.append(step).append(" ");
            if (i == 11) {
                sb.append("\n");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        data.shuffle.set(sb.toString());
    }

    public void onClickNext(View view) {
        shuffle();
    }

    public void onClickStart(View view) {
        if (data.timing.get()) {
            return;
        }

        data.shuffleLast = data.shuffle.get();
        data.timing.set(true);
        timer = 0;
        handler.post(runnable);
    }

    public void onClickStop(View view) {
        if (!data.timing.get()) {
            return;
        }

        data.timing.set(false);
        data.scores.add(timer);

        if (data.scores.size() > 6) {
            data.ao5.set("AO5 " + getTimer(average(7)));
        }
        if (data.scores.size() > 13) {
            data.ao12.set("AO12 " + getTimer(average(14)));
        }

        handler.removeCallbacks(runnable);
        updateTimer();
        shuffle();
    }
}
