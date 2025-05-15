package com.example.appsmartphone;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Main_ForgotPassword extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText emailInput;
    Button resetPass;
    TextView statusCheckEmail;
    ConstraintLayout backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_forgot_password);
        emailInput= findViewById(R.id.loginName);
        resetPass=findViewById(R.id.resetPass);
        statusCheckEmail=findViewById(R.id.statusCheckEmail);
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String email = emailInput.getText().toString().trim();
                if (email.isEmpty()) {
                    statusCheckEmail.setText("");
                }
                if (!isValidEmail(email)) {
                    statusCheckEmail.setText("Email không hợp lệ!");
                    statusCheckEmail.setTextColor(Color.RED);
                }else{
                    statusCheckEmail.setText("Đảm bảo đúng email đã đăng kí!");
                    statusCheckEmail.setTextColor(Color.YELLOW);
                }

            }
        });
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(Main_ForgotPassword.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                }

                if (!isValidEmail(email)) {
                    emailInput.setError("Email không hợp lệ!");
                    statusCheckEmail.setText("Email không hợp lệ!");
                    statusCheckEmail.setTextColor(Color.RED);

                }else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Main_ForgotPassword.this, "Đã gửi email để đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(Main_ForgotPassword.this, "Lỗi: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                }
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}