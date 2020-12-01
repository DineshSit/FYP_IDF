package com.navigine.naviginedemo;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.se.omapi.Session;
import android.view.*;
import android.view.View.*;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.*;
import android.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.io.*;
import java.lang.*;
import java.util.*;

import com.google.firebase.database.DatabaseReference;
import com.navigine.naviginesdk.*;

//class FusedLocationProviderClient extends GoogleApi<Api.ApiOptions.NoOptions> {
//  protected FusedLocationProviderClient(@NonNull Context context, Api<Api.ApiOptions.NoOptions> api, Looper looper) {
//    super(context, api, looper);
//
//  }
//}

//public abstract class DoubleClickListener implements OnClickListener {
//
//  // The time in which the second tap should be done in order to qualify as
//  // a double click
//  private static final long DEFAULT_QUALIFICATION_SPAN = 200;
//  private long doubleClickQualificationSpanInMillis;
//  private long timestampLastClick;
//
//  public DoubleClickListener() {
//    doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN;
//    timestampLastClick = 0;
//  }
//
//  public DoubleClickListener(long doubleClickQualificationSpanInMillis) {
//    this.doubleClickQualificationSpanInMillis = doubleClickQualificationSpanInMillis;
//    timestampLastClick = 0;
//  }


  public class MainActivity<searchBtn> extends Activity {

      private int numberforfun;

    private static final String TAG = "NAVIGINE.Demo";
    private static final String NOTIFICATION_CHANNEL = "NAVIGINE_DEMO_NOTIFICATION_CHANNEL";
    private static final int UPDATE_TIMEOUT = 100;  // milliseconds
    private static final int ADJUST_TIMEOUT = 5000; // milliseconds
    private static final int ERROR_MESSAGE_TIMEOUT = 5000; // milliseconds
    private static final boolean ORIENTATION_ENABLED = true; // Show device orientation?
    private static final boolean NOTIFICATIONS_ENABLED = true; // Show zone notifications?
    MyDbAdapter mydb;
    // NavigationThread instance
    private NavigationThread mNavigation = null;

    // UI Parameters
    private LocationView mLocationView = null;
    private Button mPrevFloorButton = null;
    private Button mNextFloorButton = null;
    Button searchBtn;
    private Spinner LVenue = null;
    Button mGotoLocation;
    String text1 = null;
    String text2 = null;
    String cat = null;
    TextView textView5;
    List<String> VenueList = new ArrayList<String>();

    public static String floorpreferences = "FloorPref";
      SharedPreferences sharedpreferences;


    private View mBackView = null;
    private View mNextRoute = null;
    private View mMap = null;
    private View mPrevFloorView = null;
    private View mNextFloorView = null;
    private View mZoomInView = null;
    private View mZoomOutView = null;
    private View mAdjustModeView = null;
    private TextView mCurrentFloorLabel = null;
    private TextView mErrorMessageLabel = null;
    TextView txtLocal;
    private Handler mHandler = new Handler();
    private float mDisplayDensity = 0.0f;

    private boolean mAdjustMode = false;
    private long mAdjustTime = 0;
    private int CAMERA_PERMISSION=1;

    // Location parameters
    private Location mLocation = null;
    private int mCurrentSubLocationIndex = -1;

    // Device parameters
    private DeviceInfo mDeviceInfo = null; // Current device
    private LocationPoint mPinPoint = null; // Potential device target
    private LocationPoint mTargetPoint = null; // Current device target
    private RectF mPinPointRect = null;

    private Bitmap mVenueBitmap = null;
    private Venue mTargetVenue = null;
    private Venue mSelectedVenue = null;
    private RectF mSelectedVenueRect = null;
    private Zone mSelectedZone = null;

//    protected String latitude, longitude;
//    protected LocationManager locationManager;
//    protected LocationListener locationListener;

    //qr code variable
      Button mActivateScanner,mTest,mActivateHelp;


//  private FusedLocationProviderClient fusedLocationClient;


    // My codes


    @Override
    protected void onCreate(Bundle savedInstanceState) {//textView5=(TextView) findViewById(R.id.tvLocation);


      // String Location= com.navigine.naviginesdk.Location.class.getName();
      cat = getIntent().getStringExtra("cat");

        Log.d(TAG, "MainActivity started");

      //Codes for Venue List retrieve
        sharedpreferences = getSharedPreferences(floorpreferences, Context.MODE_PRIVATE);
//get Qr Code Data
        SharedPreferences sp=getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
        String QrData=sp.getString("QrData","");
        //Debuging Message can be removed
        Log.d(TAG, "QrCode data"+QrData);
//    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

      super.onCreate(savedInstanceState);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.activity_main);



      //Guest mode
        final String Guest=sp.getString("Guest","");
      //Used  to test feature

      mTest=findViewById(R.id.btnTest);
//mTest.setOnClickListener(new OnClickListener() {
//    @Override
//    public void onClick(View v) {
// private void handleLongClick(float x, float y) {
//      Log.d(TAG, String.format(Locale.ENGLISH, "Long click at (%.2f, %.2f)", x, y));
//      makePin(mLocationView.getAbsCoordinates(x, y));
        //text = "L306";//week9


//        return;

//}
//});
mActivateScanner=findViewById(R.id.btnActivateScanner);
mActivateScanner.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
        startActivity(new Intent(getApplicationContext(),QrScanner.class));}
        else{
requestCameraPermission();

        }
    }
});


        mActivateHelp = findViewById(R.id.guide);
        mActivateHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView debug = (TextView) findViewById(R.id.debuglocate);
                debug.setText("detected");
                startActivity(new Intent(getApplicationContext(),InfoGuide.class));
            }
        });

      //My codes for search list

      mydb = new MyDbAdapter(this);
      mGotoLocation = findViewById(R.id.btnGotoLocation);
      mGotoLocation.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: Guest "+Guest);
            if(Guest.equals("True"))
            {


                registerUser();
              
            }
            else{
                Log.d(TAG, "Redirect to meetup");
          startActivity(new Intent(getApplicationContext(), FindLocation.class));}
        }
      });

      searchBtn = findViewById(R.id.btnGoToSearch);
      searchBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            //create sublocation index parseing
            SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("Floor",mCurrentSubLocationIndex);
            editor.apply();

          startActivity(new Intent(getApplicationContext(), SearchPage.class));
        }
      });

