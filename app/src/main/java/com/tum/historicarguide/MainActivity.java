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

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
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

    // private int activeCanny = 2;
    private int windowSize = 4;     // 1 (progress) + 3 (fixed int)

    private int cameraViewWidth;
    private int cameraViewHeight;

    private int currentInnerWindowWidth;
    private int currentInnerWindowHeight;

    private int displayWidth;
    private int displayHeight;

    private int leftBorderSize;
    private int topBorderSize;

    private Mat mRgba;

    private Mat arImage = new Mat();

    private long lastTime = 1;

    // Declaring native functions
    public native void setupDetection(int width, int height, long matAddrARImg);
    public native void updateSettings(int width, int height);
    public native void setSwitchState(boolean switchState);
    public native void nativeOpticalDetectionDebug(long matAddrRgba, float fps);
    public native void nativeOpticalDetection(long matAddrRgba, float fps);
    public native void nativeSetTouchPos(int xCoord, int yCoord);

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
            float borderStrength = 1f / (float)windowSize;

            int left = (int)(cameraViewWidth * borderStrength);
            int top = (int)(cameraViewHeight * borderStrength);

            leftBorderSize = left;
            topBorderSize = top;

            setupDetection(cameraViewWidth - (2 * left), cameraViewHeight - (2 * top), arImage.getNativeObjAddr());
            viewMode = VIEW_MODE_NATIVEOPTICALDETECTIONDEBUG;
        }
        else if (item == mItemPreviewNativeOpticalDetection) {
            float borderStrength = 1f / (float)windowSize;

            int left = (int)(cameraViewWidth * borderStrength);
            int top = (int)(cameraViewHeight * borderStrength);

            leftBorderSize = left;
            topBorderSize = top;

            setupDetection(cameraViewWidth - (2 * left), cameraViewHeight - (2 * top), arImage.getNativeObjAddr());
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

                setupDetection(cameraViewWidth - (2 * leftBorderSize), cameraViewHeight - (2 * topBorderSize), arImage.getNativeObjAddr());
            }
        });
        // Touch event
        final View touchView = findViewById(R.id.HelloOpenCvView);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float tempLeft = (displayWidth - currentInnerWindowWidth)/2;
                float tempTop = (int)(((displayHeight - currentInnerWindowHeight)/2)*0.75f);
                if (event.getX() > tempLeft && event.getX() < tempLeft + currentInnerWindowWidth) {
                    if (event.getY() > tempTop && event.getY() < tempTop + currentInnerWindowHeight) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            // Calc relative coordinates
                            int relativePixelPosX = (int)(event.getX() - tempLeft);
                            int relativePixelPosY =  (int)(event.getY() - tempTop);
                            nativeSetTouchPos(relativePixelPosX, relativePixelPosY);
                        }
                    }
                }
                return true;
            }
        });
        // Get Display Resolution
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        displayWidth = metrics.widthPixels;
        displayHeight = metrics.heightPixels;

        // Manage seekbar for changing inner size
        final SeekBar seekBarWindowSize;
        seekBarWindowSize = (SeekBar) findViewById(R.id.seekBarWindowSize);
        seekBarWindowSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int thresholdValue, boolean fromUser) {
                windowSize = thresholdValue + 3;

                float borderStrength = 1f / (float)windowSize;

                int left = (int)(cameraViewWidth * borderStrength);
                int top = (int)(cameraViewHeight * borderStrength);

                leftBorderSize = left;
                topBorderSize = top;

                currentInnerWindowWidth = cameraViewWidth - (2 * left);
                currentInnerWindowHeight = cameraViewHeight - (2 * top);

                updateSettings(currentInnerWindowWidth, currentInnerWindowHeight);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
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
        final Mat mRgba = new Mat();

        // Setup native part
        float borderStrength = 1f / (float)windowSize;

        int left = (int)(width * borderStrength);
        int top = (int)(height * borderStrength);

        leftBorderSize = left;
        topBorderSize = top;

        cameraViewHeight = height;
        cameraViewWidth = width;

        changeARImage();

        setupDetection(cameraViewWidth - (2 * leftBorderSize), cameraViewHeight - (2 * topBorderSize), arImage.getNativeObjAddr());
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
        Size sizeRgba = rgba.size();

        Mat rgbaInnerWindow;

        int rows = (int) sizeRgba.height;
        int cols = (int) sizeRgba.width;

        float borderStrength = 1f / (float)windowSize;

        int left = (int)(cols * borderStrength);
        int top = (int)(rows * borderStrength);

        leftBorderSize = left;
        topBorderSize = top;

        currentInnerWindowWidth = cameraViewWidth - (2 * left);
        currentInnerWindowHeight = cameraViewHeight - (2 * top);

        switch (MainActivity.viewMode) {
            case MainActivity.VIEW_MODE_NATIVEOPTICALDETECTIONDEBUG:
                rgbaInnerWindow = rgba.submat(top, rows - top, left, cols - left);
                long timeDiff = SystemClock.elapsedRealtime() - lastTime;
                float fps = 1000.0f/timeDiff;
                lastTime = SystemClock.elapsedRealtime();
                nativeOpticalDetectionDebug(rgbaInnerWindow.getNativeObjAddr(), fps);
                rgbaInnerWindow.release();
                break;
            case MainActivity.VIEW_MODE_NATIVEOPTICALDETECTION:
                rgbaInnerWindow = rgba.submat(top, rows - top, left, cols - left);
                long timeDiffRelease = SystemClock.elapsedRealtime() - lastTime;
                float fpsRelease = 1000.0f/timeDiffRelease;
                lastTime = SystemClock.elapsedRealtime();
                nativeOpticalDetection(rgbaInnerWindow.getNativeObjAddr(), fpsRelease);
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
