package com.example.appsmartphone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appsmartphone.model.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class statusLamps extends AppCompatActivity {

    ConstraintLayout lamp1,lamp2,lamp3,lamp4,lamp5,fan1,backBtn;
    ImageView statusLamp1,statusLamp2,statusLamp3,statusLamp4,statusLamp5,statusFan1;
    TextView titleLamp1,titleLamp2,titleLamp3,titleLamp4,titleLamp5,titleFan1,titleDoor;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch statusDoor;
    public boolean Lamp1,Lamp2,Lamp3,Lamp4,Lamp5,Fan1;
    String Door;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_status_lamps);
        lamp1 = findViewById(R.id.lamp1);
        lamp2 = findViewById(R.id.lamp2);
        lamp3 = findViewById(R.id.lamp3);
        lamp4 = findViewById(R.id.lamp4);
        lamp5 = findViewById(R.id.lamp5);
        fan1 = findViewById(R.id.fan1);
        titleLamp1 = findViewById(R.id.titleLamp1);
        titleLamp2 = findViewById(R.id.titleLamp2);
        titleLamp3 = findViewById(R.id.titleLamp3);
        titleLamp4 = findViewById(R.id.titleLamp4);
        titleLamp5 = findViewById(R.id.titleLamp5);
        titleFan1 = findViewById(R.id.titleFan1);
        statusLamp1 = findViewById(R.id.statusLamp1);
        statusLamp2 = findViewById(R.id.statusLamp2);
        statusLamp3 = findViewById(R.id.statusLamp3);
        statusLamp4 = findViewById(R.id.statusLamp4);
        statusLamp5 = findViewById(R.id.statusLamp5);
        statusFan1 = findViewById(R.id.statusFan1);
        backBtn =findViewById(R.id.backBtn1);
        statusDoor=findViewById(R.id.statusDoor);
        titleDoor=findViewById(R.id.titleDoor);

        lamp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushDataToFirebase("livingRoomLight",!Lamp1);
            }
        });
        lamp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushDataToFirebase("masterBedroomLight",!Lamp2);
            }
        });
        lamp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushDataToFirebase("smallBedroomLight",!Lamp3);
            }
        });
        lamp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushDataToFirebase("masterBathroomLight",!Lamp4);
            }
        });
        lamp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushDataToFirebase("publicBathroomLight",!Lamp5);
            }
        });
        fan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushDataToFirebase("ventilation",!Fan1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        statusDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("ESP32CAM");
                    database.child("statusDoor").setValue("open");
                } else {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("ESP32CAM");
                    database.child("statusDoor").setValue("close");
                }
            }
        });
        readDataFromFirebase();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void pushDataToFirebase(String name, boolean status){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("ESP32CAM");
        database.child(name).setValue(status);
    }
    private void readDataFromFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("ESP32CAM");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Database data = snapshot.getValue(Database.class);
                if (data != null) {
                    Lamp1 = data.getLivingRoomLight();
                    Lamp2 = data.getMasterBedroomLight();
                    Lamp3 = data.getSmallBedroomLight();
                    Lamp4 = data.getMasterBathroomLight();
                    Lamp5 = data.getPublicBathroomLight();
                    Fan1 = data.getVentilation();
                    Door = data.getStatusDoor();
                    if (!Lamp1) {
                        statusLamp1.setImageResource(R.drawable.light_off);
                        titleLamp1.setTextColor(getResources().getColor(R. color. white));
                    } else {
                        statusLamp1.setImageResource(R.drawable.light_on);
                        titleLamp1.setTextColor(getResources().getColor(R. color. yellow));
                    }
                    if (!Lamp2) {
                        statusLamp2.setImageResource(R.drawable.light_off);
                        titleLamp2.setTextColor(getResources().getColor(R. color. white));
                    } else {
                        statusLamp2.setImageResource(R.drawable.light_on);
                        titleLamp2.setTextColor(getResources().getColor(R. color. yellow));
                    }
                    if (!Lamp3) {
                        statusLamp3.setImageResource(R.drawable.light_off);
                        titleLamp3.setTextColor(getResources().getColor(R. color. white));
                    } else {
                        statusLamp3.setImageResource(R.drawable.light_on);
                        titleLamp3.setTextColor(getResources().getColor(R. color. yellow));
                    }
                    if (!Lamp4) {
                        statusLamp4.setImageResource(R.drawable.light_off);
                        titleLamp4.setTextColor(getResources().getColor(R. color. white));
                    } else {
                        statusLamp4.setImageResource(R.drawable.light_on);
                        titleLamp4.setTextColor(getResources().getColor(R. color. yellow));
                    }
                    if (!Lamp5) {
                        statusLamp5.setImageResource(R.drawable.light_off);
                        titleLamp5.setTextColor(getResources().getColor(R. color. white));
                    } else {
                        statusLamp5.setImageResource(R.drawable.light_on);
                        titleLamp5.setTextColor(getResources().getColor(R. color. yellow));
                    }
                    if (!Fan1) {
                        statusFan1.setImageResource(R.drawable.fan_off);
                        titleFan1.setTextColor(getResources().getColor(R. color. white));
                    } else {
                        statusFan1.setImageResource(R.drawable.fan_on);
                        titleFan1.setTextColor(getResources().getColor(R. color. yellow));
                    }
                    if(Door.equals("close")){
                        statusDoor.setChecked(false);
                        titleDoor.setText("Chốt cửa đang đóng");
                        titleDoor.setTextColor(getResources().getColor(R. color. white));
                    } else if (Door.equals("open")) {
                        statusDoor.setChecked(true);
                        titleDoor.setText("Chốt cửa đang mở");
                        titleDoor.setTextColor(getResources().getColor(R. color. yellow));
                    }
                } else {
                    Toast.makeText(statusLamps.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(statusLamps.this, "Lỗi đọc Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}