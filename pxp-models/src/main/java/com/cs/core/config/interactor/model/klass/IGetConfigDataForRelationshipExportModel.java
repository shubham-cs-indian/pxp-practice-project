package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;

public interface IGetConfigDataForRelationshipExportModel extends IGetConfigDataRequestModel {
  
  public static final String KLASS_ID      = "klassId";
  public static final String FROM          = "from";
  public static final String SIZE          = "size";
  public static final String SORT_BY       = "sortBy";
  public static final String SORT_ORDER    = "sortOrder";
  
  public String getKlassId();
  public void setKlassId(String klassId);
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getSize();
  public void setSize(Long size);
  
  public String getSortBy();
  public void setSortBy(String sortBy);
  
  public String getSortOrder();
  public void setSortOrder(String sortOrder);
}
