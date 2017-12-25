/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.Map;

import org.primefaces.model.map.Overlay;

import com.fixit.bo.maps.filters.MapModelFilterer;
import com.fixit.bo.maps.filters.MapModelFilterer.FilterType;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/16 23:12:21 GMT+2
 */
public class FilterableMapModelWrapper extends MapModelWrapperDelegate {
	
	private final MapModelFilterer mFilterer;
	private final FilterDataLoader mFilterDataLoader;
	
	public FilterableMapModelWrapper(MapModelWrapper parentWrapper, FilterDataLoader filterDataLoader) {
		super(parentWrapper);
		mFilterDataLoader = filterDataLoader;
		mFilterer = new MapModelFilterer(this);
	}
	 
	public FilterableMapModelWrapper(MapModelWrapper parentWrapper) {
		this(parentWrapper, null);
	}

	public void clearFilters() {
		mFilterer.clear(getModel().getPolygons());
	}
	
	public void filter(FilterType filterType, Integer min, Integer max) {
		if(mFilterDataLoader != null) {
			if(!mFilterer.containsFilter(getModel().getPolygons(), filterType)) {
				loadFilterData(filterType);
			}
			mFilterer.filter(getModel().getPolygons(), filterType, min, max);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadFilterData(FilterType filterType) {
		Map<String, Object> filterData = mFilterDataLoader.loadFilterData(filterType);
		for(Map.Entry<String, Object> filterEntry : filterData.entrySet()) {
			Overlay overlay = getModel().findOverlay(filterEntry.getKey());
			if(overlay != null) {
				((Map<String, Object>) overlay.getData()).put(filterType.getKey(), filterEntry.getValue());
			}
		}
	}
	
	public interface FilterDataLoader {
		Map<String, Object> loadFilterData(FilterType filterType);
	}
	
}
