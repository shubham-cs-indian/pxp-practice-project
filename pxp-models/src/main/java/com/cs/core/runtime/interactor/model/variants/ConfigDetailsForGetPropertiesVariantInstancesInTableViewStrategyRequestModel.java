package com.cs.core.runtime.interactor.model.variants;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel
    implements IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          attributeId;
  protected String          contextId;
  protected String          currentUserId;
  protected List<String>    klassIds;
  protected String          templateId;
  protected List<String>    taxonomyIds;
  protected String          natureKlassId;
  
  @Override
  public String getTemplateId()
  {
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
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
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public String getNatureKlassId()
  {
    return natureKlassId;
  }
  
  @Override
  public void setNatureKlassId(String natureKlassId)
  {
    this.natureKlassId = natureKlassId;
  }
}
