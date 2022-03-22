package com.cs.core.config.interactor.model.asset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class AssetConfigurationDetailsResponseModel
    implements IAssetConfigurationDetailsResponseModel {
  
  private static final long                                      serialVersionUID        = 1L;
  protected boolean                                              detectDuplicate         = false;
  protected boolean                                              uploadZip               = false;
  protected String                                               natureType;
  protected Map<String, List<IAssetExtensionConfigurationModel>> extensionConfiguration;
  protected String                                               klassId;
  protected boolean                                              isIndesignServerEnabled = false;
  protected boolean                                              isAutoCreateTIVExist    = false;
  
  @Override
  public boolean getDetectDuplicate()
  {
    return detectDuplicate;
  }
  
  @Override
  public void setDetectDuplicate(boolean detectDuplicate)
  {
    this.detectDuplicate = detectDuplicate;
  }
  
  @Override
  public boolean getUploadZip()
  {
    return uploadZip;
  }
  
  @Override
  public void setUploadZip(boolean uploadZip)
  {
    this.uploadZip = uploadZip;
  }
  
  @Override
  public String getNatureType()
  {
    
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    
    this.natureType = natureType;
  }
  
  @Override
  public Map<String, List<IAssetExtensionConfigurationModel>> getExtensionConfiguration()
  {
    return extensionConfiguration;
  }
  
  @Override
  @JsonDeserialize(contentUsing = AssetExtensionConfigurationCustomDeserializer.class)
  public void setExtensionConfiguration(
      Map<String, List<IAssetExtensionConfigurationModel>> extensionConfiguration)
  {
    this.extensionConfiguration = extensionConfiguration;
  }
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public boolean getIsIndesignServerEnabled()
  {
    return isIndesignServerEnabled;
  }
  
  @Override
  public void setIsIndesignServerEnabled(boolean isIndesignServerEnabled)
  {
    this.isIndesignServerEnabled = isIndesignServerEnabled;
  }

  @Override
  public boolean getIsAutoCreateTIVExist()
  {
    return isAutoCreateTIVExist;
  }

  @Override
  public void setIsAutoCreateTIVExist(boolean isAutoCreateTIVExist)
  {
    this.isAutoCreateTIVExist = isAutoCreateTIVExist;
  }
}
