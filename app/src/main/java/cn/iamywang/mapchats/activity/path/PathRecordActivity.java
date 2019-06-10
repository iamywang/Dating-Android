package cn.iamywang.mapchats.activity.path;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.iamywang.mapchats.R;
import cn.iamywang.mapchats.util.network.JavaHttpKolley;
import cn.iamywang.mapchats.util.misc.SensorEventHelper;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.*;
import com.amap.api.maps.model.*;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

public class PathRecordActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener {
    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean mFirstFix = false;
    private Marker mLocMarker;
    private Polyline mLocPoly;
    private SensorEventHelper mSensorHelper;
    private Circle mCircle;
    public static final String LOCATION_MARKER_FLAG = "当前位置";

    private String USERID;

    private Random rand = new Random();
    private int roadIndex = rand.nextInt(9000) + 1000;//路径编号
    private Boolean roadBool = true;//是否首次添加
    private Boolean enbableAddPath = false;//是否允许记录轨迹
    private Double lat, lon;//记录当前位置
    private Double pathLength = 0.00;//记录路径长度
    private TimeRecordThread recordThread;//计时线程

    class TimeRecordThread extends Thread {
        private final long starttime = System.currentTimeMillis();
        private Boolean exit = false;

        @Override
        public void run() {
            while (!exit) {
                long time = (System.currentTimeMillis() - starttime) / 1000;
                TextView timer_text = findViewById(R.id.record_t4);
                DecimalFormat df = new DecimalFormat("00");
                String tmp = df.format(time / 60) + ":" + df.format(time - (time / 60) * 60);
                timer_text.setText(tmp);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_record);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent infointent = getIntent();
        this.USERID = infointent.getStringExtra("id");
        TextView index_view = findViewById(R.id.record_t2);
        String index_text = "" + this.roadIndex;
        index_view.setText(index_text);
        mapView = (MapView) findViewById(R.id.record_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View view) {
        Button button = findViewById(R.id.record_button);
        if (this.enbableAddPath) {
            this.enbableAddPath = false;
            button.setText("开始");
            button.setEnabled(false);
            TextView time_view = findViewById(R.id.record_t4);
            Intent intent = new Intent(PathRecordActivity.this, PathResultActivity.class);
            intent.putExtra("id", this.USERID);
            intent.putExtra("nick", getIntent().getStringExtra("nick"));
            intent.putExtra("num", "" + this.roadIndex);
            Date start_date = new Date(recordThread.starttime);
            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:dd");
            intent.putExtra("start", "" + sdFormatter.format(start_date));
            intent.putExtra("time", time_view.getText().toString());
            intent.putExtra("length", "" + this.pathLength);
            startActivity(intent);
            recordThread.exit = true;
            PathRecordActivity.this.finish();
        } else {
            this.enbableAddPath = true;
            button.setText("停止");
            mLocationOption.setInterval(1000);
            mlocationClient.startLocation();
            recordThread = new TimeRecordThread();
            recordThread.start();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mSensorHelper = new SensorEventHelper(this);
        mSensorHelper.registerSensorListener();
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);// 设置指南针是否显示
        uiSettings.setZoomControlsEnabled(true);// 设置缩放按钮是否显示
        uiSettings.setScaleControlsEnabled(true);// 设置比例尺是否显示
        uiSettings.setRotateGesturesEnabled(true);// 设置地图旋转是否可用
        uiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.6507007, 117.1140042), 11));
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mapView.onPause();
        deactivate();
        mFirstFix = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocMarker != null) {
            mLocMarker.destroy();
        }
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                if (enbableAddPath) {
                    this.lat = location.latitude;
                    this.lon = location.longitude;
                    addCurLine(location);
                }
                if (!mFirstFix) {
                    mFirstFix = true;
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(location));
                }
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mLocationOption.setInterval(10000);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

    public void addCurLine(LatLng l) {
        PolylineOptions options;
        LatLng point1;
        if (mLocPoly != null) {
            options = mLocPoly.getOptions();
            point1 = options.getPoints().get(options.getPoints().size() - 1);
        } else {
            options = new PolylineOptions();
            point1 = l;
            aMap.addMarker(new MarkerOptions().position(l).title("起点").snippet("起点"));
        }
        options.add(l);
        options.width(15);
        options.color(Color.argb(255, 0, 128, 255));
        options.lineJoinType(PolylineOptions.LineJoinType.LineJoinRound);
        options.lineCapType(PolylineOptions.LineCapType.LineCapRound);
        mLocPoly = aMap.addPolyline(options);

        DecimalFormat df = new DecimalFormat("0.000000");
        String loc = df.format(this.lat) + "," + df.format(this.lon);
        DecimalFormat df2 = new DecimalFormat("0.00");
        this.pathLength += calculateLineDistance(point1, l);
        String path = df2.format(this.pathLength) + "m";
        TextView pathText = findViewById(R.id.record_t6);
        pathText.setText(path);

        JavaHttpKolley jhk = new JavaHttpKolley();
        jhk.addCurLine(this.USERID, "" + this.roadIndex, loc);
        if (this.roadBool) {
            jhk.addRoad(this.USERID, "" + this.roadIndex);
            this.roadBool = false;
        }
    }

    public double calculateLineDistance(LatLng start, LatLng end) {
        if ((start == null) || (end == null)) {
            throw new IllegalArgumentException("非法坐标值，不能为null");
        }
        double d1 = 0.01745329251994329D;
        double d2 = start.longitude;
        double d3 = start.latitude;
        double d4 = end.longitude;
        double d5 = end.latitude;
        d2 *= d1;
        d3 *= d1;
        d4 *= d1;
        d5 *= d1;
        double d6 = Math.sin(d2);
        double d7 = Math.sin(d3);
        double d8 = Math.cos(d2);
        double d9 = Math.cos(d3);
        double d10 = Math.sin(d4);
        double d11 = Math.sin(d5);
        double d12 = Math.cos(d4);
        double d13 = Math.cos(d5);
        double[] arrayOfDouble1 = new double[3];
        double[] arrayOfDouble2 = new double[3];
        arrayOfDouble1[0] = (d9 * d8);
        arrayOfDouble1[1] = (d9 * d6);
        arrayOfDouble1[2] = d7;
        arrayOfDouble2[0] = (d13 * d12);
        arrayOfDouble2[1] = (d13 * d10);
        arrayOfDouble2[2] = d11;
        double d14 = Math.sqrt((arrayOfDouble1[0] - arrayOfDouble2[0]) * (arrayOfDouble1[0] - arrayOfDouble2[0])
                + (arrayOfDouble1[1] - arrayOfDouble2[1]) * (arrayOfDouble1[1] - arrayOfDouble2[1])
                + (arrayOfDouble1[2] - arrayOfDouble2[2]) * (arrayOfDouble1[2] - arrayOfDouble2[2]));

        return (Math.asin(d14 / 2.0D) * 12742001.579854401D);
    }
}
