package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAssetAttributeInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;

import java.util.List;

public interface IContentInstanceSaveModel
    extends IKlassInstanceSaveModel, IContentInstance, IRuntimeModel {
  
  public static final String ADDED_ASSETS        = "addedAssets";
  public static final String MODIFIED_ASSETS     = "modifiedAssets";
  public static final String DELETED_ASSETS      = "deletedAssets";
  public static final String PROCESS_INSTANCE_ID = "processInstanceId";
  
  public List<? extends IAssetAttributeInstance> getAddedAssets();
  
  public void setAddedAssets(List<? extends IAssetAttributeInstance> addedAssets);
  
  public List<? extends IModifiedAssetAttributeInstanceModel> getModifiedAssets();
  
  public void setModifiedAssets(
      List<? extends IModifiedAssetAttributeInstanceModel> modifiedAssets);
  
  public List<String> getDeletedAssets();
  
  public void setDeletedAssets(List<String> deletedAssets);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
}
