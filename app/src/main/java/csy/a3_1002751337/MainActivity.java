package csy.a3_1002751337;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERN_STPRAGE=1;
    private static String[]PERMISSION_STORAGE={
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.INTERNET

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestpermission(MainActivity.this);
        Button camera = (Button)findViewById(R.id.cameramode);
        Button gallery = (Button)findViewById(R.id.gallerymode);



        camera.setOnClickListener(new ButtonClickListener());
        gallery.setOnClickListener(new ButtonClickListener());

    }

    class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.cameramode){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
            if(v.getId() == R.id.gallerymode) {
                String path="/mnt/sdcard/DCIM/csyCamera";
                boolean havepicture=false;
                File file=new File(path);
                if (file.exists()){
                    File[] listFile = file.listFiles();
                    if (listFile.length>0){
                        havepicture=true;
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, GalleryActivity.class);
                        startActivity(intent);
                    }
                }
               if (!havepicture)
                    Toast.makeText(MainActivity.this, "There is no picture yet", Toast.LENGTH_SHORT).show();


            }
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    public static void requestpermission(Activity activity){
        int permission= ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERN_STPRAGE

            );
        }
    }

}
