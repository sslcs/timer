package com.reven.timer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.reven.timer.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    final static String[] STEPS = new String[]{"R", "U", "F", "L", "B", "D"};
    private Data data;
    private Handler handler = new Handler();
    private int timer = 0;
    private int pb = -1;
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
            sb.append(time % 100 / (data.counting.get() ? 10 : 1));
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
        int count = 0;
        int lastIndex = -1;
        int append;
        int index;
        StringBuilder sb = new StringBuilder();
        while (count < 24) {
            index = (int) Math.floor(Math.random() * 6);
            while (index == lastIndex) {
                index = (int) Math.floor(Math.random() * 6);
            }
            lastIndex = index;
            sb.append(STEPS[index]);
            count += 1;

            append = (int) Math.floor(Math.random() * 3);
            if (append == 1) {
                sb.append("' ");
            } else if (append == 0) {
                sb.append(" ");
            } else if (count != 24) {
                sb.append("2");
                count += 1;
                if (count == 13) {
                    sb.append("\n");
                } else {
                    sb.append(" ");
                }
            }

            if (count == 12) {
                sb.append("\n");
            }
        }
        data.shuffle.set(sb.toString());
    }

    public void onClickNext(View view) {
        shuffle();
    }

    public void onClickStart(View view) {
        if (data.counting.get()) {
            return;
        }

        data.shuffleLast = data.shuffle.get();
        data.counting.set(true);
        timer = 0;
        handler.post(runnable);
    }

    public void onClickStop(View view) {
        if (!data.counting.get()) {
            return;
        }

        data.counting.set(false);
        data.scores.add(timer);
        if(pb==-1||timer<pb)
        {
            pb = timer;
            data.pb.set("PB " + getTimer(timer));
        }

        if (data.scores.size() > 4) {
            data.ao5.set("AO5 " + getTimer(average(5)));
        }
        if (data.scores.size() > 11) {
            data.ao12.set("AO12 " + getTimer(average(12)));
        }

        handler.removeCallbacks(runnable);
        updateTimer();
        shuffle();
    }
}
