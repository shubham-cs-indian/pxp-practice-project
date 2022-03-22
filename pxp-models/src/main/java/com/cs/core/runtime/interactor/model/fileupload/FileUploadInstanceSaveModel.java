package com.cs.core.runtime.interactor.model.fileupload;

import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;

import java.util.List;

public class FileUploadInstanceSaveModel implements IFileUploadInstanceSaveModel {
  
  private static final long                              serialVersionUID = 1L;
  
  protected IContentInstance                             klassInstance;
  protected String                                       klassInstanceId;
  protected String                                       name;
  protected List<IContentAttributeInstance>              addedAttributeInstances;
  protected List<IModifiedContentAttributeInstanceModel> modifiedAttributeInstances;
  protected List<ITagInstance>                           addedTagInstances;
  protected List<IModifiedContentTagInstanceModel>       modifiedTagInstances;
  protected String                                       userId;
  protected String                                       componentId;
  protected String                                       processInstanceId;
  protected IAssetKeysModel                              assetKeysModel;
  protected String                                       filePath;
  
  public IContentInstance getKlassInstance()
  {
    return klassInstance;
  }
  
  public void setKlassInstance(IContentInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public List<IContentAttributeInstance> getAddedAttributeInstances()
  {
    return addedAttributeInstances;
  }
  
  public void setAddedAttributeInstances(List<IContentAttributeInstance> addedAttributeInstances)
  {
    this.addedAttributeInstances = addedAttributeInstances;
  }
  
  public List<IModifiedContentAttributeInstanceModel> getModifiedAttributeInstances()
  {
    return modifiedAttributeInstances;
  }
  
  public void setModifiedAttributeInstances(
      List<IModifiedContentAttributeInstanceModel> modifiedAttributeInstances)
  {
    this.modifiedAttributeInstances = modifiedAttributeInstances;
  }
  
  public List<ITagInstance> getAddedTagInstances()
  {
    return addedTagInstances;
  }
  
  public void setAddedTagInstances(List<ITagInstance> addedTagInstances)
  {
    this.addedTagInstances = addedTagInstances;
  }
  
  public List<IModifiedContentTagInstanceModel> getModifiedTagInstances()
  {
    return modifiedTagInstances;
  }
  
  public void setModifiedTagInstances(List<IModifiedContentTagInstanceModel> modifiedTagInstances)
  {
    this.modifiedTagInstances = modifiedTagInstances;
  }
  
  public String getUserId()
  {
    return userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public String getComponentId()
  {
    return componentId;
  }
  
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
  
  public String getProcessInstanceId()
  {
    return processInstanceId;
  }
  
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
  
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
  public String getFilePath()
  {
    return filePath;
  }
  
  @Override
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }
}
