package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeIconModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IReferencedPropertyModel extends IModel {
  
  String ID            = "id";
  String TYPE          = "type";
  String CODE          = "code";
  String CHILDREN      = "children";
  String PRECISION     = "precision";
  String HIDE_SEPARATOR = "hideSeparator";
  
  public String getId();
  public void setId(String id);
  
  public String getType();
  public void setType(String type);
  
  public String getCode();
  public void setCode(String code);
  
  public List<IIdLabelCodeIconModel> getChildren();
  public void setChildren(List<IIdLabelCodeIconModel> children);
  
  public Integer getPrecision();
  public void setPrecision(Integer precision);
  
  public Boolean getHideSeparator();
  public void setHideSeparator(Boolean hideSeparator);
  
}
