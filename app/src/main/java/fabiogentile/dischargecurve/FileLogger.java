package fabiogentile.dischargecurve;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Fabio Gentile on 01/07/16!
 */

public class FileLogger extends AsyncTask<Integer, Void, Void> {
    private final String TAG = "LogToFile";

    protected Void doInBackground(Integer... params) {
        int level = params[0];
        int voltage = params[1];
        int temp = params[2];

        File logFile = new File("/storage/extSdCard/batt_stat.log");
        String currDate =
                android.text.format.DateFormat.format("dd/MM/yyyy kk:mm:ss", new java.util.Date()).toString();

        String text = level + " " + voltage + " " + temp / 10;
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

        return null;
    }


}

