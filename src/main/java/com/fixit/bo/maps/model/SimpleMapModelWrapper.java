/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.List;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import com.fixit.core.data.mongo.MapArea;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/28 21:01:40 GMT+2
 */
public class SimpleMapModelWrapper extends BaseMapModelWrapper {

	private final MapModel mModel;
	
	public SimpleMapModelWrapper(List<MapArea> mapAreas, LatLng center) {
		this(mapAreas, new Options(), center);
	}
	
	public SimpleMapModelWrapper(List<MapArea> mapAreas, Options options, LatLng center) {
		super(options, center);
		mModel = new MapAreaModel();
		
		for(MapArea mapArea : mapAreas) {
			Polygon polygon = createPolygon(mapArea);
			String mapAreaId = mapArea.get_id().toHexString();
			polygon.setId(mapAreaId);
			mModel.addOverlay(polygon);
		}
	}
	
	@Override
	public boolean onPolygonSelect(OverlaySelectEvent event) {
		return false;
	}
	
	@Override
	public MapModel getModel() {
		return mModel;
	}
	
}
