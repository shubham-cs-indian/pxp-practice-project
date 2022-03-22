package com.cs.core.config.interactor.model.asset;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;


public interface ICreateAssetInstanceAfterUploadRequestModel extends ICreateInstanceModel {
  
  public static final String ASSET_KEYS_MODEL                 = "assetKeysModel";
  public static final String CONFIG_DETAILS                   = "configDetails";
  public static final String ASSET_CONFIG_DETAILS             = "assetConfigDetails";
  public static final String TIV_CONFIG_DETAILS               = "tivConfigDetails";
  public static final String TIV_DUPLICATE_DETECTION_INFO_MAP = "tivDuplicateDetectionInfoMap";
  public static final String TIV_SUCCESS                      = "tivSuccess";
  public static final String TIV_WARNING                      = "tivWarning";
  public static final String TIV_FAILURE                      = "tivFailure";
  public static final String IS_DUPLICATE                     = "isDuplicate";
  
  public IAssetKeysModel getAssetKeysModel();
  public void setAssetKeysModel(IAssetKeysModel assetKeysModel);
  
  public IGetConfigDetailsForCustomTabModel getConfigDetails();
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails);
  
  public IAssetConfigurationDetailsResponseModel getAssetConfigDetails();
  public void setAssetConfigDetails(IAssetConfigurationDetailsResponseModel assetConfigDetails);
  
  public Map<String, IGetConfigDetailsForCreateVariantModel> getTivConfigDetails();
  public void setTivConfigDetails(Map<String, IGetConfigDetailsForCreateVariantModel> tivConfigDetails);
  
  public Map<Long, Object> getTivDuplicateDetectionInfoMap();
  public void setTivDuplicateDetectionInfoMap(Map<Long, Object> tivDuplicateDetectionInfoMap);

  public List<String> getTivSuccess();
  public void setTivSuccess(List<String> tivSuccess);
  
  public List<String> getTivWarning();
  public void setTivWarning(List<String> tivWarning);
  
  public List<String> getTivFailure();
  public void setTivFailure(List<String> tivFailure);
  
  public boolean getIsDuplicate();
  public void setIsDuplicate(boolean isDuplicate);
  
}
