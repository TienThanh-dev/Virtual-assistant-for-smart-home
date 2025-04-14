package com.example.appsmartphone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.appsmartphone.weather.WeatherService;
import com.example.appsmartphone.model.WeatherResponse;
import com.example.appsmartphone.model.Database;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    TextView statusWeather,titleTime,statusTempOutdoor,statusTempRoom,statusAirQuality,
            statusGas,statusAir,statusHumidity,titleAdvice1,statusCity;
    Button statusLamps;
    LocationManager locationManager;
    ConstraintLayout backBtn1,modeCamera;
    String textAdvice="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        statusWeather = findViewById(R.id.statusWeather);
        statusCity= findViewById(R.id.statusCity);
        titleTime = findViewById(R.id.titleTime);
        statusTempOutdoor = findViewById(R.id.statusTempOutdoor);
        statusTempRoom = findViewById(R.id.statusTempRoom);
        statusGas = findViewById(R.id.statusGas);
        statusAirQuality = findViewById(R.id.statusAirQuality);
        statusHumidity = findViewById(R.id.statusHumidity);
        statusAir = findViewById(R.id.statusAir);
        titleAdvice1 = findViewById(R.id.titleAdvice1);
        statusLamps = findViewById(R.id.statusLamps);
        backBtn1 = findViewById(R.id.backBtn1);
        modeCamera = findViewById(R.id.modeCamera);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }else {
            getCurrentLocation();
        }
        readWeatherFromFirebase();
        setCurrentTime();
        backBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });
        modeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, modeCamera.class); // Thay SecondActivity bằng tên Activity bạn muốn chuyển đến
                startActivity(intent);
            }
        });
        statusLamps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, statusLamps.class); // Thay SecondActivity bằng tên Activity bạn muốn chuyển đến
                startActivity(intent);
            }
        });
        if (textAdvice.isEmpty()){
            titleAdvice1.setText("");
        } else{
            titleAdvice1.setText("Không lấy được "+textAdvice);
            textAdvice="";
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void getCurrentLocation(){
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        getWeather(latitude, longitude);
                    } else {
                        Toast.makeText(this, "Không lấy được vị trí", Toast.LENGTH_SHORT).show();
                        textAdvice = textAdvice+" vị trí";
                    }
                });
    }
    private void getWeather(double lat, double lon){
        String apiKey = "0126ab07e562f532be6d54fff9ed0343";
        String units = "metric";
        String lang = "vi";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        service.getCurrentWeather(lat,lon,apiKey,units,lang).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String city = response.body().getName();
                    String desc = response.body().getWeather()[0].getDescription();
                    double temp = response.body().getMain().getTemp();
                    statusCity.setText(city);
                    statusWeather.setText(desc);
                    statusTempOutdoor.setText(temp+"°");
                } else {
                    statusWeather.setText("E");
                    statusTempOutdoor.setText("E °");
                    if (textAdvice.isEmpty()){
                        textAdvice =textAdvice + "dữ liệu thời tiết";
                    } else{
                        textAdvice =textAdvice + ", dữ liệu thời tiết";
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                statusWeather.setText("E");
                statusTempOutdoor.setText("E °");
            }
        });
    }
    private void readWeatherFromFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("ESP32CAM");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Database data = snapshot.getValue(Database.class);
                if (data != null) {
                    double gas = data.getGas();
                    double temp = data.getTemp();
                    double humidity = data.getHumidity();
                    double light =data.getLight();
                    statusGas.setText(gas+"ppm");
                    statusTempRoom.setText(temp+"°");
                    statusHumidity.setText(humidity+"%");

                } else {
                    Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    statusGas.setText("E ppm");
                    statusTempRoom.setText("E °");
                    statusHumidity.setText("E %");
                    if (textAdvice.isEmpty()){
                        textAdvice =textAdvice + "dữ liệu cảm biến";
                    } else{
                        textAdvice =textAdvice + ", dữ liệu cảm biến";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Lỗi đọc Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                statusGas.setText("E ppm");
                statusTempRoom.setText("E °");
                statusHumidity.setText("E %");
            }
        });
    }
    private void setCurrentTime() {
        TextView titleTime = findViewById(R.id.titleTime);

        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Định dạng thời gian theo tiếng Việt
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy | HH:mm", new Locale("vi", "VN"));
        String currentTime = sdf.format(calendar.getTime());

        // Set vào TextView
        titleTime.setText(currentTime);
    }
}