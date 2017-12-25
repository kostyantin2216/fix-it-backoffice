/**
 * 
 */
package com.fixit.bo.maps.filters;

import java.util.List;
import java.util.Map;

import org.primefaces.model.map.Polygon;

import com.fixit.bo.maps.model.PolygonManipulator;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/17 13:52:40 GMT+2
 */
public class MinMaxFilter implements MapModelFilter {
	
	private final static Integer DEFAULT_VALUE = 0;
	
	private MapModelFilter next;
	private final String key;
	private final Integer min;
	private final Integer max;

	public MinMaxFilter(String key, Integer min, Integer max) {
		this.key = key;
		this.min = min;
		this.max = max;
	}

	@Override
	public MapModelFilter then(MapModelFilter filter) {
		this.next = filter;
		return this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void filter(PolygonManipulator manipulator, List<Polygon> model) {
		for(Polygon polygon : model) {
			Map<String, Object> data = (Map<String, Object>) polygon.getData();
			Integer value = (Integer) data.get(key);
			if(value == null) {
				value = DEFAULT_VALUE;
			}
			if(value >= min && value <= max) {
				manipulator.show(polygon);
			} else {
				manipulator.hide(polygon);
			}
			if(next != null) {
				next.filter(manipulator, model);
			}
		}
	}

}
