package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DiAttributeVariantModel implements IDiAttributeVariantModel {
  
  private static final long                  serialVersionUID = 1L;
  private String                             variantId;
  private String                             klassInstanceId;
  private String                             parentId;
  private String                             label;
  private IDiAttributeVariantContextModel    context;
  private IDiAttributeVariantPropertiesModel properties;
  private String                             languageCode;
  
  /* @Override
  public String getLanguageCode() {
  	return languageCode;
  }
  
  @Override
  public void setLanguageCode(String languageCode) {
  	this.languageCode = languageCode;
  }*/
  
  @Override
  public String getVariantId()
  {
    return this.variantId;
  }
  
  @Override
  public void setVariantId(String variantId)
  {
    this.variantId = variantId;
  }
  
  @Override
  public String getContentId()
  {
    return this.klassInstanceId;
  }
  
  @Override
  public void setContentId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getParentId()
  {
    return this.parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public IDiAttributeVariantContextModel getContext()
  {
    if (this.context == null) {
      this.context = new DiAttributeVariantContextModel();
    }
    return this.context;
  }
  
  @Override
  @JsonDeserialize(as = DiAttributeVariantContextModel.class)
  public void setContext(IDiAttributeVariantContextModel context)
  {
    this.context = context;
  }
  
  @Override
  public IDiAttributeVariantPropertiesModel getProperties()
  {
    if (this.properties == null) {
      this.properties = new DiAttributeVariantPropertiesModel();
    }
    return this.properties;
  }
  
  @Override
  @JsonDeserialize(as = DiAttributeVariantPropertiesModel.class)
  public void setProperties(IDiAttributeVariantPropertiesModel properties)
  {
    this.properties = properties;
  }
}
