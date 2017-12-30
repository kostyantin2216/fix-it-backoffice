/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.Optional;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/28 21:30:56 GMT+2
 */
public class MapModelWrapperDelegate implements MapModelWrapper {
	
	private final MapModelWrapper mParentWrapper;

	protected MapModelWrapperDelegate(MapModelWrapper parentWrapper) {
		mParentWrapper = parentWrapper;
	}
	
	@Override
	public boolean onPolygonSelect(OverlaySelectEvent event) {
		return mParentWrapper.onPolygonSelect(event);
	}
	
	@Override
	public void onStateChange(StateChangeEvent event) {
		mParentWrapper.onStateChange(event);
	}
	
	@Override
	public LatLng getCenter() {
		return mParentWrapper.getCenter();
	}
	
	@Override
	public int getZoomLevel() {
		return mParentWrapper.getZoomLevel();
	}

	@Override
	public MapModel getModel() {
		return mParentWrapper.getModel();
	}

	@Override
	public Optional<MapModelWrapper> getParentWrapper() {
		return Optional.of(mParentWrapper);
	}
	
	@Override
	public Options getPolygonOptions() {
		return mParentWrapper.getPolygonOptions();
	}
}
