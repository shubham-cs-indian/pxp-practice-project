package com.cs.core.config.interactor.model.variantcontext;

import java.util.List;

import com.cs.core.config.interactor.model.asset.AssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TechnicalImageVariantWithAutoCreateEnableWrapperModel
    implements ITechnicalImageVariantWithAutoCreateEnableWrapperModel {
  
  private static final long                                       serialVersionUID = 1L;
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantWithAutoCreateEnable;
  protected String                                                fileName;
  protected Boolean                                               isSave;
  protected IAssetInformationModel                                attribute;
  protected String                                                instanceId;
  protected String                                                parentId;
  protected IAssetConfigurationDetailsResponseModel               assetConfigurationModel;
  protected String                                                thumbnailPath;
  protected String                                                mainAssetInstanceSourcePath;
  
  
  @Override
  public String getMainAssetInstanceSourcePath()
  {
    return mainAssetInstanceSourcePath;
  }

  @Override
  public void setMainAssetInstanceSourcePath(String mainAssetInstanceSourcePath)
  {
    this.mainAssetInstanceSourcePath = mainAssetInstanceSourcePath;
  }

  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantWithAutoCreateEnable()
  {
    return technicalImageVariantWithAutoCreateEnable;
  }
  
  @Override
  @JsonDeserialize(contentAs = TechnicalImageVariantWithAutoCreateEnableModel.class)
  public void setTechnicalImageVariantWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantWithAutoCreateEnable)
  {
    this.technicalImageVariantWithAutoCreateEnable = technicalImageVariantWithAutoCreateEnable;
  }
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public Boolean getIsSave()
  {
    return isSave;
  }
  
  @Override
  public void setIsSave(Boolean isSave)
  {
    this.isSave = isSave;
  }
  
  @Override
  public IAssetInformationModel getAttribute()
  {
    return attribute;
  }
  
  @Override
  @JsonDeserialize(as = AssetInformationModel.class)
  public void setAttribute(IAssetInformationModel attribute)
  {
    this.attribute = attribute;
  }
  
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.instanceId = instanceId;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public IAssetConfigurationDetailsResponseModel  getAssetConfigurationModel()
  {
    return assetConfigurationModel;
  }

  @Override
  @JsonDeserialize(as = AssetConfigurationDetailsResponseModel.class)
  public void setAssetConfigurationModel(
      IAssetConfigurationDetailsResponseModel  assetConfigurationModel)
  {
    this.assetConfigurationModel = assetConfigurationModel;
  }
  
  @Override
  public String getThumbnailPath()
  {
    return thumbnailPath;
  }

  @Override
  public void setThumbnailPath(String thumbnailPath)
  {
    this.thumbnailPath = thumbnailPath;
  }
}
