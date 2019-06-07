package cn.iamywang.mapchats;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import com.amap.api.maps.*;
import com.amap.api.maps.model.*;

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
        TextView start_view = findViewById(R.id.his_text_time);
        start_view.setText(start);

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
}
