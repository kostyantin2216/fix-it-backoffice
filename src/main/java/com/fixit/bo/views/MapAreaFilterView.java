/**
 * 
 */
package com.fixit.bo.views;

import java.util.List;
import java.util.Optional;

import com.fixit.bo.maps.filters.MapModelFilterer.FilterType;
import com.fixit.bo.maps.model.FilterableMapModelWrapper;
import com.fixit.core.logging.FILog;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/17 15:53:18 GMT+2
 */
public class MapAreaFilterView {

	private List<MapAreaFilter> filters;
	
	public MapAreaFilterView(List<MapAreaFilter> filters) {
		this.filters = filters;
	}
	
	public List<MapAreaFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<MapAreaFilter> filters) {
		this.filters = filters;
	}

	public void applyFilters(FilterableMapModelWrapper mapModel) {
		for(MapAreaFilter filter : filters) {
			if(filter.isApplyable()) {
				Optional<FilterType> filterTypeOpt = FilterType.forKey(filter.name);
				if(filterTypeOpt.isPresent()) {
					mapModel.filter(filterTypeOpt.get(), filter.min, filter.max);
				} else {
					FILog.w("Could not find FilterType for MapAreaFilterView::applyFilters");
				}
			}
		}
	}

	public void reset() {
		for(MapAreaFilter filter : filters) {
			filter.active = false;
		}
	}
	
	public static class MapAreaFilter {
		private String name;
		private boolean active;
		private Integer min;
		private Integer max;
		public MapAreaFilter() { }
		public MapAreaFilter(String name) {
			this.name = name;
			this.active = false;
			this.min = null;
			this.max = null;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		public Integer getMin() {
			return min;
		}
		public void setMin(Integer min) {
			this.min = min;
		}
		public Integer getMax() {
			return max;
		}
		public void setMax(Integer max) {
			this.max = max;
		}
		public boolean isApplyable() {
			return active && min != null && max != null;
		}
	}
	
}
