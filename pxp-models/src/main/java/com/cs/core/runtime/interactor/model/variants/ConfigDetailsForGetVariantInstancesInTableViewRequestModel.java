package com.cs.core.runtime.interactor.model.variants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigDetailsForGetVariantInstancesInTableViewRequestModel
    implements IConfigDetailsForGetVariantInstancesInTableViewRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          contextId;
  protected String          userId;
  protected List<String>    propertyIds;
  protected List<String>    parentKlassIds;
  protected List<String>    parentTaxonomyIds;
  protected String          baseType;
  protected String          moduleId;
  protected Map<Long, Map<String, List<String>>> instanceIdVsOtherClassifiers;
  
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
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public List<String> getPropertyIds()
  {
    if (propertyIds == null) {
      propertyIds = new ArrayList<String>();
    }
    return propertyIds;
  }
  
  @Override
  public void setPropertyIds(List<String> propertyIds)
  {
    this.propertyIds = propertyIds;
  }
  
  @Override
  public List<String> getParentKlassIds()
  {
    if (parentKlassIds == null) {
      parentKlassIds = new ArrayList<>();
    }
    return parentKlassIds;
  }
  
  @Override
  public void setParentKlassIds(List<String> parentKlassIds)
  {
    this.parentKlassIds = parentKlassIds;
  }
  
  @Override
  public List<String> getParentTaxonomyIds()
  {
    if (parentTaxonomyIds == null) {
      parentTaxonomyIds = new ArrayList<>();
    }
    return parentTaxonomyIds;
  }
  
  @Override
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds)
  {
    this.parentTaxonomyIds = parentTaxonomyIds;
  }
  
  @Override
  public String getBaseType()
  {
    // TODO Auto-generated method stub
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Map<Long, Map<String, List<String>>> getInstanceIdVsOtherClassifiers()
  {
    return this.instanceIdVsOtherClassifiers;
  }
  
  @Override
  public void setInstanceIdVsOtherClassifiers(Map<Long, Map<String, List<String>>> instanceIdVsOtherClassifiers)
  {
    this.instanceIdVsOtherClassifiers = instanceIdVsOtherClassifiers;
  }
}
