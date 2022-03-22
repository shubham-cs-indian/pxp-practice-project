package com.cs.core.runtime.interactor.model.variants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.PropertyInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.attribute.AttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeVariantsStatsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RowIdParameterModel implements IRowIdParameterModel {
  
  private static final long                           serialVersionUID = 1L;
  protected String                                    id;
  protected Long                                      versionId;
  protected Map<String, IPropertyInstance>            properties;
  protected String                                    originalInstanceId;
  protected List<String>                              languageCodes;
  protected String                                    creationLanguage;
  protected Map<String, IAttributeVariantsStatsModel> attributeVariantsStats;
  protected IAssetInformationModel                    assetInformation;
  
  @Override
  public IAssetInformationModel getAssetInformation()
  {
    return assetInformation;
  }

  @JsonDeserialize(as = AssetInformationModel.class)
  @Override
  public void setAssetInformation(IAssetInformationModel assetInformation)
  {
    this.assetInformation = assetInformation;
  }

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
  public Map<String, IPropertyInstance> getProperties()
  {
    return properties;
  }
  
  @JsonDeserialize(contentAs = PropertyInstance.class)
  @Override
  public void setProperties(Map<String, IPropertyInstance> properties)
  {
    this.properties = properties;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
  }
  
  @Override
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public String getCreationLanguage()
  {
    return creationLanguage;
  }
  
  @Override
  public void setCreationLanguage(String creationLanguage)
  {
    this.creationLanguage = creationLanguage;
  }
  
  @Override
  public Map<String, IAttributeVariantsStatsModel> getAttributeVariantsStats()
  {
    if (attributeVariantsStats == null) {
      attributeVariantsStats = new HashMap<>();
    }
    return attributeVariantsStats;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeVariantsStatsModel.class)
  public void setAttributeVariantsStats(
      Map<String, IAttributeVariantsStatsModel> attributeVariantsStats)
  {
    this.attributeVariantsStats = attributeVariantsStats;
  }
}
