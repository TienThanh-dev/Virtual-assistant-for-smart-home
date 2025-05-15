package com.example.appsmartphone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class modeCamera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mode_camera);
        WebView webView = findViewById(R.id.webViewStream);
        ConstraintLayout backBtn1 = findViewById(R.id.backBtn1);
        Button capBtn = findViewById(R.id.capBtn);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                finish();
                // Chỉ hiển thị Toast
                Toast.makeText(getApplicationContext(), "Không thể kết nối đến thiết bị", Toast.LENGTH_LONG).show();
            }
        });
        webView.loadUrl("http://esp32-am.ddns.net");
        backBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        capBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        webView.draw(canvas);
                        saveImageToGallery(bitmap);
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
    private void saveImageToGallery(Bitmap bitmap) {
        String displayName = "screenshot_" + System.currentTimeMillis() + ".jpg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyAppScreenshots");

            ContentResolver resolver = getContentResolver();
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            if (imageUri != null) {
                try (OutputStream fos = resolver.openOutputStream(imageUri)) {
                    if (fos != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        Toast.makeText(this, "Đã lưu ảnh vào thư viện", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("saveImageToGallery", "Không mở được OutputStream");
                        Toast.makeText(this, "Không thể lưu ảnh", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.e("saveImageToGallery", "Lỗi khi lưu ảnh", e);
                    Toast.makeText(this, "Lưu ảnh thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("saveImageToGallery", "imageUri null, không thể lưu ảnh");
                Toast.makeText(this, "Lưu ảnh thất bại", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Android 9 trở xuống
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagesDir, displayName);
            try (OutputStream fos = new FileOutputStream(image)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                // Thêm vào thư viện
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(image));
                sendBroadcast(mediaScanIntent);

                Toast.makeText(this, "Đã lưu ảnh vào thư viện", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("saveImageToGallery", "Lỗi khi lưu ảnh", e);
                Toast.makeText(this, "Lưu ảnh thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
}