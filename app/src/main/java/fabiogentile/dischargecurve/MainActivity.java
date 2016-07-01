package fabiogentile.dischargecurve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "AppTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creo il file per i log
        File logFile = new File(getString(R.string.LogFileName));
        try {
            if (!logFile.exists()) {
                if (logFile.createNewFile() == false)
                    Log.e(TAG, "impossibile creare il file");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            int scale = -1;
            int level = -1;
            int voltage = -1;
            int temp = -1;

            @Override
            public void onReceive(Context context, Intent intent) {
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                //scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                Log.e(TAG, "level is " + level + ", temp is " + temp + ", voltage is " + voltage);
                appendLog(level + " " + voltage + " " + temp);
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

        Log.i(TAG, "App creata");
    }


    public void appendLog(String text) {
        File logFile = new File(getString(R.string.LogFilePath));
        String currDate =
                android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString();

        //BufferedWriter for performance, true to set append to file flag
        BufferedWriter buf;
        try {
            buf = new BufferedWriter(new FileWriter(logFile, true));
            String strToLog = "[" + currDate + "] " + text;
            buf.append(strToLog);
            buf.newLine();
            buf.flush();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
