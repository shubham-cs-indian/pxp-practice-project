package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.klass.IModifiedSectionPermissionModel;
import com.cs.core.config.interactor.model.klass.ModifiedSectionPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ModifiedSectionModel implements IModifiedSectionModel {
  
  protected String                                id;
  protected int                                   sequence;
  protected List<IModifiedSectionPermissionModel> modifiedPermissions;
  protected Boolean                               isModified = false;
  protected Boolean                               isSkipped  = false;
  
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
  public int getSequence()
  {
    return sequence;
  }
  
  @Override
  public void setSequence(int sequence)
  {
    this.sequence = sequence;
  }
  
  @Override
  public List<IModifiedSectionPermissionModel> getModifiedPermissions()
  {
    if (modifiedPermissions == null) {
      modifiedPermissions = new ArrayList<IModifiedSectionPermissionModel>();
    }
    
    return modifiedPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedSectionPermissionModel.class)
  public void setModifiedPermissions(List<IModifiedSectionPermissionModel> modifiedPermissions)
  {
    this.modifiedPermissions = modifiedPermissions;
  }
  
  @Override
  public Boolean getIsModified()
  {
    return isModified;
  }
  
  @Override
  public void setIsModified(Boolean isModified)
  {
    this.isModified = isModified;
  }
  
  @Override
  public Boolean getIsSkipped()
  {
    return isSkipped;
  }
  
  @Override
  public void setIsSkipped(Boolean isSkipped)
  {
    this.isSkipped = isSkipped;
  }
}
