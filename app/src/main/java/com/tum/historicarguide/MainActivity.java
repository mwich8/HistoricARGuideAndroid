package com.tum.historicarguide;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity implements CvCameraViewListener2 {

    private final static String TAG = MainActivity.class.getName();

    private static final String AR_IMG_NAME_FELDHERRNHALLE = "Feldherrnhalle.jpg";
    private static final String AR_IMG_NAME_SIEGESTOR = "Siegestor.jpg";

    private String arImageName = AR_IMG_NAME_FELDHERRNHALLE;

    public static final int VIEW_MODE_NATIVEOPTICALDETECTIONDEBUG = 0;
    public static final int VIEW_MODE_NATIVEOPTICALDETECTION = 1;

    private MenuItem mItemPreviewNativeOpticalDetectionDebug;
    private MenuItem mItemPreviewNativeOpticalDetection;

    private CameraBridgeViewBase mOpenCvCameraView;

    public static int viewMode = VIEW_MODE_NATIVEOPTICALDETECTION;

    private TextView textView;
    private boolean textViewVisible = false;

    private int cameraViewWidth;
    private int cameraViewHeight;

    private int leftBorderSize;
    private int topBorderSize;

    float sizeOfInnerRect;
    float innerRectSideLength;

    private Mat mRgba;

    private Mat arImage = new Mat();

    private long lastTime = 1;

    // Declaring native functions
    public native void setupDetection(int innerWidth, int innerHeight, int frameWidth, int frameHeight, long matAddrARImg);
    public native void setSwitchState(boolean switchState);
    public native void nativeOpticalDetectionDebug(long matAddrInner, long matAddrRgba, float fps);
    public native void nativeOpticalDetection(long matAddrInner, long matAddrRgba, float fps);

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemPreviewNativeOpticalDetectionDebug = menu.add("NativeOpticalDetectionDebug");
        mItemPreviewNativeOpticalDetection = menu.add("NativeOpticalDetection");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemPreviewNativeOpticalDetectionDebug) {
            setupDetection((int)innerRectSideLength, (int)innerRectSideLength, cameraViewWidth, cameraViewHeight, arImage.getNativeObjAddr());
            viewMode = VIEW_MODE_NATIVEOPTICALDETECTIONDEBUG;
        }
        else if (item == mItemPreviewNativeOpticalDetection) {
            setupDetection((int)innerRectSideLength, (int)innerRectSideLength, cameraViewWidth, cameraViewHeight, arImage.getNativeObjAddr());
            viewMode = VIEW_MODE_NATIVEOPTICALDETECTION;
        }
        return true;
    }

    public void updateInfoText() {
        String htmlCode = "";
        if (arImageName == AR_IMG_NAME_FELDHERRNHALLE) {
            htmlCode = "<h4>" + "Feldherrnhalle" + "</h4>" +  "<br />" +
                    "<small>" + "<b>Image taken</b>: 9 November 1946" + "</small>" + "<br />" +
                    "<small>" + "<b>Background</b>: Mass rally held by the Communist Party of Germany(KPD)" + "</small>" + "<br />" +
                    "<small>" + "<b>Construction</b>: 1841 - 1844" + "</small>" + "<br />" +
                    "<small>" + "<b>Architect</b>: Friedrich von Gärtner" + "</small>" + "<br />" +
                    "<small>" + "<b>Height x Width</b>: 24 meters x 21 meters" + "</small>" + "<br />" +
                    "<small>" + "<b>Degree of Destruction</b>: Low" + "</small>" + "<br />" +
                    "<small>" + "<b>Restoration</b>: 1950 - 1962" + "</small>" + "<br />" +
                    "<small>" + "<b>Further information</b>: " + "https://de.wikipedia.org/wiki/Feldherrnhalle" + "<br />";
        } else if (arImageName == AR_IMG_NAME_SIEGESTOR) {
            htmlCode = "<h4>" + "Siegestor" + "</h4>" +  "<br />" +
                    "<small>" + "<b>Image taken</b>: End of 1945" + "</small>" + "<br />" +
                    "<small>" + "<b>Image Content</b>: The damaged victory gate without the quadriga 'Bavaria' being pulled by four lions. Furthermore, at restoration the Siegestor was simplified by omitting the two medaillons at the front side." + "</small>" + "<br />" +
                    "<small>" + "<b>Construction</b>: 1844 - 1850" + "</small>" + "<br />" +
                    "<small>" + "<b>Architect</b>: Friedrich von Gärtner" + "</small>" + "<br />" +
                    "<small>" + "<b>Height x Width</b>: 20,7 meters x 24 meters" + "</small>" + "<br />" +
                    "<small>" + "<b>Degree of Destruction</b>: Medium" + "</small>" + "<br />" +
                    "<small>" + "<b>Restoration</b>: 1958  - 1972" + "</small>" + "<br />" +
                    "<small>" + "<b>Further information</b>: " + "https://de.wikipedia.org/wiki/Siegestor" + "<br />";
        }
        textView.setText(Html.fromHtml(htmlCode));
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        // Info Button
        textView = (TextView)findViewById(R.id.text_view);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.border_style);
        textView.setMovementMethod(new ScrollingMovementMethod());
        final ImageButton infoButton =(ImageButton)findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewVisible) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
                textViewVisible = !textViewVisible;
                updateInfoText();
            }
        });
        // Switch
        final Switch arSwitch = (Switch) findViewById(R.id.mySwitch);
        //set the switch to ON
        arSwitch.setChecked(false);
        //attach a listener to check for changes in state
        arSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    setSwitchState(isChecked);
                    arImageName = AR_IMG_NAME_SIEGESTOR;
                }else{
                    setSwitchState(isChecked);
                    arImageName = AR_IMG_NAME_FELDHERRNHALLE;
                }
                changeARImage();
                updateInfoText();

                setupDetection((int)innerRectSideLength, (int)innerRectSideLength, cameraViewWidth, cameraViewHeight, arImage.getNativeObjAddr());
            }
        });
        // Create Toast to advice user
        Toast toast = Toast.makeText(this, "Focus the middle arch", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        // Pre-Select item for debugging/developing
        onOptionsItemSelected(mItemPreviewNativeOpticalDetection);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        // Setup native part
        sizeOfInnerRect = 0.15f;
        innerRectSideLength = sizeOfInnerRect * width;

        leftBorderSize = (int)((width/2) - (innerRectSideLength/2));
        topBorderSize = (int)((height/2) - (innerRectSideLength/2));

        cameraViewHeight = height;
        cameraViewWidth = width;

        changeARImage();

        setupDetection((int)innerRectSideLength, (int)innerRectSideLength, cameraViewWidth, cameraViewHeight, arImage.getNativeObjAddr());
    }

    public void changeARImage() {
        Bitmap loadedBmp;
        InputStream inputStream = null;
        try {
            inputStream = getResources().getAssets().open(arImageName);
            loadedBmp = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            Log.d(TAG, "Found " + arImageName + " image");
            Utils.bitmapToMat(loadedBmp, arImage);
        } catch (IOException e) {
            Log.d(TAG, "Didn't found " + arImageName + " image");
            e.printStackTrace();
        }
    }

    public void onCameraViewStopped() {
        // Explicitly deallocate Mats
        if (mRgba != null)
            mRgba.release();
        mRgba = null;
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();

        Mat rgbaInnerWindow;

        long timeDiff = SystemClock.elapsedRealtime() - lastTime;
        float fps = 1000.0f/timeDiff;

        switch (MainActivity.viewMode) {
            case MainActivity.VIEW_MODE_NATIVEOPTICALDETECTIONDEBUG:
                rgbaInnerWindow = rgba.submat(topBorderSize, cameraViewHeight - topBorderSize, leftBorderSize, cameraViewWidth - leftBorderSize);
                lastTime = SystemClock.elapsedRealtime();
                nativeOpticalDetectionDebug(rgbaInnerWindow.getNativeObjAddr(), rgba.getNativeObjAddr(), fps);
                rgbaInnerWindow.release();
                break;
            case MainActivity.VIEW_MODE_NATIVEOPTICALDETECTION:
                rgbaInnerWindow = rgba.submat(topBorderSize, cameraViewHeight - topBorderSize, leftBorderSize, cameraViewWidth - leftBorderSize);
                lastTime = SystemClock.elapsedRealtime();
                nativeOpticalDetection(rgbaInnerWindow.getNativeObjAddr(), rgba.getNativeObjAddr(), fps);
                rgbaInnerWindow.release();
                break;
        }

        return rgba;
    }

    static {
        if (OpenCVLoader.initDebug()) {
            // Loading shared libraries *.so-files
            System.loadLibrary("nativeOpenCV310Android");
        } else {
            // Report initialization error
        }
    }
}
