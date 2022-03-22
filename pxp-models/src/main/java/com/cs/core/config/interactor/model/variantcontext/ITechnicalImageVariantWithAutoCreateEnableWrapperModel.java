package com.cs.core.config.interactor.model.variantcontext;

import java.util.List;

import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITechnicalImageVariantWithAutoCreateEnableWrapperModel extends IModel {
  
  public static final String TECHNICAL_IMAGE_VARIANT_WITH_AUTO_CREATE_ENABLE = "technicalImageVariantWithAutoCreateEnable";
  public static final String FILE_NAME                                       = "fileName";
  public static final String IS_SAVE                                         = "isSave";
  public static final String ATTRIBUTE                                       = "attribute";
  public static final String INSTANCEID                                      = "instanceId";
  public static final String PARENT_ID                                       = "parentId";
  public static final String ASSET_CONFIGURATION_MODEL                       = "assetConfigurationModel";
  public static final String THUMBNAIL_PATH                                  = "thumbnailPath";
  public static final String MAIN_ASSET_INSTANCE_SOURCE_PATH                 = "mainAssetInstanceSourcePath";
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantWithAutoCreateEnable();
  
  public void setTechnicalImageVariantWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantWithAutoCreateEnable);
  
  public String getFileName();
  
  public void setFileName(String fileName);
  
  public Boolean getIsSave();
  
  public void setIsSave(Boolean isSave);
  
  public IAssetInformationModel getAttribute();
  
  public void setAttribute(IAssetInformationModel attribute);
  
  public String getInstanceId();
  
  public void setInstanceId(String attribute);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public IAssetConfigurationDetailsResponseModel  getAssetConfigurationModel();
  
  public void setAssetConfigurationModel(IAssetConfigurationDetailsResponseModel  assetConfigurationModel);
  
  public String getThumbnailPath();
  public void setThumbnailPath(String thumbnailPath);

  String getMainAssetInstanceSourcePath();

  void setMainAssetInstanceSourcePath(String mainAssetInstanceSourcePath);
}
