package ass.cashorsplash.huawei;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ass.cashorsplash.huawei.Model.User;
import ass.cashorsplash.huawei.Utility.NetworkChangeListener;

public class InscriptionActivity extends AppCompatActivity {
    private EditText email, password, name, last_name, cin;
    private Button btnsingin;
    private TextView txt_signup;
    FirebaseAuth mFirebaseAuth;
    private LoadingDialog loadingDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        loadingDialog = new LoadingDialog(InscriptionActivity.this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edit_mail);
        password = findViewById(R.id.edit_password);
        name = findViewById(R.id.edit_name);
        last_name = findViewById(R.id.edit_lastname);
        cin = findViewById(R.id.edit_cin);
    }

    public void inscription(View view) {
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String Name = name.getText().toString();
        String Last_name = last_name.getText().toString();
        String CIN = cin.getText().toString();
        if (Name.isEmpty()) {
            name.setError("Please enter your name !");
            name.requestFocus();
        } else if (Last_name.isEmpty()) {
            last_name.setError("Please enter your Last name !");
            last_name.requestFocus();
        } else if (mail.isEmpty()) {
            email.setError("Please enter your email");
            email.requestFocus();
        } else if (pass.isEmpty()) {
            password.setError("Please enter your password !");
            password.requestFocus();
        } else if (CIN.isEmpty()) {
            cin.setError("Please enter your CIN !");
            cin.requestFocus();
        } else {
            final boolean verify = mail.isEmpty() && pass.isEmpty() && Name.isEmpty() && Last_name.isEmpty() && CIN.isEmpty();
            if (verify) {
                Toast.makeText(InscriptionActivity.this, "Fields are empty !", Toast.LENGTH_LONG).show();
            } else if (!(verify)) {
                loadingDialog.StartLoadingDialog();

                mFirebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(InscriptionActivity.this, "Sign up unsuccessful, Please try again", Toast.LENGTH_LONG).show();
                        } else {
                            storeNewUsersData();
                            mFirebaseAuth.getCurrentUser().sendEmailVerification();
                            startActivity(new Intent(InscriptionActivity.this, VerifActivity.class));
                        }
                        loadingDialog.dismissDialog();
                    }
                });

            } else {
                Toast.makeText(InscriptionActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void storeNewUsersData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");
        User user = new User(name.getText().toString(), last_name.getText().toString(), email.getText().toString(), cin.getText().toString(), password.getText().toString(), 0, 0, 0);
        reference.child(mFirebaseAuth.getCurrentUser().getUid()).setValue(user);
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