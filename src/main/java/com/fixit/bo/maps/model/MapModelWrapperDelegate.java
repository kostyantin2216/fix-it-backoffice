/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.List;
import java.util.Optional;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import com.fixit.core.data.mongo.MapArea;

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
	public void hide(Polygon polygon) {
		mParentWrapper.hide(polygon);
	}

	@Override
	public void show(Polygon polygon) {
		mParentWrapper.show(polygon);
	}

	@Override
	public void select(Polygon polygon) {
		mParentWrapper.select(polygon);
	}

	@Override
	public void unselect(Polygon polygon) {
		mParentWrapper.unselect(polygon);
	}
	
	@Override
	public void onPolygonSelect(OverlaySelectEvent event) {
		mParentWrapper.onPolygonSelect(event);
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
	public List<MapArea> getMapAreas() {
		return mParentWrapper.getMapAreas();
	}
	
	@Override
	public String getMapElementId() {
		return mParentWrapper.getMapElementId();
	}

	@Override
	public Optional<MapModelWrapper> getParentWrapper() {
		return Optional.of(mParentWrapper);
	}
}
