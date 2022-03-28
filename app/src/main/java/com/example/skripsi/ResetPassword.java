package com.example.skripsi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPassword extends AppCompatActivity {
    @Nullable
    @BindView(R.id.btn_resetPassword)
    Button btn_resetPassword;
    @BindView(R.id.txt_resetPassword)
    EditText txt_resetPassword;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()==null){
            btn_resetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                String emailReset = txt_resetPassword.getText().toString();
                if (emailReset.equals("")){
                    Toast.makeText(ResetPassword.this, "Masukkan email yang sudah teregistrasi",Toast.LENGTH_SHORT).show();
                } else{
                    mAuth.sendPasswordResetEmail(emailReset).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPassword.this, "Password reset email telah dikirim",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassword.this, LoginActivity.class));
                            } else {
                                Toast.makeText(ResetPassword.this, "Pengiriman email reset password gagal",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                }
            });
        } else {
            mAuth.signOut();
        }
    }
}