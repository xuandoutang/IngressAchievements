/*
 *    Copyright (C) 2015 Yuki Mima
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
        ingressListView.showBackground();
        ingressListView.setOnItemClickListener(new IngressListView.OnItemClickListener() {
            @Override
            public void onItemClick(AchievementView view, int position) {
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
                view.setAchievementType(AchievementView.SILVER);
                ingressListView.showBackground();
            }
        });

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


}
