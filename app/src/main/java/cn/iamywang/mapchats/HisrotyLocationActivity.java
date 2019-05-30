package cn.iamywang.mapchats;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class HisrotyLocationActivity extends AppCompatActivity implements LocationSource,
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
    private String USERID = "1";
    private Random rand = new Random();
    private int roadIndex = rand.nextInt(1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisroty_location);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent infointent = getIntent();
        this.USERID = infointent.getStringExtra("id");
        mapView = (MapView) findViewById(R.id.his_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        JavaHttpKolley jhk = new JavaHttpKolley();
        jhk.getHisLine(this.USERID, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
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
                DecimalFormat df = new DecimalFormat("0.000000");
                Double lat = location.latitude;
                Double lon = location.longitude;
                String loc = df.format(lat) + "," + df.format(lon);
                JavaHttpKolley jhk = new JavaHttpKolley();
                jhk.addCurLine(this.USERID, "" + this.roadIndex, loc, this);
                addCurLine(location);
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
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
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


    public void addLocLine(List<LatLng> list) {
        PolylineOptions options = new PolylineOptions();
        options.addAll(list);
        options.width(10);
        options.color(Color.argb(255, 80, 80, 80));
        aMap.addPolyline(options);
    }

    public void addCurLine(LatLng l) {
        if (mLocPoly != null) {
            PolylineOptions options = mLocPoly.getOptions();
            options.add(l);
            options.width(10);
            options.color(Color.argb(255, 70, 128, 128));
            mLocPoly = aMap.addPolyline(options);
        } else {
            PolylineOptions options = new PolylineOptions();
            options.add(l);
            options.width(10);
            options.color(Color.argb(255, 70, 128, 128));
            mLocPoly = aMap.addPolyline(options);
        }
    }
}