/*
    LVenue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: "+VenueList.get(position) ,Toast.LENGTH_SHORT).show();
      }
    });
*/


      //Ends here
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
              WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

      // Setting up GUI parameters

      mBackView = (View) findViewById(R.id.navigation__back_view);
      mNextRoute = (View) findViewById(R.id.Next_destination);
      mMap = (View) findViewById(R.id.navigation__map_view);
      mPrevFloorButton = (Button) findViewById(R.id.navigation__prev_floor_button);
      mNextFloorButton = (Button) findViewById(R.id.navigation__next_floor_button);
      mPrevFloorView = (View) findViewById(R.id.navigation__prev_floor_view);
      mNextFloorView = (View) findViewById(R.id.navigation__next_floor_view);
      mCurrentFloorLabel = (TextView) findViewById(R.id.navigation__current_floor_label);
      mZoomInView = (View) findViewById(R.id.navigation__zoom_in_view);
      mZoomOutView = (View) findViewById(R.id.navigation__zoom_out_view);
      mAdjustModeView = (View) findViewById(R.id.navigation__adjust_mode_view);
      mErrorMessageLabel = (TextView) findViewById(R.id.navigation__error_message_label);


      mBackView.setVisibility(View.INVISIBLE);
      mNextRoute.setVisibility(View.INVISIBLE);
      mMap.setVisibility(View.INVISIBLE);
      mPrevFloorView.setVisibility(View.INVISIBLE);
      mNextFloorView.setVisibility(View.INVISIBLE);
      mCurrentFloorLabel.setVisibility(View.INVISIBLE);
      mZoomInView.setVisibility(View.INVISIBLE);
      mZoomOutView.setVisibility(View.INVISIBLE);
      mAdjustModeView.setVisibility(View.INVISIBLE);
      mErrorMessageLabel.setVisibility(View.GONE);


      mVenueBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.elm_venue);

      //ImageButton imgButton = (ImageButton) findViewById(R.id.navigation__location_view);

      //Create Location pinning here. Yongkai's code
//    mVenueBitmap.setListener
//            (
//                    new Bitmap.Listener() {
//                      @Override
//                      public void onClick(float x, float y) {
//                        handleClick(x, y);
//                      }
//
//                    }
//            );


      // Initializing location view
      mLocationView = (LocationView) findViewById(R.id.navigation__location_view);
      mLocationView.setBackgroundResource(R.drawable.elm_splash);



      mLocationView.setListener
              (
                      new LocationView.Listener() {


                        @Override
                        public void onClick(float x, float y) {
                          handleClick(x, y);
                        }

                        //create ondouleClick (Yongkai)

                        @Override
                        public void onDoubleClick(float x, float y) {
                            int a = Math.round(x);
                            int b = Math.round(y);
                          handleDoubleClick(a, b);
                          //on double click create pin
//                          ImageView imageView = new ImageView(MainActivity.this);
//                          imageView.setBackgroundResource(R.drawable.pin);
//
//                          addView(imageView, 40, 40);

//                          super.onDoubleClick(x, y);
                        }

//                        public void addView(ImageView imageView, int width, int height) {
//                          RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
//                          layoutParams.setMargins(0, 0, 0, 0);
//
//                          imageView.setLayoutParams(layoutParams);
//                          mLocationView.addView(imageView);
//
//
//                        }

                        @Override
                        public void onLongClick(float x, float y) {
                          handleLongClick(x, y);
                        }

                        @Override
                        public void onScroll(float x, float y, boolean byTouchEvent) {
                          handleScroll(x, y, byTouchEvent);
                        }

                        @Override
                        public void onZoom(float ratio, boolean byTouchEvent) {
                          handleZoom(ratio, byTouchEvent);
                        }

                        @Override
                        public void onDraw(Canvas canvas) {

                          drawZones(canvas);
                          drawPoints(canvas);
                          drawVenues(canvas);
                          drawDevice(canvas);


                        }
                      }
              );

      // Loading map only when location view size is known
      mLocationView.addOnLayoutChangeListener
              (
                      new OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                          int width = right - left;
                          int height = bottom - top;
                          if (width == 0 || height == 0)
                            return;

                          Log.d(TAG, "Layout chaged: " + width + "x" + height);

                          int oldWidth = oldRight - oldLeft;
                          int oldHeight = oldBottom - oldTop;
                          if (oldWidth != width || oldHeight != height)
                            loadMap();

                        }
                      }
              );

      mDisplayDensity = getResources().getDisplayMetrics().density;
      mNavigation = NavigineSDK.getNavigation();

      // Setting up device listener
      if (mNavigation != null) {
        mNavigation.setDeviceListener
                (
                        new DeviceInfo.Listener() {
                          @Override
                          public void onUpdate(DeviceInfo info) {
                            handleDeviceUpdate(info);
                          }
                        }
                );
      }

    /*// Setting up zone listener
    if (mNavigation != null)
    {
      mNavigation.setZoneListener
      (
        new Zone.Listener()
        {
          @RequiresApi(api = Build.VERSION_CODES.O)
          @Override public void onEnterZone(Zone z) { handleEnterZone(z); }
          //@Override public void onLeaveZone(Zone z) { handleLeaveZone(z); }
        }
      );
    }*/

      if (NOTIFICATIONS_ENABLED) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26)
          notificationManager.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL, "default",
                  NotificationManager.IMPORTANCE_LOW));
      }


    }
//end of oncreaete
    @Override
    public void onDestroy() {
      if (mNavigation != null) {
        NavigineSDK.finish();
        mNavigation = null;
      }

      super.onDestroy();
    }

    @Override
    public void onBackPressed() {
      moveTaskToBack(true);
    }


    //Rotation feature
      public void turnMap(View v){
        if (ORIENTATION_ENABLED){
//            Animation an = new RotateAnimation(0.0f, 90.0f, 45,45);
//            an.setDuration(0);
//            mLocationView.setAnimation(an);

//            RotateAnimation rotate = new RotateAnimation(0f, mLocationView.getRotation()+90,
//                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            mLocationView.startAnimation(rotate);
//            rotate.setFillAfter(true);

            View mainLayout = findViewById(R.id.navigation__location_view);
            int w = mainLayout.getWidth();
            int h = mainLayout.getHeight();

            mainLayout.setRotation(mainLayout.getRotation()+90);

            //zoomed rotation
//            mainLayout.setTranslationX((w - h) / 2);
//            mainLayout.setTranslationY((h - w) / 2);
//
//            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mainLayout.getLayoutParams();
//            lp.height = w;
//            lp.width = h;
//            mainLayout.requestLayout();

//            mLocationView.setRotation(mDeviceInfo.getAzimuth());
            TextView debug = (TextView) findViewById(R.id.debuglocate);
//            debug.setText((int) mDeviceInfo.getAzimuth());


          }
      }

