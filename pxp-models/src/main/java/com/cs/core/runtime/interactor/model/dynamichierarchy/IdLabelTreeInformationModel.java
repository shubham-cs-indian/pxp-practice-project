package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class IdLabelTreeInformationModel implements IIdLabelTreeInformationModel {
  
  private static final long                    serialVersionUID = 1L;
  
  protected String                             id;
  protected String                             label;
  protected List<IIdLabelTreeInformationModel> children         = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<IIdLabelTreeInformationModel> getChildren()
  {
    return children;
  }
  
  @JsonDeserialize(contentAs = IdLabelTreeInformationModel.class)
  @Override
  public void setChildren(List<IIdLabelTreeInformationModel> children)
  {
    this.children = children;
  }
}
