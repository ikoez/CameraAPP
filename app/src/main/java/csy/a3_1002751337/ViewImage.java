package csy.a3_1002751337;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewImage extends AppCompatActivity {

    private String content;
    private File[] filelist;
    private File file;
    private int flag,position;
    private String[] filepath;
    private String[] filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);

        Intent i = getIntent();

        position = i.getExtras().getInt("position");

        filepath = i.getStringArrayExtra("filepath");

        filename = i.getStringArrayExtra("filename");

        TextView text = (TextView) findViewById(R.id.locationtext);

        String locationstring = "No location recorded";
        file=new File("/mnt/sdcard/DCIM/csyLocation");
        String oldgetfile = filename[position];
        String getfile= oldgetfile.replace("jpg","txt");

        flag = -1;
        if (file.exists()) {
            filelist =file.listFiles();
            for(int j=0;j<filelist.length;j++){

                if (filelist[j].getName().toString().equals(getfile)==true)
                    flag=j;
            }
        }

        if (flag>-1) {
            FileInputStream fileInputStream = null;
            try {

                fileInputStream = new FileInputStream(filelist[flag]);
                int len = 0;
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
                while ((len = fileInputStream.read(buffer)) != -1) {
                    byteArrayInputStream.write(buffer, 0, len);
                }

                locationstring = new String(byteArrayInputStream.toByteArray());
                String[] ss = locationstring.split("\r\n");
                locationstring = "Latitude:"+ss[0]+"\r\n"+"Longitude:"+ss[1];
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }



        text.setText(locationstring);
        ImageView imageview = (ImageView) findViewById(R.id.full_image_view);
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        imageview.setImageBitmap(bmp);


        Button delete=(Button)findViewById(R.id.buttondelete);
        delete.setOnClickListener(new ButtonClickListener());

    }

    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.buttondelete) {
                deleteFile(new File(filepath[position]));
                if (flag>-1) {
                    deleteFile(filelist[flag]);
                }
                finish();
            }

        }

    }

    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            }
        }
    }

}
