package com.example.chefvlogosss;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WatchVlogActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_watch_vlog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        WebView webView = this.findViewById(R.id.youtubeWebView);
        webView.getSettings().setJavaScriptEnabled(true);

        String videoLink = getIntent().getStringExtra("video_link");

        if (videoLink != null) {
            // Például WebView vagy YouTube Player API használata itt
            Log.d("WatchVlogActivity", "Videó link: " + videoLink);
        } else {
            Toast.makeText(this, "Nem érkezett videólink", Toast.LENGTH_SHORT).show();
        }

        String videoId = "";
        assert videoLink != null;
        if (videoLink.contains("youtube.com/watch?v=")) {
            // Hosszú formátum
            videoId = videoLink.substring(videoLink.indexOf("v=") + 2);
            int ampIndex = videoId.indexOf("&");
            if (ampIndex != -1) {
                videoId = videoId.substring(0, ampIndex);
            }

        } else if (videoLink.contains("youtu.be/")) {
            // Rövid formátum
            int start = videoLink.indexOf("youtu.be/") + 9;
            int end = videoLink.indexOf("?", start);
            if (end == -1) {
                videoId = videoLink.substring(start);
            } else {
                videoId = videoLink.substring(start, end);
            }
        }
        videoLink = "https://www.youtube.com/embed/" + videoId;

        webView.getSettings().setJavaScriptEnabled(true);
        //assert videoLink != null;
        //webView.loadUrl(videoLink);

        /*String videoId = "mOx_5-MRlhE";  // Csak a videó ID*/
        String html = "<html><body style='margin:0'>" +
                "<iframe width='100%' height='100%' src='" + videoLink + "' " +
                "frameborder='0' allowfullscreen></iframe>" +
                "</body></html>";

        webView.loadData(html, "text/html", "utf-8");

        FloatingActionButton vissza = findViewById(R.id.visszagomb);

        vissza.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}