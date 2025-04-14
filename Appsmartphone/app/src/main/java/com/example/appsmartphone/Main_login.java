package com.example.appsmartphone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main_login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_login);

        mAuth = FirebaseAuth.getInstance();

        EditText emailInput= findViewById(R.id.loginName);
        EditText passInput=findViewById(R.id.password);
        Button SignInBtn= findViewById(R.id.signInBtn);

        SignInBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Main_login.this, "Điền đầy đủ email và mật khẩu!", Toast.LENGTH_SHORT).show();
            } else {
                signInUser(email, password);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, chuyển sang màn hình khác
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Intent intent = new Intent(Main_login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(Main_login.this, "Email hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}