//      private void updateCameraBearing(GoogleMap googleMap, float bearing) {
//          if ( googleMap == null) return;
//          CameraPosition camPos = CameraPosition
//                  .builder(
//                          googleMap.getCameraPosition() // current Camera
//                  )
//                  .bearing(bearing)
//                  .build();
//          googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
//      }

//      protected void animateToLocation(Location position)
//      {
////          if (mGoogleMap == null || position == null)
////              return;
////
////          float zoomLevel = mGoogleMap.getCameraPosition().zoom != mGoogleMap.getMinZoomLevel() ? mGoogleMap.getCameraPosition().zoom : 18.0F;
////          LatLng latLng = new LatLng(position.getLatitude(), position.getLongitude());
////          CameraPosition cameraPosition = new CameraPosition.Builder()
////                  .target(latLng)
////                  .bearing(position.getBearing())
////                  .zoom(zoomLevel)
////                  .build();
////
////          mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
//      }

    //Refresh locator
    public void toggleAdjustMode(View v) {
      mAdjustMode = !mAdjustMode;
      mAdjustTime = 0;
      Button adjustModeButton = (Button) findViewById(R.id.btnTest);
      adjustModeButton.setBackgroundResource(mAdjustMode ?
              R.drawable.btn_adjust_mode_on :
              R.drawable.btn_adjust_mode_off);
      mLocationView.redraw();
    }

    public void onNextFloor(View v) {
      if (loadNextSubLocation())
        mAdjustTime = System.currentTimeMillis() + ADJUST_TIMEOUT;
    }

    public void onPrevFloor(View v) {
      if (loadPrevSubLocation())
        mAdjustTime = System.currentTimeMillis() + ADJUST_TIMEOUT;
    }

    public void onZoomIn(View v) {
      mLocationView.zoomBy(1.25f);
        int pin_imageview_id = 123;
        try {
            View pinView = findViewById(pin_imageview_id);

            pinView.setScaleX(pinView.getScaleX()-0.025f);
            pinView.setScaleY(pinView.getScaleY()-0.025f);

            TextView debug = (TextView) findViewById(R.id.debuglocate);


            if (pinView.getScaleY() <= 0.62f){
                pinView.setScaleX(pinView.getScaleX()+0.02f);
                pinView.setScaleY(pinView.getScaleY()+0.02f);
//                pinView.setY(pinView.getY()/1.05f);
//                pinView.setX(pinView.getX()/1.025f);
            }

            if(pinView.getScaleX() == 1 || pinView.getScaleY() == 1) {
                debug.setText("not changed");
            }
            else {
                debug.setText("changed");
            }
//            pinView.setY(pinView.getY()*1.05f);
//            pinView.setX(pinView.getX()*1.025f);

            //create statement to change plot xy
        }

        catch (Exception e){

        }


    }

    //alternative setonTouch to zoom for pin
