package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class CreateOrSaveGlobalPermissionModel implements ICreateOrSaveGlobalPermissionModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected String            id;
  protected String            entityId;
  protected IGlobalPermission globalPermission;
  protected List<String>      addedAllowedTemplates;
  protected List<String>      deletedAllowedTemplates;
  protected String            modifiedDefaultTemplate;
  
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
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public List<String> getAddedAllowedTemplates()
  {
    if (addedAllowedTemplates == null) {
      addedAllowedTemplates = new ArrayList<>();
    }
    return addedAllowedTemplates;
  }
  
  @Override
  public void setAddedAllowedTemplates(List<String> addedAllowedTemplates)
  {
    this.addedAllowedTemplates = addedAllowedTemplates;
  }
  
  @Override
  public List<String> getDeletedAllowedTemplates()
  {
    if (deletedAllowedTemplates == null) {
      deletedAllowedTemplates = new ArrayList<>();
    }
    return deletedAllowedTemplates;
  }
  
  @Override
  public void setDeletedAllowedTemplates(List<String> deletedAllowedTemplates)
  {
    this.deletedAllowedTemplates = deletedAllowedTemplates;
  }
  
  @Override
  public String getModifiedDefaultTemplate()
  {
    return modifiedDefaultTemplate;
  }
  
  @Override
  public void setModifiedDefaultTemplate(String modifiedDefaultTemplate)
  {
    this.modifiedDefaultTemplate = modifiedDefaultTemplate;
  }
}
