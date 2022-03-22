package com.cs.core.runtime.interactor.model.dynamichierarchy;

import java.util.List;

public interface IIdLabelTreeInformationModel extends IIdLabelModel {
  
  public static final String ID       = "id";
  public static final String LABEL    = "label";
  public static final String CHILDREN = "children";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IIdLabelTreeInformationModel> getChildren();
  
  public void setChildren(List<IIdLabelTreeInformationModel> children);
}
