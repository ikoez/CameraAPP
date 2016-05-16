package csy.a3_1002751337;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {

    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        String path="/mnt/sdcard/DCIM/csyCamera";
        File file=new File(path);
        if (file.isDirectory()) {
            listFile = file.listFiles();

            FilePathStrings = new String[listFile.length];

            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {

                FilePathStrings[i] = listFile[i].getAbsolutePath();

                FileNameStrings[i] = listFile[i].getName();
            }
        }


        GridView gv = (GridView)findViewById(R.id.GridView1);
        //为GridView设置适配器
        Display display = getWindowManager().getDefaultDisplay();
        int mScreenHeight= display.getHeight();
        int mScreenWidth = display.getWidth();
        gv.setAdapter(new ImageAdapter(this,  FilePathStrings, FileNameStrings, mScreenWidth, mScreenHeight));
        gv.setOnItemClickListener(new OnItemClickListenerImpl());

    }

    protected void onResume() {
        super.onResume();
        String path="/mnt/sdcard/DCIM/csyCamera";
        File file=new File(path);
        if (file.isDirectory()) {
            listFile = file.listFiles();

            FileNameStrings = new String[listFile.length];
            FilePathStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {

                FilePathStrings[i] = listFile[i].getAbsolutePath();
                FileNameStrings[i] = listFile[i].getName();
            }
        }


        GridView gv = (GridView)findViewById(R.id.GridView1);
        //为GridView设置适配器
        Display display = getWindowManager().getDefaultDisplay();
        int mScreenHeight= display.getHeight();
        int mScreenWidth = display.getWidth();
        gv.setAdapter(new ImageAdapter(this,  FilePathStrings, FileNameStrings, mScreenWidth, mScreenHeight));
        gv.setOnItemClickListener(new OnItemClickListenerImpl());


    }

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            //Toast.makeText(GalleryActivity.this, String.valueOf(position),Toast.LENGTH_SHORT).show();

            Intent i = new Intent(GalleryActivity.this, ViewImage.class);
            // Pass String arrays FilePathStrings
            i.putExtra("filepath", FilePathStrings);
            // Pass String arrays FileNameStrings
            i.putExtra("filename", FileNameStrings);
            // Pass click position
            i.putExtra("position", position);
            startActivity(i);
        }

    }



}
