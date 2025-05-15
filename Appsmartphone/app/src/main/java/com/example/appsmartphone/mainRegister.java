package com.example.appsmartphone;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class mainRegister extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText emailInput,checkPassInput,passInput;
    TextView statusPass;
    Button SignUpBtn;
    ConstraintLayout backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_register);

        emailInput= findViewById(R.id.newLoginName);
        passInput=findViewById(R.id.newPassword);
        checkPassInput=findViewById(R.id.checkPassword);
        statusPass = findViewById(R.id.statusCheckpass);
        SignUpBtn= findViewById(R.id.registerBtn);
        backBtn=findViewById(R.id.backBtn);
        mAuth = FirebaseAuth.getInstance();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkPassInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                checkPass();
            }
        });
        SignUpBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passInput.getText().toString().trim();
            String confirmPass = checkPassInput.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Điền đầy đủ email và mật khẩu!", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                passInput.setText("");
                checkPassInput.setText("");
            } else {
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Lỗi: " + Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void checkPass(){
        String password = passInput.getText().toString().trim();
        String confirmPass = checkPassInput.getText().toString().trim();
        if (!confirmPass.isEmpty()) {
            if (password.equals(confirmPass)) {
                statusPass.setText("Mật khẩu trùng khớp");
                statusPass.setTextColor(Color.GREEN);
            } else {
                statusPass.setText("Mật khẩu không khớp");
                statusPass.setTextColor(Color.RED);
            }
        } else {
            statusPass.setText("");
        }
    }
}