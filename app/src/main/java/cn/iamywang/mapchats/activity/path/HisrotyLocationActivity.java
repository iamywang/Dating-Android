package cn.iamywang.mapchats.activity.path;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import cn.iamywang.mapchats.R;
import cn.iamywang.mapchats.util.JavaHttpKolley;
import com.amap.api.maps.*;
import com.amap.api.maps.model.*;

import java.text.DecimalFormat;
import java.util.List;

public class HisrotyLocationActivity extends AppCompatActivity {
    private AMap aMap;
    private MapView mapView;
    public String USERID, PATHID;

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
        this.PATHID = infointent.getStringExtra("path");

        TextView path_view = findViewById(R.id.his_text_tag);
        path_view.setText(this.PATHID);

        mapView = (MapView) findViewById(R.id.his_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

        if (this.PATHID.equals("0")) {
            JavaHttpKolley jhk = new JavaHttpKolley();
            jhk.getHisRoadList(this.USERID, this);
        } else {
            JavaHttpKolley jhk = new JavaHttpKolley();
            jhk.getHisLine(this.USERID, this.PATHID, this);
        }
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
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.6507007, 117.1140042), 11));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void addHisLine(List<LatLng> list, int color, String start, String end) {
        PolylineOptions options = new PolylineOptions();
        options.addAll(list);
        options.width(15);
        options.color(color);
        options.lineJoinType(PolylineOptions.LineJoinType.LineJoinRound);
        options.lineCapType(PolylineOptions.LineCapType.LineCapRound);
        options.setDottedLine(true);
        aMap.addMarker(new MarkerOptions().position(list.get(0)).title("起点").snippet(start));
        aMap.addPolyline(options);
        double result_length = 0.00;
        for (int i = 1; i < list.size(); i++) {
            result_length += calculateLineDistance(list.get(i - 1), list.get(i));
        }
        DecimalFormat df2 = new DecimalFormat("0.00");
        String length = df2.format(result_length) + "m";
        TextView start_view = findViewById(R.id.his_text_time);
        TextView length_view = findViewById(R.id.his_text_length);
        start_view.setText(start);
        length_view.setText(length);

        MarkerOptions pathMarkerOptions = new MarkerOptions();
        pathMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.run)));
        pathMarkerOptions.anchor(0.5f, 0.5f);
        pathMarkerOptions.position(list.get(0));
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 11));
        Marker pathMarker = aMap.addMarker(pathMarkerOptions);

        class PathReviewThread extends Thread {
            private Marker pathMarker;
            private List<LatLng> list;

            private PathReviewThread(List<LatLng> list, Marker pathMarker) {
                this.list = list;
                this.pathMarker = pathMarker;
            }

            @Override
            public void run() {
                try {
                    for (int i = 1; i < list.size(); i++) {
                        Thread.sleep(400);
                        pathMarker.setPosition(list.get(i));
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(i), 16));
                    }
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pathMarker.remove();
            }
        }
        PathReviewThread t = new PathReviewThread(list, pathMarker);
        t.start();
        aMap.addMarker(new MarkerOptions().position(list.get(list.size() - 1)).title("终点").snippet(end));
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
