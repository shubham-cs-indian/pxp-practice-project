package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DiEmbeddedVariantsModel implements IDiEmbeddedVariantsModel {
  
  private static final long               serialVersionUID = 1L;
  private String                          variantId;
  private String                          klassInstanceId;
  private String                          parentId;
  private String                          label;
  private String                          languageCode;
  private List<String>                    klassTypeIds;
  private List<String>                    taxonomyIds;
  private IDiAttributeVariantContextModel context;
  private IDiPropertiesModel              properties;
  
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
  public List<String> getTypeIds()
  {
    if (this.klassTypeIds == null) {
      klassTypeIds = new ArrayList<>();
    }
    return this.klassTypeIds;
  }
  
  @Override
  public void setTypeIds(List<String> klassTypeIds)
  {
    this.klassTypeIds = klassTypeIds;
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
  public IDiPropertiesModel getProperties()
  {
    if (this.properties == null) {
      this.properties = new DiPropertiesModel();
    }
    return this.properties;
  }
  
  @Override
  @JsonDeserialize(as = DiPropertiesModel.class)
  public void setProperties(IDiPropertiesModel properties)
  {
    this.properties = properties;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }
  
  @Override
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
  }
}
