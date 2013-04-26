package com.zxzh.baiduloation;

import project.tigerim.R;
import android.content.Context;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.kmdsoft.demomap.OverItemT;

public class BaiduLoaction {
	private LocationClient mLocationClient = null; // 声明定位类
	private LocationClientOption option = null;// 定位说明类
	private MyReceiveListenner mListenner = new MyReceiveListenner();// 定位监听器
	private static boolean isOpenLocation = false;
	private LocationObject object;

	private MapView mapView;
	private TextView adressTV;
	private OverItemT overItemT;
	private LocationData locationData;
	private boolean ismap = false;

	public BaiduLoaction(Context context, LocationObject object) {
		mLocationClient = new LocationClient(context.getApplicationContext());
		mLocationClient.registerLocationListener(mListenner); // 注册监听函数
		this.object = object;
		ismap = false;
	}

	public BaiduLoaction(Context context, MapView mapView, TextView adressTV) {
		mLocationClient = new LocationClient(context.getApplicationContext());
		mLocationClient.registerLocationListener(mListenner); // 注册监听函数
		this.mapView = mapView;
		this.adressTV = adressTV;
		locationData = new LocationData();
		overItemT = new OverItemT(context.getResources().getDrawable(
				R.drawable.ico_set_distance));
		ismap = true;
	}

	// 接受定位得到的消息
	private class MyReceiveListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			
			if (ismap) {
				locationData.latitude = location.getLatitude();
				locationData.longitude = location.getLongitude();
				mapView.getOverlays().clear();
				overItemT.setData(locationData);
				mapView.getOverlays().add(overItemT);
				mapView.refresh();
				adressTV.setText("Latitude:" + locationData.latitude
						+ "\nLongitude:" + locationData.longitude + "\n"
						+ location.getAddrStr());
				GeoPoint point = new GeoPoint(
						(int) (locationData.latitude * 1E6),
						(int) (locationData.longitude * 1E6));// 用给定的经纬度构造一个GeoPoint，单位是微度
																// (度 * 1E6)
				mapView.getController().setCenter(point);// 设置地图中心点
			}else{
				object.x = location.getLatitude();
				object.y = location.getLongitude();
			}
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	public void startLocation() {
		try {
			if (!isOpenLocation) // 如果没有打开
			{
				option = new LocationClientOption();
				option.setOpenGps(true);
				option.setAddrType("all");// 返回的定位结果包含地址信息
				option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
				option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
				option.disableCache(true);// 禁止启用缓存定位
				mLocationClient.setLocOption(option);
				mLocationClient.start(); // 打开定位
				isOpenLocation = true; // 标识为已经打开了定位
				if (mLocationClient != null && mLocationClient.isStarted())
					mLocationClient.requestOfflineLocation();
				if (mLocationClient != null && mLocationClient.isStarted())
					mLocationClient.requestLocation();
				if (mLocationClient != null && mLocationClient.isStarted())
					mLocationClient.requestPoi();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * end 定位
	 */
	public void closeLocation() {
		try {
			mLocationClient.stop(); // 结束定位
			isOpenLocation = false; // 标识为已经结束了定位
		} catch (Exception e) {

		}
	}
}
