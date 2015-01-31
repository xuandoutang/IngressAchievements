package com.example.amyu.ingress_achievement_view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final IngressListView ingressListView = (IngressListView) findViewById(R.id.sample1);
        ingressListView.setOnItemClickListener(mOnItemClickListener);

        for (int i = 0; i < 50; i++) {
            AchievementView view = new AchievementView(getApplicationContext());
            view.setAchievementType(AchievementView.GOLD);
            view.setIconRes(R.drawable.sample_icon);
            ingressListView.addView(view);
        }

        ingressListView.setNumColumns(1);

        SeekBar seekBar = (SeekBar) findViewById(R.id.sample2);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ingressListView.setNumColumns(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private IngressListView.OnItemClickListener mOnItemClickListener = new IngressListView.OnItemClickListener() {
        @Override
        public void onItemClick(AchievementView view, int position) {
            Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
            view.setAchievementType(AchievementView.SILVER);
        }
    };

}