//    imageView.setOnTouchListener(new MoveViewTouchListener(imageView));

    public void onZoomOut(View v) {
      mLocationView.zoomBy(0.8f);
        int pin_imageview_id = 123;
        try {
            View pinView = findViewById(pin_imageview_id);
            pinView.setScaleX(pinView.getScaleX() + 0.02f);
            pinView.setScaleY(pinView.getScaleY() + 0.02f);
            TextView debug = (TextView) findViewById(R.id.debuglocate);


            if (pinView.getScaleY() >= 1.0f) {
                pinView.setScaleX(pinView.getScaleX() - 0.025f);
                pinView.setScaleY(pinView.getScaleY() - 0.025f);
//                pinView.setY(pinView.getY()*1.05f);
//                pinView.setX(pinView.getX()*1.025f);
            }

            if (pinView.getScaleX() == 1 || pinView.getScaleY() == 1) {
                debug.setText("not changed");
            } else {
                debug.setText("changed");
            }
//            pinView.setY(pinView.getY()/1.05f);
//            pinView.setX(pinView.getX()/1.025f);

            //create statement to change plot xy
        }
        catch (Exception e){

        }

    }


    public void onMakeRoute(View v) {
      if (mNavigation == null)
        return;

      if (mPinPoint == null)
        return;

      mTargetPoint = mPinPoint;
      mTargetVenue = null;
      mPinPoint = null;
      mPinPointRect = null;

      mNavigation.setTarget(mTargetPoint);
      mBackView.setVisibility(View.VISIBLE);
      mLocationView.redraw();
    }

    public void onCancelRoute(View v) {
      if (mNavigation == null)
        return;

      mTargetPoint = null;
      mTargetVenue = null;
      mPinPoint = null;
      mPinPointRect = null;

      mNavigation.cancelTargets();
      mBackView.setVisibility(View.GONE);
      mLocationView.redraw();

    }

    private void handleClick(float x, float y) {
      Log.d(TAG, String.format(Locale.ENGLISH, "Click at (%.2f, %.2f)", x, y));
      TextView debug = (TextView) findViewById(R.id.debuglocate);
      debug.setText("Clicked");

      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return;

      SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);

      if (subLoc == null)
        return;

      if (mPinPoint != null) {
        if (mPinPointRect != null && mPinPointRect.contains(x, y)) {
          mTargetPoint = mPinPoint;
          mTargetVenue = null;
          mPinPoint = null;
          mPinPointRect = null;
          mNavigation.setTarget(mTargetPoint);
          mBackView.setVisibility(View.VISIBLE);
          return;
        }
        cancelPin();
        return;
      }

      if (mSelectedVenue != null) {
        if (mSelectedVenueRect != null && mSelectedVenueRect.contains(x, y)) {
          mTargetVenue = mSelectedVenue;
          mTargetPoint = null;
          mNavigation.setTarget(new LocationPoint(mLocation.getId(), subLoc.getId(), mTargetVenue.getX(), mTargetVenue.getY()));
          mBackView.setVisibility(View.VISIBLE);
        }
        cancelVenue();
        return;
      }

      // Check if we touched venue
      mSelectedVenue = getVenueAt(x, y);
      mSelectedVenueRect = new RectF();

      // Check if we touched zone
      if (mSelectedVenue == null) {
        Zone Z = getZoneAt(x, y);
        if (Z != null)
          mSelectedZone = (mSelectedZone == Z) ? null : Z;
      }

      mLocationView.redraw();
    }

    private void handleLongClick(float x, float y) {
      Log.d(TAG, String.format(Locale.ENGLISH, "Long click at (%.2f, %.2f)", x, y));
      makePin(mLocationView.getAbsCoordinates(x, y));
      TextView debug = (TextView) findViewById(R.id.debuglocate);
      debug.setText("Hold");
      cancelVenue();
    }

    private void handleScroll(float x, float y, boolean byTouchEvent) {
      if (byTouchEvent)
        mAdjustTime = NavigineSDK.currentTimeMillis() + ADJUST_TIMEOUT;
    }

    private void handleZoom(float ratio, boolean byTouchEvent) {
      if (byTouchEvent)
        mAdjustTime = NavigineSDK.currentTimeMillis() + ADJUST_TIMEOUT;
    }

    //yongkai code
    public void addView(ImageView imageView, int myX, int myY) {
      RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(70, 70);
//      AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams)imageView.getLayoutParams();
//      layoutParams.setMargins(0, 0, 0, 0);

        if (myX >= 0 && myY >= 0) {
            layoutParams.setMargins(myX,myY,0,0);
        }

        else {
          TextView debug = (TextView) findViewById(R.id.debuglocate);
          debug.setText("ERROR. please feedback and wait for new update.");
        }


//      layoutParams.x = myX;
//      layoutParams.y = myY;

      imageView.setLayoutParams(layoutParams);

//    View locationnew = findViewById(navigation__location_view);

      mLocationView.addView(imageView);

      int pin_imageview_id = 123;

      ImageView pinned = imageView;
      imageView.setId(pin_imageview_id);



      // code handle zoom and scroll using method invoke listener
//      if (RunOnce) {
//
//        }

    }

    private void handleDoubleClick(int x, int y) {


        makePin(mLocationView.getAbsCoordinates(x, y));
//        cancelPin();



//        TextView debug = (TextView) findViewById(R.id.debuglocate);
//        debug.setText("Pinned");
//
//        ImageView imageView = new ImageView( MainActivity.this);
//        imageView.setBackgroundResource(R.drawable.pin);
//
//
//
//        int pin_imageview_id = 123;
//
//        try {
//            View pinView = findViewById(pin_imageview_id);
//            if (pinView != null) {
//                debug.setText("Unpinned");
//                ((ViewGroup) pinView.getParent()).removeView(pinView);
//            }
//            else {
//                addView(imageView, x, y);
//            }
//        }catch (Exception e){
//
//        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleEnterZone(Zone z) {
      Log.d(TAG, "Entered zone " + z.getName());
      if (NOTIFICATIONS_ENABLED) {
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.putExtra("zone_id", z.getId());
        notificationIntent.putExtra("zone_name", z.getName());
        notificationIntent.putExtra("zone_color", z.getColor());
        notificationIntent.putExtra("zone_alias", z.getAlias());

        // Setting up a notification
        Notification.Builder notificationBuilder = new Notification.Builder(this, NOTIFICATION_CHANNEL);
        notificationBuilder.setContentIntent(PendingIntent.getActivity(this, z.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        notificationBuilder.setContentTitle("New zone");
        notificationBuilder.setContentText("You have entered zone '" + z.getName() + "'");
        notificationBuilder.setSmallIcon(R.drawable.elm_logo);
        notificationBuilder.setAutoCancel(true);

        // Posting a notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(z.getId(), notificationBuilder.build());
      }
    }


    private void handleLeaveZone(Zone z) {
      Log.d(TAG, "Left zone " + z.getName());
      if (NOTIFICATIONS_ENABLED) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(z.getId());
      }
    }

    private void handleDeviceUpdate(DeviceInfo deviceInfo) {
      mDeviceInfo = deviceInfo;
      if (mDeviceInfo == null)
        return;

      // Check if location is loaded
      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return;

      if (mDeviceInfo.isValid()) {
        cancelErrorMessage();
        mBackView.setVisibility(mTargetPoint != null || mTargetVenue != null ?
                View.VISIBLE : View.GONE);
        if (mAdjustMode)
          adjustDevice();
      } else {
        mBackView.setVisibility(View.GONE);
        switch (mDeviceInfo.getErrorCode()) {
          case 4:
            setErrorMessage("You are out of navigation zone! Please, check that your bluetooth is enabled!");
            break;

        /*case 8:
        case 30:
          setErrorMessage("Not enough beacons on the location! Please, add more beacons!");
          break;*/

          default:
            setErrorMessage(String.format(Locale.ENGLISH,
                    "Something is wrong with location '%s' (error code %d)! " +
                            "Please, contact technical support!",
                    mLocation.getName(), mDeviceInfo.getErrorCode()));
            break;
        }
      }

      // This causes map redrawing
      mLocationView.redraw();
    }

    private void setErrorMessage(String message) {
      mErrorMessageLabel.setText(message);
      mErrorMessageLabel.setVisibility(View.VISIBLE);
    }


    private void cancelErrorMessage() {
      mErrorMessageLabel.setVisibility(View.GONE);
    }

    private boolean loadMap() {
      if (mNavigation == null) {
        Log.e(TAG, "Can't load map! Navigine SDK is not available!");
        return false;
      }

      mLocation = mNavigation.getLocation();
      mCurrentSubLocationIndex = -1;

      if (mLocation == null) {
        Log.e(TAG, "Loading map failed: no location");
        return false;
      }

      if (mLocation.getSubLocations().size() == 0) {
        Log.e(TAG, "Loading map failed: no sublocations");
        mLocation = null;
        return false;
      }

      if (!loadSubLocation(0)) {
        Log.e(TAG, "Loading map failed: unable to load default sublocation");
        mLocation = null;
        return false;
      }

      if (mLocation.getSubLocations().size() >= 2) {
        mPrevFloorView.setVisibility(View.VISIBLE);
        mNextFloorView.setVisibility(View.VISIBLE);
        mCurrentFloorLabel.setVisibility(View.VISIBLE);
      }
      mZoomInView.setVisibility(View.VISIBLE);
      mZoomOutView.setVisibility(View.VISIBLE);
      mAdjustModeView.setVisibility(View.VISIBLE);

      mNavigation.setMode(NavigationThread.MODE_NORMAL);
      //Normal mode for best accuracy. Takes up most battery.

      if (D.WRITE_LOGS)
        mNavigation.setLogFile(getLogFile("log"));

      mLocationView.redraw();
      //Priya's Code
    /*
    SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
    Log.d(TAG, String.format(Locale.ENGLISH, "%s ",mCurrentSubLocationIndex));
    for(int i = 0; i < subLoc.getVenues().size(); i++)
    {
      Venue ven = subLoc.getVenues().get(i);
        VenueList.add(ven.getName());
    }
    Set<String> NewVenue = new HashSet<>(VenueList);
    VenueList.clear();
    VenueList.addAll(NewVenue);

     */


      text1 = getIntent().getStringExtra("Position1");
      text2 = getIntent().getStringExtra("Position2");
      if (text1 != "---Select---") {
        Search();
      }
        //QR Code Listener problem
        //Get Qr Data
        SharedPreferences sp=getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
        String QrData=sp.getString("QrData","");
if(QrData!="NotSet")
{Toast.makeText(this," Location "+QrData,Toast.LENGTH_SHORT).show();
    Searchv2(QrData);
}




      return true;
    }



    private boolean loadSubLocation(int index) {
      if (mNavigation == null)
        return false;

      if (mLocation == null || index < 0 || index >= mLocation.getSubLocations().size())
        return false;

      SubLocation subLoc = mLocation.getSubLocations().get(index);
      Log.d(TAG, String.format(Locale.ENGLISH, "Loading sublocation %s (%.2f x %.2f)", subLoc.getName(), subLoc.getWidth(), subLoc.getHeight()));

      if (subLoc.getWidth() < 1.0f || subLoc.getHeight() < 1.0f) {
        Log.e(TAG, String.format(Locale.ENGLISH, "Loading sublocation failed: invalid size: %.2f x %.2f", subLoc.getWidth(), subLoc.getHeight()));
        return false;
      }

      if (!mLocationView.loadSubLocation(subLoc)) {
        Log.e(TAG, "Loading sublocation failed: invalid image");
        return false;
      }

      float viewWidth = mLocationView.getWidth();
      float viewHeight = mLocationView.getHeight();
      float minZoomFactor = Math.min(viewWidth / subLoc.getWidth(), viewHeight / subLoc.getHeight());
      float maxZoomFactor = LocationView.ZOOM_FACTOR_MAX;
      mLocationView.setZoomRange(minZoomFactor, maxZoomFactor);
      mLocationView.setZoomFactor(minZoomFactor);
      Log.d(TAG, String.format(Locale.ENGLISH, "View size: %.1f x %.1f", viewWidth, viewHeight));

      mAdjustTime = 0;
      mCurrentSubLocationIndex = index;


      mCurrentFloorLabel.setText(String.format(Locale.ENGLISH, "%d", mCurrentSubLocationIndex + 3));

      if (mCurrentSubLocationIndex > 0) {
        mPrevFloorButton.setEnabled(true);
        mPrevFloorView.setBackgroundColor(Color.parseColor("#90aaaaaa"));
      } else {
        mPrevFloorButton.setEnabled(false);
        mPrevFloorView.setBackgroundColor(Color.parseColor("#90dddddd"));
      }

      if (mCurrentSubLocationIndex + 1 < mLocation.getSubLocations().size()) {
        mNextFloorButton.setEnabled(true);
        mNextFloorView.setBackgroundColor(Color.parseColor("#90aaaaaa"));
      } else {
        mNextFloorButton.setEnabled(false);
        mNextFloorView.setBackgroundColor(Color.parseColor("#90dddddd"));
      }

      cancelVenue();
      mLocationView.redraw();

      return true;
    }

    private boolean loadNextSubLocation() {
      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return false;
      return loadSubLocation(mCurrentSubLocationIndex + 1);
    }

    private boolean loadPrevSubLocation() {
      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return false;
      return loadSubLocation(mCurrentSubLocationIndex - 1);
    }

    private void makePin(PointF P) {
      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return;

      SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
      if (subLoc == null)
        return;

      if (P.x < 0.0f || P.x > subLoc.getWidth() ||
              P.y < 0.0f || P.y > subLoc.getHeight()) {
        // Missing the map
        return;
      }

      if (mTargetPoint != null || mTargetVenue != null)
        return;

      if (mDeviceInfo == null || !mDeviceInfo.isValid())
        return;

      mPinPoint = new LocationPoint(mLocation.getId(), subLoc.getId(), P.x, P.y);
      mPinPointRect = new RectF();
      mLocationView.redraw();
    }

    private void cancelPin() {
      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return;

      SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
      if (subLoc == null)
        return;

      if (mTargetPoint != null || mTargetVenue != null || mPinPoint == null)
        return;

      mPinPoint = null;
      mPinPointRect = null;
      mLocationView.redraw();
    }

    private void cancelVenue() {
      mSelectedVenue = null;
      mLocationView.redraw();
    }

    private Venue getVenueAt(float x, float y) {
      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return null;

      SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
      if (subLoc == null)
        return null;

      Venue v0 = null;
      float d0 = 1000.0f;

      for (int i = 0; i < subLoc.getVenues().size(); ++i) {
        Venue v = subLoc.getVenues().get(i);
        PointF P = mLocationView.getScreenCoordinates(v.getX(), v.getY());
        float d = Math.abs(x - P.x) + Math.abs(y - P.y);
        if (d < 30.0f * mDisplayDensity && d < d0) {
          v0 = v;
          d0 = d;
        }
      }

      return v0;
    }
//yiheng's search
      public void Searchv2(String location)
      {
          SubLocation subLoc = mLocation.getSubLocations().get(0);

          for(int i = 0; i < subLoc.getVenues().size(); ++i) {
              Venue ven = subLoc.getVenues().get(i);
              Log.d(TAG, String.format(Locale.ENGLISH, " %s == %s ", ven.getName(), text1));

              if (ven.getName().equals(location)) {
                  Log.d(TAG, String.format(Locale.ENGLISH, "Got it"));
                  Log.d(TAG, String.format(Locale.ENGLISH, "Click at (%.2f, %.2f)", ven.getX(), ven.getY()));
                  //
                  handleClick(ven.getX(), ven.getY());
                  mSelectedVenue = subLoc.getVenues().get(i);
                  mTargetVenue = mSelectedVenue;
                  mNextRoute.setVisibility(View.INVISIBLE);
                  mNavigation.setTarget(new LocationPoint(mLocation.getId(), subLoc.getId(), mTargetVenue.getX(), mTargetVenue.getY()));
              }



          }
      }


    // Search function
    public void Search() {
      //text = "L306";

      SubLocation subLoc = mLocation.getSubLocations().get(0);

      for (int i = 0; i < subLoc.getVenues().size(); ++i) {
        Venue ven = subLoc.getVenues().get(i);
        String venname = subLoc.getName();
        Log.d(TAG, String.format(Locale.ENGLISH, " %s == %s ", ven.getName(), text1));
        //Log.d(TAG, String.format(Locale.ENGLISH, " %s == %s ", ven.getCategory().getName(), text1));
        if (ven.getName().equals(text1)) {
          Log.d(TAG, String.format(Locale.ENGLISH, "Got it"));
          Log.d(TAG, String.format(Locale.ENGLISH, "Click at (%.2f, %.2f)", ven.getX(), ven.getY()));
          //
          handleClick(ven.getX(), ven.getY());
          mSelectedVenue = subLoc.getVenues().get(i);
          mTargetVenue = mSelectedVenue;
          mNavigation.setTarget(new LocationPoint(mLocation.getId(), subLoc.getId(), mTargetVenue.getX(), mTargetVenue.getY()));
          if (text2.equals("---Select---")) {
            Log.d(TAG, String.format(Locale.ENGLISH, "Undefined! Please select a venue!"));
            continue;
          } else {
            mNextRoute.setVisibility(View.VISIBLE);
          }


        }


      }
      return;
    }


    public void onNextRoute(View v) {
      //text = "L306";
      Log.d(TAG, String.format(Locale.ENGLISH, text1));
      SubLocation subLoc = mLocation.getSubLocations().get(0);

      for (int i = 0; i < subLoc.getVenues().size(); ++i) {
        Venue ven = subLoc.getVenues().get(i);
        Log.d(TAG, String.format(Locale.ENGLISH, " %s == %s ", ven.getName(), text1));

        if (ven.getName().equals(text2)) {
          Log.d(TAG, String.format(Locale.ENGLISH, "Got it"));
          Log.d(TAG, String.format(Locale.ENGLISH, "Click at (%.2f, %.2f)", ven.getX(), ven.getY()));
          //
          handleClick(ven.getX(), ven.getY());
          mSelectedVenue = subLoc.getVenues().get(i);
          mTargetVenue = mSelectedVenue;
          mNextRoute.setVisibility(View.INVISIBLE);
          mNavigation.setTarget(new LocationPoint(mLocation.getId(), subLoc.getId(), mTargetVenue.getX(), mTargetVenue.getY()));
        }


      }
      return;
    }


    public void onMap(View v) {
      cat = null;
      mMap.setVisibility(View.INVISIBLE);
      return;
    }

    private Zone getZoneAt(float x, float y) {
      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return null;

      SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
      if (subLoc == null)
        return null;

      PointF P = mLocationView.getAbsCoordinates(x, y);
      LocationPoint LP = new LocationPoint(mLocation.getId(), subLoc.getId(), P.x, P.y);

      for (int i = 0; i < subLoc.getZones().size(); ++i) {
        Zone Z = subLoc.getZones().get(i);
        if (Z.contains(LP))
          return Z;
      }
      return null;
    }


    private void drawPoints(Canvas canvas) {
      // Check if location is loaded
      if (mLocation == null || mCurrentSubLocationIndex < 0)
        return;

      // Get current sublocation displayed
      SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);

      if (subLoc == null)
        return;

      final int solidColor = Color.argb(255, 64, 163, 205);  // Light-blue color
      final int circleColor = Color.argb(127, 64, 163, 205);  // Semi-transparent light-blue color
      final int arrowColor = Color.argb(255, 255, 255, 255); // White color
      final float dp = mDisplayDensity;
      final float textSize = 16 * dp;

      // Preparing paints
      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
      paint.setStyle(Paint.Style.FILL_AND_STROKE);
      paint.setTextSize(textSize);
      paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

      // Drawing pin point (if it exists and belongs to the current sublocation)
      if (mPinPoint != null && mPinPoint.subLocation == subLoc.getId()) {

        final PointF T = mLocationView.getScreenCoordinates(mPinPoint);
        final float tRadius = 10 * dp;

        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(4 * dp);
        canvas.drawLine(T.x, T.y, T.x, T.y - 3 * tRadius, paint);

        paint.setARGB(255,255,228,43);
        paint.setStrokeWidth(0);
        canvas.drawCircle(T.x, T.y - 3 * tRadius, tRadius, paint);

//          ImageView imageView = new ImageView(MainActivity.this);
//          imageView.setBackgroundResource(R.drawable.pin);
//
//        addView(imageView, T.x, T.y-3);

//        canvas.drawBitmap(mVenueBitmap, null, new RectF(T.x, T.y - 3), paint);

        final String text = "Make route";
        final float textWidth = paint.measureText(text);
        final float h = 50 * dp;
        final float w = Math.max(120 * dp, textWidth + h / 2);
        final float x0 = T.x;
        final float y0 = T.y - 75 * dp;

        mPinPointRect.set(x0 - w / 2, y0 - h / 2, x0 + w / 2, y0 + h / 2);

        paint.setARGB(255,255,65,65);
        canvas.drawRoundRect(mPinPointRect, h / 2, h / 2, paint);

        paint.setARGB(255, 255, 255, 255);
        canvas.drawText(text, x0 - textWidth / 2, y0 + textSize / 4, paint);
      }

      // Drawing target point (if it exists and belongs to the current sublocation)
      if (mTargetPoint != null && mTargetPoint.subLocation == subLoc.getId()) {
        Log.d(TAG, String.format(Locale.ENGLISH, "Drawing Path"));

        final PointF T = mLocationView.getScreenCoordinates(mTargetPoint);
        final float tRadius = 10 * dp;

        paint.setARGB(255,255,65,65);
        paint.setStrokeWidth(4 * dp);

        canvas.drawLine(T.x, T.y, T.x, T.y - 3 * tRadius, paint);

        paint.setARGB(255,255,65,65);

        canvas.drawCircle(T.x, T.y - 3 * tRadius, tRadius, paint);
      }
    }


      private void drawVenues(Canvas canvas)
      {
          if (mLocation == null || mCurrentSubLocationIndex < 0)
              return;

          SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
          final float dp = mDisplayDensity;
          final float textSize  = 16 * dp;
          final float venueSize = 30 * dp;
          final int   venueColor = Color.argb(255, 0xCD, 0x88, 0x50); // Venue color

          Paint paint = new Paint();
          paint.setStyle(Paint.Style.FILL_AND_STROKE);
          paint.setStrokeWidth(0);
          paint.setColor(venueColor);
          paint.setTextSize(textSize);
          paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));





          for(int i = 0; i < subLoc.getVenues().size(); ++i)
          {
              Venue v = subLoc.getVenues().get(i);
              if (v.getSubLocationId() != subLoc.getId())
                  continue;
              if (cat != null){
                  if (v.getCategory().getName().equals(cat)) {
                      mMap.setVisibility(View.VISIBLE);
                      final PointF P = mLocationView.getScreenCoordinates(v.getX(), v.getY());
                      final float x0 = P.x - venueSize / 2;
                      final float y0 = P.y - venueSize / 2;
                      final float x1 = P.x + venueSize / 2;
                      final float y1 = P.y + venueSize / 2;
                      canvas.drawBitmap(mVenueBitmap, null, new RectF(x0, y0, x1, y1), paint);
                  }
              }
              else {
                  mMap.setVisibility(View.INVISIBLE);
                  final PointF P = mLocationView.getScreenCoordinates(v.getX(), v.getY());
                  final float x0 = P.x - venueSize/2;
                  final float y0 = P.y - venueSize/2;
                  final float x1 = P.x + venueSize/2;
                  final float y1 = P.y + venueSize/2;
                  canvas.drawBitmap(mVenueBitmap, null, new RectF(x0, y0, x1, y1), paint);
              }

          }

          if (mSelectedVenue != null)
          {


              final PointF T = mLocationView.getScreenCoordinates(mSelectedVenue.getX(), mSelectedVenue.getY());
              final float textWidth = paint.measureText(mSelectedVenue.getName());

              final float h  = 50 * dp;
              final float w  = Math.max(120 * dp, textWidth + h/2);
              final float x0 = T.x;
              final float y0 = T.y - 50 * dp;

              mSelectedVenueRect.set(x0 - w/2, y0 - h/2, x0 + w/2, y0 + h/2);

              paint.setColor(venueColor);
              canvas.drawRoundRect(mSelectedVenueRect, h/2, h/2, paint);

              paint.setARGB(255, 255, 255, 255);
              canvas.drawText(mSelectedVenue.getName(), x0 - textWidth/2, y0 + textSize/4, paint);

          }
      }

      private void drawZones(Canvas canvas)
      {
          // Check if location is loaded
          if (mLocation == null || mCurrentSubLocationIndex < 0)
              return;

          // Get current sublocation displayed
          SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
          if (subLoc == null)
              return;

          // Preparing paints
          Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
          paint.setStyle(Paint.Style.FILL_AND_STROKE);

          for(int i = 0; i < subLoc.getZones().size(); ++i)
          {
              Zone Z = subLoc.getZones().get(i);

              if (Z.getPoints().size() < 3)
                  continue;

              boolean selected = (Z == mSelectedZone);

              Path path = new Path();
              final LocationPoint P0 = Z.getPoints().get(0);
              final PointF        Q0 = mLocationView.getScreenCoordinates(P0);
              path.moveTo(Q0.x, Q0.y);

              for(int j = 0; j < Z.getPoints().size(); ++j)
              {
                  final LocationPoint P = Z.getPoints().get((j + 1) % Z.getPoints().size());
                  final PointF        Q = mLocationView.getScreenCoordinates(P);
                  path.lineTo(Q.x, Q.y);
              }

              int zoneColor = Color.parseColor(Z.getColor());
              int red       = (zoneColor >> 16) & 0xff;
              int green     = (zoneColor >> 8 ) & 0xff;
              int blue      = (zoneColor >> 0 ) & 0xff;
              paint.setColor(Color.argb(selected ? 200 : 100, red, green, blue));
              canvas.drawPath(path, paint);
          }
      }

      private void drawDevice(Canvas canvas)
      {
          // Check if location is loaded
          if (mLocation == null || mCurrentSubLocationIndex < 0)
              return;

          // Check if navigation is available
          if (mDeviceInfo == null || !mDeviceInfo.isValid())
              return;

          // Get current sublocation displayed
          SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);

          if (subLoc == null)
              return;

          final int solidColor  = Color.argb(255, 64,  163, 205); // Light-blue color
          final int circleColor = Color.argb(127, 64,  163, 205); // Semi-transparent light-blue color
          final int arrowColor  = Color.argb(255, 255, 255, 255); // White color
          final float dp = mDisplayDensity;

          // Preparing paints
          Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
          paint.setStyle(Paint.Style.FILL_AND_STROKE);
          paint.setStrokeCap(Paint.Cap.ROUND);

          /// Drawing device path (if it exists)
          Log.d(TAG, String.format(Locale.ENGLISH, "Making path sublocation %s ",mDeviceInfo.getPaths()));
          if (mDeviceInfo.getPaths() != null && mDeviceInfo.getPaths().size() > 0)
          {
              RoutePath path = mDeviceInfo.getPaths().get(0);
              if (path.getPoints().size() >= 2)
              {
                  paint.setColor(solidColor);

                  for(int j = 1; j < path.getPoints().size(); ++j)
                  {
                      LocationPoint P = path.getPoints().get(j-1);
                      LocationPoint Q = path.getPoints().get(j);
                      Log.d(TAG, String.format(Locale.ENGLISH, "Clicked Q  %s  ", path.getPoints().get(j) ));
                      if (P.subLocation == subLoc.getId() && Q.subLocation == subLoc.getId())//
                      {

                          //Log.d(TAG, String.format(Locale.ENGLISH, "Clicked Q sublocation %s == %s " , Q.subLocation,subLoc.getId() ));
                          //Log.d(TAG, String.format(Locale.ENGLISH, "Clicked P sublocation %s == %s " , P.subLocation,subLoc.getId() ));
                          Log.d(TAG, String.format(Locale.ENGLISH, "Drawing path " ));
                          paint.setStrokeWidth(3 * dp);
                          PointF P1 = mLocationView.getScreenCoordinates(P);
                          PointF Q1 = mLocationView.getScreenCoordinates(Q);
                          canvas.drawLine(P1.x, P1.y, Q1.x, Q1.y, paint);//
                          //Log.d(TAG, String.format(Locale.ENGLISH, "Making path sublocation %s "));
                      }
                  }
              }
          }

          paint.setStrokeCap(Paint.Cap.BUTT);

          // Check if device belongs to the current sublocation
          if (mDeviceInfo.getSubLocationId() != subLoc.getId())
              return;

          final float x  = mDeviceInfo.getX();
          final float y  = mDeviceInfo.getY();
          final float r  = mDeviceInfo.getR();
          final float angle = mDeviceInfo.getAzimuth();
          final float sinA = (float)Math.sin(angle);
          final float cosA = (float)Math.cos(angle);
          final float radius  = mLocationView.getScreenLengthX(r);  // External radius: navigation-determined, transparent
          final float radius1 = 25 * dp;                            // Internal radius: fixed, solid

          PointF O = mLocationView.getScreenCoordinates(x, y);
          PointF P = new PointF(O.x - radius1 * sinA * 0.22f, O.y + radius1 * cosA * 0.22f);
          PointF Q = new PointF(O.x + radius1 * sinA * 0.55f, O.y - radius1 * cosA * 0.55f);
          PointF R = new PointF(O.x + radius1 * cosA * 0.44f - radius1 * sinA * 0.55f, O.y + radius1 * sinA * 0.44f + radius1 * cosA * 0.55f);
          PointF S = new PointF(O.x - radius1 * cosA * 0.44f - radius1 * sinA * 0.55f, O.y - radius1 * sinA * 0.44f + radius1 * cosA * 0.55f);

          // Drawing transparent circle
