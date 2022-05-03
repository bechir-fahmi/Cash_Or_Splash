package ass.cashorsplash.huawei;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    TextView txtMoney;
    RelativeLayout relativeLayout;
    Animation txtAnimation, layoutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        txtAnimation = AnimationUtils.loadAnimation(this, R.anim.fall_down);
        layoutAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        txtMoney = findViewById(R.id.txtmoney);
        relativeLayout = findViewById(R.id.relSplash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout.setAnimation(layoutAnimation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtMoney.setVisibility(View.VISIBLE);
                        txtMoney.setAnimation(txtAnimation);
                    }
                }, 900);
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 7000);
    }
}