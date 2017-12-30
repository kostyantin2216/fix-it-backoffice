/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polygon;

import com.fixit.core.data.mongo.MapArea;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/28 21:28:34 GMT+2
 */
public interface PolygonManipulator {

	public Options getPolygonOptions();
	
	default void hide(Polygon polygon) {
		polygon.setFillOpacity(0);
		polygon.setStrokeOpacity(0);
	}
	
	default void show(Polygon polygon) {
		Options options = getPolygonOptions();
		polygon.setFillOpacity(options.fillOpacity);
		polygon.setStrokeOpacity(options.strokeOpacity);
	}
	
	default void select(Polygon polygon) {
		Options options = getPolygonOptions();
		polygon.setFillColor(options.selectedFillColor);
		polygon.setStrokeColor(options.selectedStrokeColor);
	}
	
	default void unselect(Polygon polygon) {
		Options options = getPolygonOptions();
		polygon.setFillColor(options.fillColor);
		polygon.setStrokeColor(options.strokeColor);	
	}
	
	default Polygon createPolygon(MapArea area) {
		double[][] coordinates = area.getGeometry().getCoordinates()[0];
		Polygon polygon = new Polygon();
        List<LatLng> paths = polygon.getPaths();
    
        for(int i = 0; i < coordinates.length; i++) {
        	paths.add(new LatLng(coordinates[i][1], coordinates[i][0]));
        }
        
        getPolygonOptions().apply(polygon);
        polygon.setData(new HashMap<String, Object>());
  
        return polygon;
	}
	
	@SuppressWarnings("unchecked")
	default void setData(Polygon polygon, String key, Object object) {
		((Map<String, Object>) polygon.getData()).put(key, object);
	}
	
	@SuppressWarnings("unchecked")
	default <T> T getData(Polygon polygon, String key) {
		return (T)((Map<String, Object>) polygon.getData()).get(key);
	}
	
	public boolean onPolygonSelect(OverlaySelectEvent event);
	
	public static class Options {
		private String selectedStrokeColor = "#7CFC00";
		private String selectedFillColor = "#7CFC00";
		private String strokeColor = "#FF9900";
		private String fillColor = "#FF9900";
		private double strokeOpacity = 0.8;
		private double fillOpacity = 0.2;
	
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
		
		public void apply(Polygon polygon) {
	        polygon.setStrokeColor(strokeColor);
	        polygon.setFillColor(fillColor);
	        polygon.setStrokeOpacity(strokeOpacity);
	        polygon.setFillOpacity(fillOpacity);
		}
	}

}
