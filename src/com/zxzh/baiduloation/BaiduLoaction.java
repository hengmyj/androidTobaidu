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
	private LocationClient mLocationClient = null; // ������λ��
	private LocationClientOption option = null;// ��λ˵����
	private MyReceiveListenner mListenner = new MyReceiveListenner();// ��λ������
	private static boolean isOpenLocation = false;
	private LocationObject object;

	private MapView mapView;
	private TextView adressTV;
	private OverItemT overItemT;
	private LocationData locationData;
	private boolean ismap = false;

	public BaiduLoaction(Context context, LocationObject object) {
		mLocationClient = new LocationClient(context.getApplicationContext());
		mLocationClient.registerLocationListener(mListenner); // ע���������
		this.object = object;
		ismap = false;
	}

	public BaiduLoaction(Context context, MapView mapView, TextView adressTV) {
		mLocationClient = new LocationClient(context.getApplicationContext());
		mLocationClient.registerLocationListener(mListenner); // ע���������
		this.mapView = mapView;
		this.adressTV = adressTV;
		locationData = new LocationData();
		overItemT = new OverItemT(context.getResources().getDrawable(
				R.drawable.ico_set_distance));
		ismap = true;
	}

	// ���ܶ�λ�õ�����Ϣ
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
						(int) (locationData.longitude * 1E6));// �ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢��
																// (�� * 1E6)
				mapView.getController().setCenter(point);// ���õ�ͼ���ĵ�
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
			if (!isOpenLocation) // ���û�д�
			{
				option = new LocationClientOption();
				option.setOpenGps(true);
				option.setAddrType("all");// ���صĶ�λ���������ַ��Ϣ
				option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
				option.setScanSpan(5000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
				option.disableCache(true);// ��ֹ���û��涨λ
				mLocationClient.setLocOption(option);
				mLocationClient.start(); // �򿪶�λ
				isOpenLocation = true; // ��ʶΪ�Ѿ����˶�λ
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
	 * end ��λ
	 */
	public void closeLocation() {
		try {
			mLocationClient.stop(); // ������λ
			isOpenLocation = false; // ��ʶΪ�Ѿ������˶�λ
		} catch (Exception e) {

		}
	}
}
