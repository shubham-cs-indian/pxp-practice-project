package com.cs.core.runtime.interactor.model.variants;

import java.util.List;
import java.util.Map;

public class VariantReferencedInstancesModel implements IVariantReferencedInstancesModel {
  
  private static final long     serialVersionUID = 1L;
  protected String              name;
  protected String              baseType;
  protected String              id;
  protected String              defaultAssetInstanceId;
  protected List<String>        types;
  protected List<String>        taxonomyIds;
  protected String              thumbKey;
  protected Map<String, String> properties;
  protected String              type;
  
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
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return defaultAssetInstanceId;
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    this.defaultAssetInstanceId = defaultAssetInstanceId;
  }
  
  @Override
  public List<String> getTypes()
  {
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public String getThumbKey()
  {
    return thumbKey;
  }
  
  @Override
  public void setThumbKey(String thumbKey)
  {
    this.thumbKey = thumbKey;
  }
  
  @Override
  public Map<String, String> getProperties()
  {
    return properties;
  }
  
  @Override
  public void setProperties(Map<String, String> properties)
  {
    this.properties = properties;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
}
