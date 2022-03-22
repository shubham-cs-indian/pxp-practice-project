package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;

import java.util.List;
import java.util.Map;

public interface ICreateAssetInstanceModel extends ICreateInstanceModel {
  
  public static final String METADATA   = "metadata";
  public static final String ATTRIBUTES = "attributes";
  public static final String ASSET_INFORMATION = "assetInformation";
  
  public Map<String, Object> getMetadata();
  
  public void setMetadata(Map<String, Object> metadata);
  
  public List<IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<IContentAttributeInstance> attributeInstances);
  
  public IAssetInformationModel getAssetInformation();
  
  public void setAssetInformation(IAssetInformationModel assetInformation);
}
