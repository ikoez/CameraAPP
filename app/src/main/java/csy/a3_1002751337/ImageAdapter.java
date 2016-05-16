package csy.a3_1002751337;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by SEC on 2016/2/1.
 */

public class ImageAdapter extends BaseAdapter{

    private Context context;
    private int mScreenHeight;
    private int mScreenWidth;
    private String[] filepath;
    private String[] filename;
    private File[] filelist;
    private File file;
    private int flag;
    private static LayoutInflater inflater = null;



    ImageAdapter(Context context, String[] fpath,  String[] fname, int width, int height){
        this.context = context;
        mScreenWidth = width;
        mScreenHeight = height;
        filepath = fpath;
        filename = fname;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public int getCount() {
        return filepath.length;
    }

    public Object getItem(int item) {
        return item;
    }

    public long getItemId(int id) {
        return id;
    }


    public View getView(int position, View convertView, ViewGroup parent) {


        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.gridview_item, null);

        TextView text = (TextView) vi.findViewById(R.id.text);

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
                locationstring = "Lat:"+ss[0].substring(0,5)+";Long:"+ss[1].substring(0,6);
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


        ImageView imageView = (ImageView) vi.findViewById(R.id.image);
            LayoutParams para = imageView.getLayoutParams();
            para.height = mScreenHeight/5;//一屏幕显示8行
            para.width = (mScreenWidth-20)/3;//一屏显示两列
            imageView.setLayoutParams(para);
            //imageView.setLayoutParams(new GridView.LayoutParams((mScreenWidth-20)/4, mScreenHeight/5 ));//设置ImageView对象布局
        //    imageView.setAdjustViewBounds(false);//设置边界对齐
        //    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
        //    imageView.setPadding(8, 8, 8, 8);//设置间距


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize=4;
       // options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filepath[position],options);

        imageView.setImageBitmap(bitmap);
      //  imageView.setImageResource(mps[position]);//为ImageView设置图片资源
        return vi;
    }



}

