/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/28 23:16:20 GMT+2
 */
public class SelectableMapModelWrapper extends MapModelWrapperDelegate {

	private final List<String> mSelectedAreaIds = new ArrayList<>();
	
	public SelectableMapModelWrapper(MapModelWrapper parentWrapper) {
		this(parentWrapper, null);
	}
	
	public SelectableMapModelWrapper(MapModelWrapper parentWrapper, List<String> selectedAreaIds) {
		super(parentWrapper);	
		if(selectedAreaIds != null) {
			setSelectedAreas(selectedAreaIds);
		}
	}
	
	public List<String> getSelectedAreas() {
		return mSelectedAreaIds;
	}
	
	public void setSelectedAreas(List<String> selectedAreasIds) {
		clearSelectedAreas();
		
		MapModel mapModel = getModel();
		for(String selectedArea : selectedAreasIds) {
			Polygon polygon = (Polygon) mapModel.findOverlay(selectedArea);
			if(polygon != null) {
				select(polygon);
				mSelectedAreaIds.add(selectedArea);
			}
		}
	}
	
	public void clearSelectedAreas() {
		MapModel mapModel = getModel();
		for(String selectedArea : mSelectedAreaIds) {
			Polygon polygon = (Polygon) mapModel.findOverlay(selectedArea);
			if(polygon != null) {
				unselect(polygon);
			}
		}
		mSelectedAreaIds.clear();
	}

	@Override
	public boolean onPolygonSelect(OverlaySelectEvent event) {
		if(!super.onPolygonSelect(event)) {
			Polygon polygon = (Polygon) event.getOverlay();
			String id = event.getOverlay().getId();
			if(mSelectedAreaIds.contains(id)) {
				mSelectedAreaIds.remove(id);
				unselect(polygon);
			} else {
				mSelectedAreaIds.add(id);
				select(polygon);
			}
		} 
		return true;
	}
	
}
