/**
 * 
 */
package com.fixit.bo.maps.filters;

import java.util.List;

import org.primefaces.model.map.Polygon;

import com.fixit.bo.maps.model.PolygonManipulator;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/17 13:49:35 GMT+2
 */
public interface MapModelFilter {

	public void filter(PolygonManipulator polygonManipulator, List<Polygon> model);
	
	MapModelFilter then(MapModelFilter filter);
	
}
