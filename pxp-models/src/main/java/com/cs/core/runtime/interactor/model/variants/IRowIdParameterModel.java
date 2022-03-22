package com.cs.core.runtime.interactor.model.variants;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRowIdParameterModel extends IModel {
  
  public static final String ID                       = "id";
  public static final String VERSION_ID               = "versionId";
  public static final String PROPERTIES               = "properties";
  public static final String ORIGINAL_INSTANCE_ID     = "originalInstanceId";
  public static final String LANGUAGE_CODES           = "languageCodes";
  public static final String CREATION_LANGUAGE        = "creationLanguage";
  public static final String ATTRIBUTE_VARIANTS_STATS = "attributeVariantsStats";
  public static final String ASSET_INFORMATION        = "assetInformation";
  
  public String getId();
  
  public void setId(String id);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public Map<String, IPropertyInstance> getProperties();
  
  public void setProperties(Map<String, IPropertyInstance> properties);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getCreationLanguage();
  
  public void setCreationLanguage(String creationLanguage);
  
  public Map<String, IAttributeVariantsStatsModel> getAttributeVariantsStats();
  
  public void setAttributeVariantsStats(
      Map<String, IAttributeVariantsStatsModel> attributeVariantsStats);
  
  public IAssetInformationModel getAssetInformation();
  
  public void setAssetInformation(IAssetInformationModel assetInformation);
}
