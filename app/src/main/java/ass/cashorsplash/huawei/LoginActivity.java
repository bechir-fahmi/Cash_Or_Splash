package ass.cashorsplash.huawei;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ass.cashorsplash.huawei.Utility.NetworkChangeListener;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button btnsingin;
    private TextView txt_signup;
    private FirebaseAuth mFirebaseAuth;
    private LoadingDialog loadingDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edit_mail);
        password = findViewById(R.id.edit_password);
        loadingDialog = new LoadingDialog(LoginActivity.this);

        if (mFirebaseAuth.getCurrentUser() != null) {
            if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                Toast.makeText(LoginActivity.this, "1", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(LoginActivity.this, VerifActivity.class));
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this, "22" + user.getEmail(), Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void login(View view) {
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        if (mail.isEmpty()) {
            email.setError("Please enter your email");
            email.requestFocus();
        } else if (pass.isEmpty()) {
            password.setError("Please enter your password !");
            password.requestFocus();
        } else if (mail.isEmpty() && pass.isEmpty()) {
            Toast.makeText(this, "Fields are empty !", Toast.LENGTH_LONG).show();
        } else if (!(mail.isEmpty() && pass.isEmpty())) {
            loadingDialog.StartLoadingDialog();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            mFirebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (!mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                            mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LoginActivity.this, "Verification Email Sent", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, VerifActivity.class));
                                }
                            });

                        } else {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login in :D " + user.getEmail(), Toast.LENGTH_LONG).show();
                            loadingDialog.dismissDialog();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Error login please try again !", Toast.LENGTH_LONG).show();
                        loadingDialog.dismissDialog();
                    }
                }
            });
        }
    }

    public void insc(View view) {
        startActivity(new Intent(LoginActivity.this, InscriptionActivity.class));
    }

    public void forget(View view) {
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