package ass.cashorsplash.huawei;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ass.cashorsplash.huawei.Utility.NetworkChangeListener;

public class HomeActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private RewardedAd mRewardedAd;
    private AdRequest adRequest2;
    private InterstitialAd mInterstitialAd;
    private final String TAG = "MainActivity";
    private MediaPlayer mediaPlayer;
    private TextView txt_coin;
    private LoadingDialog loadingDialog;
    private DatabaseReference myRef;
    private Long coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
//        String currentDateandTime = sdf.format(new Date());
//
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//        builder1.setMessage(currentDateandTime);
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton(
//                "Yes",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        builder1.setNegativeButton(
//                "No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alert11 = builder1.create();
//        alert11.show();

        adRequest2 = new AdRequest.Builder().build();
        loadreward();
        loadint();
        loadingDialog = new LoadingDialog(HomeActivity.this);
        txt_coin = findViewById(R.id.textViewCoins);
        loadingDialog.StartLoadingDialog();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        myRef = database.getReference("users").child(Auth.getCurrentUser().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String userdata = snap.getKey();
                    String point = "point";
                    if (userdata.equals(point)) {
                        coins = (Long) snap.getValue();
                        txt_coin.setText(snap.getValue().toString());
                        loadingDialog.dismissDialog();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.dismissDialog();
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    public void spin(View view) {
//        Interstitialads();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    public void dailyCheck(View view) {

    }

    public void small_ads(View view) {
        Interstitialads();
    }

    public void startVideo(View view) {
//        for (int i = 0; i < 4; i++) {
//            System.out.println("vedioooooooooo"+i);
        if (mRewardedAd != null) {

                loadreward();
                Activity activityContext = HomeActivity.this;
                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        System.out.println("you get your reward ");

                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        myRef.child("point").setValue(coins + rewardAmount);
                        Toast.makeText(getApplicationContext(), "Congratulation you received " + rewardAmount + " points", Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();
                    }
                });
            } else{
                Toast.makeText(getApplicationContext(), " There are no ads for now, please try again later", Toast.LENGTH_SHORT).show();
            }
//        }
    }

    public void loadreward() {
//        for(int i=0 ;i<1000;i++) {ca-app-pub-4645474601463767/7699957044
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                        Toast.makeText(HomeActivity.this, "The rewarded ad wasn't ready yet " + loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "onAdFailedToLoad");
                        System.out.println("adload ");
                    }
                });
//        }
    }

    public void loadint() {
//        for (int i = 0; i < 1000; i++) {
        mInterstitialAd = new InterstitialAd(this);
//        ca-app-pub-4645474601463767/7493310588
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void Interstitialads() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            myRef.child("point").setValue(coins + 1);
            loadint();
        } else {
            Toast.makeText(this, "The interstitial wasn't loaded yet", Toast.LENGTH_SHORT).show();
            System.out.println("fail init ");
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();

    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}