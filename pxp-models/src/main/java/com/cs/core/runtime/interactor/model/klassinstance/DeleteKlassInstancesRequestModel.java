package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DeleteKlassInstancesRequestModel implements IDeleteKlassInstancesRequestModel {
  
  protected Boolean                        hasDeletePermission;
  protected Map<String, IGlobalPermission> globalPermissions;
  protected List<IDeleteInstanceDetails>   allDeleteInstanceDetails;
  protected Boolean                        isDeleteFromArchive;

  
  @Override
  public Boolean getHasDeletePermission()
  {
    return hasDeletePermission;
  }
  
  @Override
  public void setHasDeletePermission(Boolean hasDeletePermission)
  {
    this.hasDeletePermission = hasDeletePermission;
  }
  
  @Override
  public Map<String, IGlobalPermission> getGlobalPermissions()
  {
    return globalPermissions;
  }
  
  @Override
  public void setGlobalPermissions(Map<String, IGlobalPermission> globalPermissions)
  {
    this.globalPermissions = globalPermissions;
  }
  
  @Override
  public List<IDeleteInstanceDetails> getAllDeleteInstanceDetails()
  {
    return allDeleteInstanceDetails;
  }
  
  @Override
  @JsonDeserialize(contentAs = DeleteInstanceDetails.class)
  public void setAllDeleteInstanceDetails(List<IDeleteInstanceDetails> allDeleteInstanceDetails)
  {
    this.allDeleteInstanceDetails = allDeleteInstanceDetails;
  }
  
  @Override
  public Boolean getIsDeleteFromArchive()
  {
    return isDeleteFromArchive;
  }
  
  @Override
  public void setIsDeleteFromArchive(Boolean isDeleteFromArchive)
  {
    this.isDeleteFromArchive = isDeleteFromArchive;
  }
  
}
