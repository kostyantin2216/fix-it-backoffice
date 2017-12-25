/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.HashMap;
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
 * @createdAt 	2017/11/28 21:01:40 GMT+2
 */
public class BaseMapModelWrapper implements MapModelWrapper {

	private final MapModel mModel;
	private final List<MapArea> mMapAreas;
	private final String mMapElementId;
	private final Options mOptions;
	
	public BaseMapModelWrapper(List<MapArea> mapAreas, String mapElementId, LatLng center) {
		this(mapAreas, mapElementId, new Options(center));
	}
	
	public BaseMapModelWrapper(List<MapArea> mapAreas, String mapElementId, Options options) {
		mModel = new MapAreaModel();
		mMapAreas = mapAreas;
		mMapElementId = mapElementId;
		mOptions = options;

		for(MapArea mapArea : mMapAreas) {
			double[][][] coordinates = mapArea.getGeometry().getCoordinates();
			Polygon polygon = createPolygon(coordinates[0]);
			String mapAreaId = mapArea.get_id().toHexString();
			polygon.setId(mapAreaId);
			mModel.addOverlay(polygon);
		}
	}
	
	private Polygon createPolygon(double[][] coordinates) {
		Polygon polygon = new Polygon();
        List<LatLng> paths = polygon.getPaths();
    
        for(int i = 0; i < coordinates.length; i++) {
        	paths.add(new LatLng(coordinates[i][1], coordinates[i][0]));
        }
  
        polygon.setStrokeColor(mOptions.strokeColor);
        polygon.setFillColor(mOptions.fillColor);
        polygon.setStrokeOpacity(mOptions.strokeOpacity);
        polygon.setFillOpacity(mOptions.fillOpacity);
        polygon.setData(new HashMap<String, Object>());
  
        return polygon;
	}
	
	@Override
	public void hide(Polygon polygon) {
		polygon.setFillOpacity(0);
		polygon.setStrokeOpacity(0);
	}
	
	@Override
	public void show(Polygon polygon) {
		polygon.setFillOpacity(mOptions.fillOpacity);
		polygon.setStrokeOpacity(mOptions.strokeOpacity);
	}
	
	@Override
	public void select(Polygon polygon) {
		polygon.setFillColor(mOptions.selectedFillColor);
		polygon.setStrokeColor(mOptions.selectedStrokeColor);
	}

	@Override
	public void unselect(Polygon polygon) {
		polygon.setFillColor(mOptions.fillColor);
		polygon.setStrokeColor(mOptions.strokeColor);
	}
	
	@Override
	public void onPolygonSelect(OverlaySelectEvent event) {
		// do nothing, child wrappers can override if needed.
	}
	
	@Override
	public void onStateChange(StateChangeEvent event) {
		mOptions.zoomLevel = event.getZoomLevel();
		mOptions.center = event.getCenter();
	}
	
	@Override
	public LatLng getCenter() {
		return mOptions.center;
	}
	
	@Override
	public int getZoomLevel() {
		return mOptions.zoomLevel;
	}
	
	@Override
	public MapModel getModel() {
		return mModel;
	}
	
	@Override
	public List<MapArea> getMapAreas() {
		return mMapAreas;
	}
	
	@Override
	public String getMapElementId() {
		return mMapElementId;
	}	

	@Override
	public Optional<MapModelWrapper> getParentWrapper() {
		return Optional.empty();
	}
	
	public static class Options {
		private LatLng center; 
		private String selectedStrokeColor = "#7CFC00";
		private String selectedFillColor = "#7CFC00";
		private String strokeColor = "#FF9900";
		private String fillColor = "#FF9900";
		private double strokeOpacity = 0.8;
		private double fillOpacity = 0.2;
		private int zoomLevel = 8;
		
		public Options(LatLng center) {
			this.center = center;
		}
		
		public Options setStrokeColor(String color) {
			this.strokeColor = color;
			return this;
		}
		public Options setFillColor(String color) {
			this.fillColor = color;
			return this;
		}
		public Options setStrokeOpacity(double opacity) {
			this.strokeOpacity = opacity;
			return this;
		}
		public Options setFillOpacity(double opacity) {
			this.fillOpacity = opacity;
			return this;
		}
		public Options setSelectedStrokeColor(String selectedStrokeColor) {
			this.selectedStrokeColor = selectedStrokeColor;
			return this;
		}
		public Options setSelectedFillColor(String selectedFillColor) {
			this.selectedFillColor = selectedFillColor;
			return this;
		}
		public Options setZoomLevel(int zoomLevel) {
			this.zoomLevel = zoomLevel;
			return this;
		}
	}
	
}
