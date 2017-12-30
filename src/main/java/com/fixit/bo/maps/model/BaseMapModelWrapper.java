/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.Optional;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/29 17:19:42 GMT+2
 */
public abstract class BaseMapModelWrapper implements MapModelWrapper {

	private final static int DEFAULT_ZOOM_LEVEL = 8;
	
	private final Options mOptions;
	
	private LatLng mCenter;
	private int mZoomLevel;
	
	public BaseMapModelWrapper(Options options, LatLng center) {
		this(options, center, DEFAULT_ZOOM_LEVEL);
	}
	
	public BaseMapModelWrapper(Options options, LatLng center, int zoomLevel) {
		mOptions = options;
		mCenter = center;
		mZoomLevel = zoomLevel;
	}
	
	@Override
	public void onStateChange(StateChangeEvent event) {
		mZoomLevel = event.getZoomLevel();
		mCenter = event.getCenter();
	}
	
	@Override
	public LatLng getCenter() {
		return mCenter;
	}
	
	@Override
	public int getZoomLevel() {
		return mZoomLevel;
	}
	
	@Override
	public Options getPolygonOptions() {
		return mOptions;
	}

	@Override
	public Optional<MapModelWrapper> getParentWrapper() {
		return Optional.empty();
	}
	
}
