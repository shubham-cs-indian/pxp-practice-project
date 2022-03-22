package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.filter.ISortModel;

import java.util.ArrayList;
import java.util.List;

public class KlassInstanceTableViewGetPropertiesModel
    implements IKlassInstanceTableViewGetPropertiesModel {
  
  protected List<String>     attributeIds = new ArrayList<>();
  
  protected List<String>     tagIds       = new ArrayList<>();
  
  protected List<String>     roleIds      = new ArrayList<>();
  
  protected List<String>     ids          = new ArrayList<>();
  
  protected String           klassInstanceId;
  
  protected List<ISortModel> sortOptions;
  
  @Override
  public List<String> getAttributeIds()
  {
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
    return roleIds;
  }
  
  @Override
  public void setRoleIds(List<String> roleIds)
  {
    this.roleIds = roleIds;
  }
  
  @Override
  public List<String> getIds()
  {
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public List<ISortModel> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
    }
    return sortOptions;
  }
  
  @Override
  public void setSortOptions(List<ISortModel> sortOptions)
  {
    this.sortOptions = sortOptions;
  }
}
