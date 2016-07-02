package fabiogentile.dischargecurve;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServiceDC extends IntentService {
    private static final String TAG = "ServizioLog";
    BatteryManager mBatteryManager = null;
    private BroadcastReceiver batteryReceiver = null;


    public ServiceDC() {
        super("ServiceDC");
        Log.i(TAG, "ServiceDC: Servizio Creato");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: Servizio Lanciato");

        // Creo il file per i log
        File logFile = new File(getString(R.string.LogFileName));
        try {
            if (!logFile.exists()) {
                if (!logFile.createNewFile())
                    Log.e(TAG, "impossibile creare il file");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creo il receiver delle informazioni
        batteryReceiver = new BroadcastReceiver() {
            int level = -1;
            int voltage = -1;
            int temp = -1;
            int current = 0;

            @Override
            public void onReceive(Context context, Intent intent) {
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);

                try {

                    File currentFile = new File(getString(R.string.CurrentFile));

                    //Controllo che il file della corrente esista
                    if (currentFile.exists()) {
                        FileReader fReader = new FileReader(currentFile);
                        BufferedReader bufferedReader = new BufferedReader(fReader);
                        current = Integer.parseInt(bufferedReader.readLine());
                    } else
                        current = 0;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    current = 0;
                }

                LogToFile(level, temp, voltage, current);
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

        // Se viene chiuso si ricrea automaticamente
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Distruggo il servizio
        Log.i(TAG, "onDestroy: Distruggo il servizio");
        if (batteryReceiver != null)
            unregisterReceiver(batteryReceiver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "onHandleIntent: QUALCOSA");
    }


    private void LogToFile(Integer level, Integer temp, Integer voltage, Integer current) {
        File logFile = new File(getString(R.string.LogFileName));
        String currDate =
                android.text.format.DateFormat.format("dd/MM/yyyy kk:mm:ss", new java.util.Date()).toString();

        String text = level + " " + voltage + " " + ((float) temp / 10.0) + " " + ((float) current / 1000.0);
        Log.i(TAG, "[" + currDate + "] " + text);

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
