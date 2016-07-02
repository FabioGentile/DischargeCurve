package fabiogentile.dischargecurve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainWindows";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: App creata");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent service = new Intent(getApplicationContext(), ServiceDC.class);
        this.startService(service);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: chiudo l'applicazione");
    }
}
