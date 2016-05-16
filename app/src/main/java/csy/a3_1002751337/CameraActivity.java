package csy.a3_1002751337;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.Manifest;

public class CameraActivity extends Activity implements SensorListener {

    private Camera mCamera;
    private CameraPreview mPreview;
    private boolean safeToTakePicture = false;
    private FrameLayout preview;
    private int nowdegree;

    private SensorManager sensorManager;
    private long lastSense, lastCapture = -1;
    private float x, y, z;
    private float lastx, lasty, lastz;
    private Sensor sensor1;

    private LocationManager locationManager;
    private String locationProvider;
    private boolean haveProvider,havelocation;
    private Location latestLocation;
    private String timeStamp;


    /*   @Override
    public Object onRetainNonConfigurationInstance() {
        final MyDataObject data = collectMyLoadedData();
        return data;
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

/*
        final MyDataObject data = (MyDataObject) getLastNonConfigurationInstance();
        if (data == null) {
            data = loadMyData();
        }
*/
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (sensor1 != null) {
            sensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        }


        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        mCamera.setDisplayOrientation(90);
        nowdegree=90;



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        haveProvider = true;
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
          //  Toast.makeText(this, "Network provider", Toast.LENGTH_SHORT).show();
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
          //  Toast.makeText(this, "GPS provider", Toast.LENGTH_SHORT).show();
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(this, "No location provider", Toast.LENGTH_SHORT).show();
            haveProvider = false;
        }
//        locationManager.setTestProviderEnabled("gps",true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (haveProvider) {
            latestLocation = locationManager.getLastKnownLocation(locationProvider);
            locationManager.requestLocationUpdates(locationProvider, 100, 0, locationListener);
        }

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile();
            //     Toast.makeText(CameraActivity.this, "File", Toast.LENGTH_LONG).show();
            if (pictureFile == null) {

                return;
            }
            data = imagerotation(data).toByteArray() ;
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    };

    public ByteArrayOutputStream imagerotation(byte[] data)
    {
        Matrix matrix=new Matrix();
        matrix.postRotate(nowdegree);
        Bitmap bitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos;
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    /** Create a File for saving an image or video */
    private File getOutputMediaFile() {


        File mediaStorageDir = new File("/mnt/sdcard/DCIM/csyCamera");
        //Environment.getExternalStoragePublicDirectory(
        //   Environment.DIRECTORY_PICTURES), "MyCameraApp");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        //timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }


    protected void onResume() {
        super.onResume();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (sensor1 != null) {
            sensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        }

        if(mCamera==null) {
            mCamera = getCameraInstance();
            //mCamera.unlock();
            //mCamera.startPreview();
            mPreview = new CameraPreview(this, mCamera);
            preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            mCamera.setDisplayOrientation(90);
            nowdegree=90;

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null) locationManager.requestLocationUpdates(locationProvider, 100, 0, locationListener);

    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
        if (sensorManager != null) {
            sensorManager.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
            sensorManager = null;
        }
        if (locationManager != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }

    }

    private void releaseCamera() {
        if (mCamera != null) {
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.stopPreview();
      //      mPreview=null;
            mCamera.release();        // release the camera for other applications
            mCamera = null;
            preview.removeView(mPreview);
        }
    }


    public void onAccuracyChanged(int arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    public void onSensorChanged(int sensor, float[] dData) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long now = System.currentTimeMillis();
            if ((now - lastSense) <= 100) return;
            if ((now - lastCapture) > 3000) {
                long delta = (now - lastSense);
                lastSense = now;

                x = dData[SensorManager.DATA_X];
                y = dData[SensorManager.DATA_Y];
                z = dData[SensorManager.DATA_Z];
                float deltaX = x - lastx;
                float deltaY = y - lasty;
                float deltaZ = z - lastz;
                double vv = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / delta * 10000;
                if (vv > 2000) {
                    Toast.makeText(CameraActivity.this, "Taking picture in 1s", Toast.LENGTH_LONG).show();
                    //sensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
                    lastCapture = System.currentTimeMillis();
                    SystemClock.sleep(1000);
                    //  mCamera.startPreview();
                    timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    mCamera.takePicture(null, null, mPicture);
                    havelocation=true;
                    recordlocation();
                    Toast.makeText(CameraActivity.this, "Saved Picture", Toast.LENGTH_LONG).show();
                    if (!havelocation)
                        Toast.makeText(this, "No location available, Please check GPS and Network", Toast.LENGTH_SHORT).show();
                    // sensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER);
                }
                lastx = x;
                lasty = y;
                lastz = z;
            }
        }
    }

    public void recordlocation() {
        /*
          if (locationManager != null) {
              if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
              }
              //latestLocation = locationManager.getLastKnownLocation(locationProvider);
              //locationManager.requestLocationUpdates(locationProvider, 300, 1, locationListener);
          }
            */
          if (latestLocation != null) {
              String locationStr = latestLocation.getLatitude() + "\r\n" + latestLocation.getLongitude() + "\r\n";
              File locationPath = new File("/mnt/sdcard/DCIM/csyLocation");

              if (!locationPath.exists()) {
                  if (!locationPath.mkdirs()) {
                      Toast.makeText(this, "Inavailable path", Toast.LENGTH_SHORT).show();
                      return;
                  }
              }

            //  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
              File locationFile;
              locationFile = new File(locationPath.getPath() + File.separator +"IMG_" + timeStamp + ".txt");
              if (locationFile == null) {

                  return;
              }

              try {
                  FileOutputStream fos = new FileOutputStream(locationFile);
                  fos.write(locationStr.getBytes());
                  fos.close();
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }


          } else {
              havelocation = false;
          }


    }

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {

            latestLocation = location;
        }
    };

}


