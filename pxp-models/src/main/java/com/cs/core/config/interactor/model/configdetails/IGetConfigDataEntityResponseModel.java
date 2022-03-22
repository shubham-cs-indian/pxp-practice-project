package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDataEntityResponseModel extends IModel {
  
  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  public static final String TOTAL_COUNT = "totalCount";
  public static final String LIST        = "list";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public Long getTotalCount();
  
  public void setTotalCount(Long totalCount);
  
  public List<IConfigEntityInformationModel> getList();
  
  public void setList(List<IConfigEntityInformationModel> list);
}
