package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetStaticCollectionDetailsModel extends IModel {
  
  public static final String SECTIONS = "sections";
  public static final String ID       = "id";
  public static final String LABEL    = "label";
  public static final String CHILDREN = "children";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<ICollectionModel> getChildren();
  
  public void setChildren(List<ICollectionModel> children);
  
  public List<? extends ISection> getSections();
  
  public void setSections(List<? extends ISection> sections);
}
