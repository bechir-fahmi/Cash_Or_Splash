package ass.cashorsplash.huawei;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ass.cashorsplash.huawei.Utility.NetworkChangeListener;

public class MainActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private RewardedAd mRewardedAd;
    private final String TAG = "MainActivity";
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest2;
    private DatabaseReference myRef, myRefMoney;
    private LuckyWheel lw;
    //    private Long win, lose;
    List<WheelItem> wheelItems;
    Button showVideoAdBtn;
    int[] winArray = {1, 3, 1, 3, 3, 1, 1, 1, 3, 3, 1, 3, 1, 3, 1, 1, 3, 3, 3, 5};
    int[] loseArray = {2, 4, 2, 4, 4, 2, 4, 6};
    private int all_win;
    private int all_lose;
    private Boolean win_lose;
    //    int i, j = 0;
    int roll_number = 0;
    int nbr_games = 1;
    private EditText edit_amount;
    private TextView balnce;
    private Long money;
    int solde;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        myRef = database.getReference("users").child(Auth.getCurrentUser().getUid());
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snap : snapshot.getChildren()) {
//                    String userdata = snap.getKey();
//                    String nbr_win = "nbr_win";
//                    String nbr_lose = "nbr_lose";
//                    if (userdata.equals(nbr_win)) {
//                        win = (Long) snap.getValue();
//                    }
//                    if (userdata.equals(nbr_lose)) {
//                        lose = (Long) snap.getValue();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        generateWheelItems();
        balnce = findViewById(R.id.txt_balnce);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String userdata = snap.getKey();
                    String point = "point";
                    if (userdata.equals(point)) {
                        money = (Long) snap.getValue();
                        balnce.clearComposingText();
                        balnce.setText(snap.getValue().toString() + " Dt");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        edit_amount = findViewById(R.id.amount);
        Button start = findViewById(R.id.start);
        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int star, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int star, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    start.setEnabled(false);
                } else {
                    start.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lw = findViewById(R.id.lwv);
        lw.addWheelItems(wheelItems);
        lw.setTarget(3);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        myRefMoney = database.getReference("users").child(Auth.getCurrentUser().getUid());
        myRefMoney.keepSynced(true);

        lw.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                start.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Target Reached", Toast.LENGTH_LONG).show();
                if (win_lose) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("cong you win " + roll_number);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("oooooh nooo you lose ☺ " + roll_number);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                balnce.setVisibility(View.VISIBLE);
            }
        });


        loadData();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solde = Integer.parseInt(edit_amount.getText().toString());
                int bal = Integer.parseInt(String.valueOf(money));
                if (solde <= bal) {
                    start.setVisibility(View.INVISIBLE);
                    balnce.setVisibility(View.INVISIBLE);
                    loadData();

                    if (all_win - all_lose == 0) {
                        roll_number = loseArray[getRandomNumber(8)];
//                        j = (j >= 7) ? 0 : j + 1;
                        win_lose = false;
                        myRefMoney.child("money_win").setValue(all_win + solde);
                        myRefMoney.child("point").setValue(bal - (solde));
//                        myRefMoney.child("nbr_game").setValue(nbr_games++);
                    } else if (all_win < (all_lose * 15) / 10) {
                        roll_number = loseArray[getRandomNumber(8)];
//                        j = (j >= 7) ? 0 : j + 1;
                        win_lose = false;
                        myRefMoney.child("money_win").setValue(all_win + solde);
                        myRefMoney.child("point").setValue(bal - (solde));
//                        myRefMoney.child("nbr_game").setValue(nbr_games++);
                    } else if (all_win >= (all_lose + all_lose * 0.5)) {
//                        System.out.println(getRandomNumber(7));
                        roll_number = winArray[getRandomNumber(20)];
//                        i = (i >= 6) ? 0 : i + 1;
                        win_lose = true;
                        switch (roll_number) {
                            case 1:
                            case 3:
                                myRefMoney.child("money_lose").setValue(all_lose + (solde * 2));
                                myRefMoney.child("point").setValue(bal + (solde * 2));
                                break;
                            case 5:
                                myRefMoney.child("money_lose").setValue(all_lose + (solde * 10));
                                myRefMoney.child("point").setValue(bal + (solde * 10));
                                break;
                        }

//                        myRefMoney.child("nbr_game").setValue(nbr_games++);
                    }
                    lw.rotateWheelTo(roll_number);
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("ins balnce ");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
    }

    private void loadData() {
        myRefMoney.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String userdata = snap.getKey();
                    String money_win = "money_win";
                    if (userdata.equals(money_win)) {
                        Long all_wins = (Long) snap.getValue();
                        all_win = all_wins.intValue();
                    }
                    String money_lose = "money_lose";
                    if (userdata.equals(money_lose)) {
                        Long all_loses = (Long) snap.getValue();
                        all_lose = all_loses.intValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void generateWheelItems() {
        wheelItems = new ArrayList<>();
        wheelItems.add(new WheelItem(Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(getResources(),
                R.drawable.win), "*2 "));
        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
                R.drawable.lose), "*0 "));
        wheelItems.add(new WheelItem(Color.parseColor("#F00E6F"), BitmapFactory.decodeResource(getResources(),
                R.drawable.win), "*2 "));
        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
                R.drawable.lose), "*0 "));
        wheelItems.add(new WheelItem(Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(getResources(),
                R.drawable.win), "*10 "));
        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
                R.drawable.lose), "*0"));
    }

    private int getRandomNumber(int i) {
        int rnd = new Random().nextInt(1000);
        int winner = 0;
        switch (i) {
            case 8:
                if (rnd != 8) {
                    winner = new Random().nextInt(7);
                } else winner = 7;
                break;
            case 20:
                if (rnd != 20) {
                    winner = new Random().nextInt(19);
                } else winner = 19;
                break;
        }
        System.out.println("winner =====" + winner);
        return (winner);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}