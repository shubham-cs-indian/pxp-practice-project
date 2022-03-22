package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;

public class GetFilterAndSortDataRequestModel implements IGetFilterAndSortDataRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    typeIds          = new ArrayList<>();
  protected List<String>    attributeIds     = new ArrayList<>();
  protected List<String>    tagIds           = new ArrayList<>();
  protected List<String>    roleIds          = new ArrayList<>();
  protected String          moduleId;
  
  @Override
  public List<String> getTypeIds()
  {
    if(typeIds == null) {
      typeIds = new ArrayList<String>();
    }
    return typeIds;
  }
  
  @Override
  public void setTypeIds(List<String> typeIds)
  {
    this.typeIds = typeIds;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    if(attributeIds == null) {
      attributeIds = new ArrayList<String>();
    }
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public List<String> getTagIds()
  {
    if(tagIds == null) {
      tagIds = new ArrayList<String>();
    }
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
  }
  
  @Override
  public List<String> getRoleIds()
  {
    if(roleIds == null) {
      roleIds = new ArrayList<String>();
    }
    return roleIds;
  }
  
  @Override
  public void setRoleIds(List<String> roleIds)
  {
    this.roleIds = roleIds;
  }
  
  @Override
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }
}
