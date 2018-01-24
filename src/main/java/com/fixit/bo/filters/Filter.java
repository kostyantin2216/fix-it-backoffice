/**
 * 
 */
package com.fixit.bo.filters;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/22 23:47:24 GMT+2
 */
public interface Filter<E> {
	public boolean update(Filter<E> filter);
	public boolean isFiltered(E entity);
}
