package com.cs.core.config.interactor.model.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;


public class CreateAssetInstanceAfterUploadRequestModel extends CreateInstanceModel
    implements ICreateAssetInstanceAfterUploadRequestModel {
  
  private static final long                                   serialVersionUID             = 1L;
  private IAssetKeysModel                                     assetKeysModel;
  private IGetConfigDetailsForCustomTabModel                  configDetails;
  private IAssetConfigurationDetailsResponseModel             assetConfigDetails;
  private Map<String, IGetConfigDetailsForCreateVariantModel> tivConfigDetails             = new HashMap<>();
  private Map<Long, Object>                                   tivDuplicateDetectionInfoMap = new HashMap<>();
  private List<String>                                        tivSuccess                   = new ArrayList<>();
  private List<String>                                        tivWarning                   = new ArrayList<>();
  private List<String>                                        tivFailure                   = new ArrayList<>();
  private boolean                                             isDuplicate                  = false;
  
  @Override
  public IAssetKeysModel getAssetKeysModel()
  {
    return assetKeysModel;
  }
  
  @Override
  public void setAssetKeysModel(IAssetKeysModel assetKeysModel)
  {
    this.assetKeysModel = assetKeysModel;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.configDetails = configDetails;
  }

  @Override
  public IAssetConfigurationDetailsResponseModel getAssetConfigDetails()
  {
    return assetConfigDetails;
  }

  @Override
  public void setAssetConfigDetails(IAssetConfigurationDetailsResponseModel assetConfigDetails)
  {
    this.assetConfigDetails = assetConfigDetails;
  }

  @Override
  public Map<String, IGetConfigDetailsForCreateVariantModel> getTivConfigDetails()
  {
    return tivConfigDetails;
  }

  @Override
  public void setTivConfigDetails(Map<String, IGetConfigDetailsForCreateVariantModel> tivConfigDetails)
  {
    this.tivConfigDetails = tivConfigDetails;
  }

  @Override
  public Map<Long, Object> getTivDuplicateDetectionInfoMap()
  {
    return tivDuplicateDetectionInfoMap;
  }
  
  @Override
  public void setTivDuplicateDetectionInfoMap(Map<Long, Object> tivDuplicateDetectionInfoMap)
  {
    this.tivDuplicateDetectionInfoMap = tivDuplicateDetectionInfoMap;
  }
  
  @Override
  public List<String> getTivSuccess()
  {
    return tivSuccess;
  }
  
  @Override
  public void setTivSuccess(List<String> tivSuccess)
  {
    this.tivSuccess = tivSuccess;
  }
  
  @Override
  public List<String> getTivWarning()
  {
    return tivWarning;
  }
  
  @Override
  public void setTivWarning(List<String> tivWarning)
  {
    this.tivWarning = tivWarning;
  }
  
  @Override
  public List<String> getTivFailure()
  {
    return tivFailure;
  }
  
  @Override
  public void setTivFailure(List<String> tivFailure)
  {
    this.tivFailure = tivFailure;
  }

  @Override
  public boolean getIsDuplicate()
  {
    return isDuplicate;
  }

  @Override
  public void setIsDuplicate(boolean isDuplicate)
  {
    this.isDuplicate = isDuplicate;
  }
  
}
