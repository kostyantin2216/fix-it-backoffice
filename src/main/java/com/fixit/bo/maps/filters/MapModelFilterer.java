/**
 * 
 */
package com.fixit.bo.maps.filters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.primefaces.model.map.Polygon;

import com.fixit.bo.maps.model.PolygonManipulator;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/17 13:49:05 GMT+2
 */
public class MapModelFilterer {
	
	public enum FilterType {
		TRADESMEN_COUNT("tradesmenCount"),
		ORDER_COUNT("orderCount");
		
		final String key;
		
		FilterType(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
		
		MinMaxFilter minMax(Integer min, Integer max) {
			return new MinMaxFilter(key, min, max);
		}
		
		public static Optional<FilterType> forKey(String key) {
			for(FilterType filterType : values()) {
				if(filterType.key.equals(key)) {
					return Optional.of(filterType);
				}
			}
			return Optional.empty();
		}
	}
	
	private MapModelFilter filter;
	private PolygonManipulator polygonManipulator;
	
	public MapModelFilterer(PolygonManipulator polygonManipulator) {
		this.polygonManipulator = polygonManipulator;
	}
	
	@SuppressWarnings("unchecked")
	public boolean containsFilter(List<Polygon> polygons, FilterType filterType) {
		if(polygons.size() < 1) {
			return true;
		}
		
		for(Polygon polygon : polygons) {
			Map<String, Object> data = (Map<String, Object>) polygon.getData();
			if(data.containsKey(filterType.key)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void clear(List<Polygon> model) {
		filter = null;
		for(Polygon polygon : model) {
			polygonManipulator.show(polygon);
		}
	}
	
	public void filter(List<Polygon> model, FilterType filterType, Integer min, Integer max) {
		if(filter != null) {
			filter.then(filterType.minMax(min, max));
		} else {
			filter = filterType.minMax(min, max);
		}
		filter.filter(polygonManipulator, model);
	}

}
