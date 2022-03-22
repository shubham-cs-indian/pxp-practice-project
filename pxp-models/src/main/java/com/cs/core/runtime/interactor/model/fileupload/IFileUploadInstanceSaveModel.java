package com.cs.core.runtime.interactor.model.fileupload;

import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;

import java.util.List;

public interface IFileUploadInstanceSaveModel extends IModel {
  
  public static final String KLASS_INSTANCE               = "klassInstance";
  public static final String KLASS_INSTANCE_ID            = "klassInstanceId";
  public static final String ADDED_ATTRIBUTE_INSTANCES    = "addedAttributeInstances";
  public static final String MODIFIED_ATTRIBUTE_INSTANCES = "modifiedAttributeInstances";
  public static final String ADDED_TAGS                   = "addedTags";
  public static final String MODIFIED_TAGS                = "modifiedTags";
  public static final String USER_ID                      = "userId";
  public static final String COMPONENT_ID                 = "componentId";
  public static final String PROCESS_INSTANCE_ID          = "processInstanceId";
  public static final String ASSET_KEYS_MODEL             = "assetKeysModel";
  public static final String FILE_PATH                    = "filePath";
  
  public IContentInstance getKlassInstance();
  
  public void setKlassInstance(IContentInstance klassInstance);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getName();
  
  public void setName(String name);
  
  public List<IContentAttributeInstance> getAddedAttributeInstances();
  
  public void setAddedAttributeInstances(List<IContentAttributeInstance> addedAttributeInstances);
  
  public List<IModifiedContentAttributeInstanceModel> getModifiedAttributeInstances();
  
  public void setModifiedAttributeInstances(
      List<IModifiedContentAttributeInstanceModel> modifiedAttributeInstances);
  
  public List<ITagInstance> getAddedTagInstances();
  
  public void setAddedTagInstances(List<ITagInstance> addedTags);
  
  public List<IModifiedContentTagInstanceModel> getModifiedTagInstances();
  
  public void setModifiedTagInstances(List<IModifiedContentTagInstanceModel> modifiedTags);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public IAssetKeysModel getAssetKeysModel();
  
  public void setAssetKeysModel(IAssetKeysModel processInstanceId);
  
  public String getFilePath();
  
  public void setFilePath(String filePath);
}
