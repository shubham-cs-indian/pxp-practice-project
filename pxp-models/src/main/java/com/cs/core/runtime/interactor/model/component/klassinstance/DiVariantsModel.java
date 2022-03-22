package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DiVariantsModel implements IDiVariantsModel {
  
  private static final long  serialVersionUID = 1L;
  
  private String             variantId;
  private String             parentId;
  private List<String>       klassTypeIds;
  private IDiPropertiesModel properties;
  
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
  public List<String> getKlassTypeIds()
  {
    if (this.klassTypeIds == null) {
      klassTypeIds = new ArrayList<>();
    }
    return this.klassTypeIds;
  }
  
  @Override
  public void setKlassTypeIds(List<String> klassTypeIds)
  {
    this.klassTypeIds = klassTypeIds;
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
}
