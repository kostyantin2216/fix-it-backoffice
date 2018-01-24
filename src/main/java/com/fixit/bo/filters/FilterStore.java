/**
 * 
 */
package com.fixit.bo.filters;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/22 23:50:03 GMT+2
 */
public class FilterStore<E> {
	
	private final List<E> mOriginalData;
	private List<E> mFilteredData;
	
	private Filter<E> mFilter;
	private Comparator<E> mComparator;
	
	public FilterStore(List<E> data, Filter<E> filter) {
		this(data, filter, null);
	}
	
	public FilterStore(List<E> data, Filter<E> filter, Comparator<E> comparator) {
		mOriginalData = data;
		mFilter = filter;
		mComparator = comparator;
		applyFilter(filter, true);
	}
	
	public List<E> getData() {
		return mFilteredData;
	}
	
	public Filter<E> getFilter() {
		return mFilter;
	}
	
	public void applyFilter(Filter<E> filter) {
		applyFilter(filter, false);
	}
	
	public void applyFilter(Filter<E> filter, boolean force) {
		if(mFilter.update(filter) || force) {
			mFilteredData = mOriginalData.stream()
										 .filter(o -> mFilter.isFiltered(o))
										 .collect(Collectors.toList());
			if(mComparator != null) {
				mFilteredData.sort(mComparator);
			}
		}
	}
	
	public void addOrder(E order) {
		mOriginalData.add(order);
		if(mFilter.isFiltered(order)) {
			mFilteredData.add(order);
		}
	}
	
	public void removeOrder(E order) {
		mOriginalData.remove(order);
		mFilteredData.remove(order);
	}
	
	public void updateOrder(E order) {
		if(mFilter.isFiltered(order)) {
			if(!mFilteredData.contains(order)) {
				mFilteredData.add(order);
			}
		} else {
			mFilteredData.remove(order);
		}
	}

}
