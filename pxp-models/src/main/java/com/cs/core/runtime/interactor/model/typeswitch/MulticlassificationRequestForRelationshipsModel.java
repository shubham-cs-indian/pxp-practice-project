package com.cs.core.runtime.interactor.model.typeswitch;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestForRelationshipsModel;

import java.util.ArrayList;
import java.util.List;

public class MulticlassificationRequestForRelationshipsModel extends MulticlassificationRequestModel
    implements IMulticlassificationRequestForRelationshipsModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    xRayAttributes;
  protected List<String>    xRayTags;
  protected String          sideId;
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public List<String> getXRayAttributes()
  {
    if (xRayAttributes == null) {
      xRayAttributes = new ArrayList<>();
    }
    return xRayAttributes;
  }
  
  @Override
  public void setXRayAttributes(List<String> xRayAttributes)
  {
    this.xRayAttributes = xRayAttributes;
  }
  
  @Override
  public List<String> getXRayTags()
  {
    if (xRayTags == null) {
      xRayTags = new ArrayList<>();
    }
    return xRayTags;
  }
  
  @Override
  public void setXRayTags(List<String> xRayTags)
  {
    this.xRayTags = xRayTags;
  }
}
