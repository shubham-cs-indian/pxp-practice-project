package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ConfigTaskContentTypeModel implements IConfigTaskContentTypeModel {
  
  private static final long                                serialVersionUID = 1L;
  protected Map<String, IConfigTaskReferenceResponseModel> referencedKlasses;
  protected Map<String, IConfigEntityInformationModel>     referencedRoles;
  protected Map<String, IConfigEntityInformationModel>     referencedUsers;
  
  @Override
  public Map<String, IConfigTaskReferenceResponseModel> getReferencedKlasses()
  {
    return this.referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = ConfigTaskReferenceResponseModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IConfigTaskReferenceResponseModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  public Map<String, IConfigEntityInformationModel> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedRoles(Map<String, IConfigEntityInformationModel> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  public Map<String, IConfigEntityInformationModel> getReferencedUsers()
  {
    return referencedUsers;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedUsers(Map<String, IConfigEntityInformationModel> referencedUsers)
  {
    this.referencedUsers = referencedUsers;
  }
}
