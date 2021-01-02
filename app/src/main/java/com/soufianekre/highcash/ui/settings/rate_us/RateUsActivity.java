package com.soufianekre.highcash.ui.settings.rate_us;

import com.soufianekre.highcash.R;
import com.soufianekre.highcash.helper.AnimationHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import xyz.klinker.android.floating_tutorial.FloatingTutorialActivity;
import xyz.klinker.android.floating_tutorial.TutorialPage;

public class RateUsActivity extends FloatingTutorialActivity {
    @NotNull
    @Override
    public List<TutorialPage> getPages() {
        List<TutorialPage> pages = new ArrayList<>();
        pages.add(new TutorialPage(this) {
            @Override
            public void initPage() {
                setContentView(R.layout.view_rate_us_first);
                setNextButtonText("Next");
            }

            @Override
            public void animateLayout() {
                AnimationHelper.animateGroup(
                        findViewById(R.id.rate_us_top_text),
                        findViewById(R.id.rate_us_thumb_up_img),
                        findViewById(R.id.rate_us_thumb_down_img)
                );
            }
        });
        pages.add(new TutorialPage(this) {
            @Override
            public void initPage() {
                setContentView(R.layout.view_rate_us_second);
                setNextButtonText("Rate");
            }
            @Override
            public void animateLayout() {

                AnimationHelper.animateGroup(
                        findViewById(R.id.rate_us_two_top_text),
                        findViewById(R.id.rate_us_two_bottom_text)
                );
            }
        });

        return pages;
    }
}
