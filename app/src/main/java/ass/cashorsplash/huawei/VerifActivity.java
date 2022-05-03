package ass.cashorsplash.huawei;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class VerifActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif);

        loadingDialog = new LoadingDialog(VerifActivity.this);
    }
//h3o^-+oh^-
    public void verif(View view) {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        loadingDialog.StartLoadingDialog();
        Toast.makeText(VerifActivity.this, "Verification " + !Auth.getCurrentUser().isEmailVerified(), Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (Auth.getCurrentUser().isEmailVerified()) {
                    startActivity(new Intent(VerifActivity.this, HomeActivity.class));
                } else {
                    Auth.getCurrentUser().sendEmailVerification();
                }
                loadingDialog.dismissDialog();
            }
        });

    }
}