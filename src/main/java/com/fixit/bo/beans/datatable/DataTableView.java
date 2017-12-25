package com.fixit.bo.beans.datatable;

import java.util.List;

import com.fixit.core.dao.CommonDao;
import com.fixit.core.data.DataModelObject;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/27 19:33:49 GMT+3
 */
public abstract class DataTableView<DMO extends DataModelObject<?>, DAO extends CommonDao<DMO, ?>> {
	
	private List<DMO> mTableData;
	
	private final CommonDao<DMO, ?> mDao;
	
	public DataTableView(CommonDao<DMO, ?> dao) {
		mDao = dao;
	}

	public List<DMO> getTableData() {
		if(mTableData == null) {
			mTableData = mDao.findAll();
		}
		return mTableData;
	}
	
}
