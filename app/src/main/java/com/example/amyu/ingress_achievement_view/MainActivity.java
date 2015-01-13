package com.example.amyu.ingress_achievement_view;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AchievementView view1 = (AchievementView) findViewById(R.id.sample1);
        AchievementView view2 = (AchievementView) findViewById(R.id.sample2);
        AchievementView view3 = (AchievementView) findViewById(R.id.sample3);
        AchievementView view4 = (AchievementView) findViewById(R.id.sample4);
        AchievementView view5 = (AchievementView) findViewById(R.id.sample5);

        view1.setAchievementType(AchievementView.BRONZE);
        view2.setAchievementType(AchievementView.SILVER);
        view3.setAchievementType(AchievementView.GOLD);

        view1.setIconResource(R.drawable.sample_icon);
        view2.setIconResource(R.drawable.ic_launcher);
    }


}
