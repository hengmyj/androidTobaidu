package com.kmdsoft.demomap;

import project.tigerim.R;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zxzh.baiduloation.BaiduLoaction;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class MapActivity extends Activity {
	BMapManager mBMapMan = null;
	MapView mMapView = null;

	private TextView adressTV;
	private BaiduLoaction baidulocation;
//	MyLocationOverlay myLocationOverlay = null;
	LocationData locationiData = null;
	OverItemT overItemT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init("E8DC01EA8C1EECD05E143BCAF19C87B1B17C7C6E", null);
		setContentView(R.layout.activity_baidumap);
		locationiData=new LocationData();
		locationiData.latitude=29.534266;
		locationiData.longitude=106.490784;
		
		mMapView = (MapView) findViewById(R.id.bmapsView);
		adressTV=(TextView)findViewById(R.id.textview);
//		myLocationOverlay = new MyLocationOverlay(mMapView);
//		myLocationOverlay.setData(locationiData);
		ininMap();
//		overItemT=new OverItemT(getResources().getDrawable(R.drawable.ico_set_distance));
//		mMapView.getOverlays().add(overItemT);
//		myLocationOverlay.enableCompass();
//		mMapView.refresh();
//		mMapView.getController().animateTo(new GeoPoint((int)(locationiData.latitude*1e6),
//				(int)(locationiData.longitude* 1e6)));
		baidulocation = new BaiduLoaction(this, mMapView,adressTV);
		
	}

	private void ininMap() {
		
		// mMapView.setBuiltInZoomControls(true);// 设置启用内置的缩放控件
		MapController mMapController = mMapView.getController();// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point = new GeoPoint(
				(int) (locationiData.latitude * 1E6),
				(int) (locationiData.longitude * 1E6));// 用给定的经纬度构造一个GeoPoint，单位是微度
																// (度 * 1E6)
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(16);// 设置地图zoom级别
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
	
		baidulocation.closeLocation();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		
		baidulocation.startLocation();
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
