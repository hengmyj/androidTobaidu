package com.kmdsoft.demomap;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class OverItemT extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();

	private double mLat1 = 29.534266;// 39.9022; // point1纬度
	private double mLon1 = 106.490784;// 116.3822; // point1经度

	public OverItemT(Drawable arg0) {
		super(arg0);
		// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		GeoList.add(new OverlayItem(p1, "P1", "point1"));
		populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
	}

	public void setData(LocationData locData) {
		GeoList.clear();
		GeoPoint p1 = new GeoPoint((int) (locData.latitude * 1E6), (int) (locData.longitude * 1E6));
		GeoList.add(new OverlayItem(p1, "P1", "point1"));
		populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		return GeoList.get(arg0);
	}

	@Override
	public int size() {
		return GeoList.size();
	}

}
