package com.skyncalci.Floating;


import static com.skyncalci.Floating.App.CHANNEL_ID;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.floating_bubble.Content;
import com.example.floating_bubble.HoverMenu;
import com.example.floating_bubble.HoverView;
import com.example.floating_bubble.window.HoverMenuService;
import com.skyncalci.R;

import java.util.Collections;
import java.util.List;

public class SimpleHoverMenu extends HoverMenuService {

    private static final String TAG = "SingleSectionHoverMenuService";

    @Override
    protected int getForegroundNotificationId() {
        return 1000;
    }

    @Nullable
    @Override
    protected Notification getForegroundNotification() {
        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("SKY!N Calci Floating Mode")
                .setContentText("Floating Mode is running in a foreground.")
                .build();

        startForeground(1,notification);
        return notification;
    }

    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        hoverView.setMenu(createHoverMenu());
        hoverView.collapse();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hoverView.expand();
            }
        }, 100);
    }

    @NonNull
    private HoverMenu createHoverMenu() {
        return new SingleSectionHoverMenu(getApplicationContext());
    }

    private static class SingleSectionHoverMenu extends HoverMenu {

        private Context mContext;
        private Section mSection,mSection2;

        private SingleSectionHoverMenu(@NonNull Context context) {
            mContext = context;

            mSection = new Section(
                    new SectionId("1"),
                    createTabView(),
                    createScreen()
            );
        }

        private View createTabView() {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.mipmap.ic_launcher_foreground);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            return imageView;
        }

        private Content createScreen() {
            return new SimpleHoverMenuScreen(mContext, "SKY!N Floating Mode");
        }

        @Override
        public String getId() {
            return "singlesectionmenu_foreground";
        }

        @Override
        public int getSectionCount() {
            return 1;
        }

        @Nullable
        @Override
        public Section getSection(int index) {
            if (0 == index) {
                return mSection;
            } else {
                return null;
            }
        }

        @Nullable
        @Override
        public Section getSection(@NonNull SectionId sectionId) {
            if (sectionId.equals(mSection.getId())) {
                return mSection;
            } else {
                return null;
            }
        }

        @NonNull
        @Override
        public List<Section> getSections() {
            return Collections.singletonList(mSection);
        }
    }
}