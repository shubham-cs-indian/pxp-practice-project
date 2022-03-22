package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

import java.util.List;

public interface IAssetKlassSaveModel extends IKlassSaveModel, IAssetModel {
  
  public static final String ADDED_EXTENSION_CONFIGURATION    = "addedExtensionConfiguration";
  public static final String DELETED_EXTENSION_CONFIGURATION  = "deletedExtensionConfiguration";
  public static final String MODIFIED_EXTENSION_CONFIGURATION = "modifiedExtensionConfiguration";
  
  public List<IAssetExtensionConfigurationModel> getAddedExtensionConfiguration();
  
  public void setAddedExtensionConfiguration(
      List<IAssetExtensionConfigurationModel> addedExtensionConfiguration);
  
  public List<String> getDeletedExtensionConfiguration();
  
  public void setDeletedExtensionConfiguration(List<String> deletedExtensionConfiguration);
  
  public List<IAssetExtensionConfigurationModel> getModifiedExtensionConfiguration();
  
  public void setModifiedExtensionConfiguration(
      List<IAssetExtensionConfigurationModel> modifiedExtensionConfiguration);
}
