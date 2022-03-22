package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IAssetConfigurationDetailsResponseModel extends IModel {
  
  public static final String DETECT_DUPLICATE           = "detectDuplicate";
  public static final String UPLOAD_ZIP                 = "uploadZip";
  public static final String NATURE_TYPE                = "natureType";
  public static final String EXTENSION_CONFIGURATION    = "extensionConfiguration";
  public static final String KLASS_ID                   = "klassId";
  public static final String IS_INDESIGN_SERVER_ENABLED = "isIndesignServerEnabled";
  public static final String IS_AUTO_CREATE_TIV_EXIST   = "isAutoCreateTIVExist";
  
  public boolean getDetectDuplicate();
  
  public void setDetectDuplicate(boolean detectDuplicate);
  
  public boolean getUploadZip();
  
  public void setUploadZip(boolean uploadZip);
  
  public String getNatureType();
  
  public void setNatureType(String natureType);
  
  public Map<String, List<IAssetExtensionConfigurationModel>> getExtensionConfiguration();
  
  public void setExtensionConfiguration(
      Map<String, List<IAssetExtensionConfigurationModel>> extensionConfiguration);
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public boolean getIsIndesignServerEnabled();
  
  public void setIsIndesignServerEnabled(boolean isIndesignServerEnabled);
  
  public boolean getIsAutoCreateTIVExist();
  public void setIsAutoCreateTIVExist(boolean isAutoCreateTIVExist);
}
