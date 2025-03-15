package com.u063.minecraftpe;

import com.u063.minecraftpe.server.udpServer;
import com.u063.minecraftpe.server.Num2Byte;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        udpServer udpServer = new udpServer(19132);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    udpServer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        Log.e("num",""+Num2Byte.toByte(0x00000000372cdc9e)[7]);
    }
}