//          paint.setStrokeWidth(0);
//          paint.setColor(circleColor);
//          canvas.drawCircle(O.x, O.y, radius, paint);

          // Drawing solid circle
          paint.setColor(solidColor);
          canvas.drawCircle(O.x, O.y, radius1, paint);

          if (ORIENTATION_ENABLED)
          {
              // Drawing arrow
              paint.setColor(arrowColor);
              Path path = new Path();
              path.moveTo(Q.x, Q.y);
              path.lineTo(R.x, R.y);
              path.lineTo(P.x, P.y);
              path.lineTo(S.x, S.y);
              path.lineTo(Q.x, Q.y);
              canvas.drawPath(path, paint);
          }
      }

      private void adjustDevice()
      {
          // Check if location is loaded
          if (mLocation == null || mCurrentSubLocationIndex < 0)
              return;

          // Check if navigation is available
          if (mDeviceInfo == null || !mDeviceInfo.isValid())
              return;

          long timeNow = System.currentTimeMillis();

          // Adjust map, if necessary
          if (timeNow >= mAdjustTime)
          {
              // Firstly, set the correct sublocation
              SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
              if (mDeviceInfo.getSubLocationId() != subLoc.getId())
              {
                  for(int i = 0; i < mLocation.getSubLocations().size(); ++i)
                      if (mLocation.getSubLocations().get(i).getId() == mDeviceInfo.getSubLocationId())
                          loadSubLocation(i);
              }

              // Secondly, adjust device to the center of the screen
              PointF center = mLocationView.getScreenCoordinates(mDeviceInfo.getX(), mDeviceInfo.getY());
              float deltaX  = mLocationView.getWidth()  / 2 - center.x;
              float deltaY  = mLocationView.getHeight() / 2 - center.y;
              mAdjustTime   = timeNow;
              mLocationView.scrollBy(deltaX, deltaY);
          }
      }

      private String getLogFile(String extension)
      {
          try
          {
              final String extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Navigine.Demo";
              (new File(extDir)).mkdirs();
              if (!(new File(extDir)).exists())
                  return null;

              Calendar calendar = Calendar.getInstance();
              calendar.setTimeInMillis(System.currentTimeMillis());

              return String.format(Locale.ENGLISH, "%s/%04d%02d%02d_%02d%02d%02d.%s", extDir,
                      calendar.get(Calendar.YEAR),
                      calendar.get(Calendar.MONTH) + 1,
                      calendar.get(Calendar.DAY_OF_MONTH),
                      calendar.get(Calendar.HOUR_OF_DAY),
                      calendar.get(Calendar.MINUTE),
                      calendar.get(Calendar.SECOND),
                      extension);
          }
          catch (Throwable e)
          {
              return null;
          }
      }

    public void ClickVenueList(View v) {
      Intent intent = new Intent(this, VenuePage.class);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("Floor",mCurrentSubLocationIndex);
        editor.apply();

      startActivity(intent);
    }


    public void Search_Btn(View v) {


      int num = mCurrentSubLocationIndex;

      SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("Floor",0);
        editor.apply();


//        String floornumber = settings.getString("Floor","");

      //intent.putExtra("VenueList", (Serializable) VenueList);
        Intent intent = new Intent(getApplicationContext(), SearchPage.class);



      startActivity(intent);
    }



      private void requestCameraPermission()
      {
if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
new AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("Camera Permission is needed to use Qr code Scanning Feature").setPositiveButton("OK", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
    }
}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}).create().show();
}else
{
    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
}
      }

      private void registerUser()
      {

          AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
          alertDialog.setTitle("User Feature");
          alertDialog.setMessage("Register to user this feature");
          alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                          SharedPreferences sp;
                          sp=getSharedPreferences("MyUserProfile",MODE_PRIVATE);
                          SharedPreferences.Editor editor=sp.edit();
                          editor.putString("Guest","False");
                          editor.commit();
                          startActivity(new Intent(getApplicationContext(),Register.class));
                      }
                  });
          alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  return;
              }
          });
          alertDialog.show();
      }


      @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          if (requestCode == CAMERA_PERMISSION)
           {
              if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();}
else
              {
                  Toast.makeText(this,"Please enable Camera Access!",Toast.LENGTH_SHORT).show();
              }
          }
      }



      public void guideButton(View v) {

            if(v.getId() == R.id.guide) {
                Intent intent = new Intent(this, InfoGuide.class);

                startActivity(intent);
            }

      }

      // Location listener
// Yongkai's code
//  @Override
//  public void onLocationChanged(android.location.Location location) {
//    if(location!=null)
//    {
//      txtLocal = (TextView) findViewById(R.id.debuglocate);
//      txtLocal.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
//    }
//  }
//
//  @Override
//  public void onStatusChanged(String s, int i, Bundle bundle) {
//    Log.d("Latitude","status");
//  }
//
//  @Override
//  public void onProviderEnabled(String s) {
//    Log.d("Latitude","enable");
//
//  }
//
//  @Override
//  public void onProviderDisabled(String s) {
//    Log.d("Latitude","disable");
//  }
//
//
  }

