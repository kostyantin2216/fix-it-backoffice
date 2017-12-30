/**
 * 
 */
package com.fixit.bo.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.springframework.util.StringUtils;

import com.fixit.core.dao.sql.DataCommentDao;
import com.fixit.core.data.sql.DataComment;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/27 13:06:37 GMT+2
 */
@ManagedBean
@SessionScoped
public class DataCommentsBean implements Serializable {
	private static final long serialVersionUID = 8938713438531746844L;

	@ManagedProperty(value = "#{dataCommentDao}")
	private transient DataCommentDao mCommentDao;
	
	private final Map<String, List<DataComment>> mCommentCache = new HashMap<>();
	
	private DataComment mEditableComment;

	public void setmCommentDao(DataCommentDao mCommentDao) {
		this.mCommentDao = mCommentDao;
	}

	public DataComment getEditableComment() {
		return mEditableComment;
	}

	public void setEditableComment(DataComment editableComment) {
		this.mEditableComment = editableComment;
	}
	
	@PostConstruct
	public void init() {
		mEditableComment = new DataComment();
	}
	
	public List<DataComment> getComments(Integer dataType, String dataId) {
		String key = getKey(dataType, dataId);
		List<DataComment> comments = mCommentCache.get(key);
		if(comments == null) {
			comments = mCommentDao.findForData(dataType, dataId);
			comments.sort(DataComment.CREATION_DATE_COMPARATOR);
			mCommentCache.put(key, comments);
		}
		return comments;
	}
	
	public void saveEditableComment(Integer dataType, String dataId) {
		if(!StringUtils.isEmpty(mEditableComment.getContent())) {
			mEditableComment.setDataType(dataType);
			mEditableComment.setDataTypeId(dataId);
			mEditableComment.setCreatedAt(new Date());
			
			mCommentDao.save(mEditableComment);
			
			String key = getKey(dataType, dataId);
			List<DataComment> comments = mCommentCache.get(key);
			if(comments == null) {
				comments = new ArrayList<>();
				mCommentCache.put(key, comments);
			}
			comments.add(0, mEditableComment);
			mEditableComment = new DataComment();
		}
	}
	
	public void deleteComment(DataComment comment) {
		mCommentDao.delete(comment.getId());
		
		String key = getKey(comment.getDataType(), comment.getDataTypeId());
		List<DataComment> comments = mCommentCache.get(key);
		if(comments != null) {
			comments.remove(comment);
		}
	}
	
	private String getKey(Integer dataType, String dataId) {
		return dataType + "-" + dataId;
	}
	
}
