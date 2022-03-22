package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAssetInstanceModel extends CreateInstanceModel
    implements ICreateAssetInstanceModel {
  
  private static final long                 serialVersionUID = 1L;
  
  protected List<IContentAttributeInstance> attributes;
  protected Map<String, Object>             metadata;
  protected IAssetInformationModel         assetInformation;
  
  
  @Override
  public List<IContentAttributeInstance> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  public void setAttributes(List<IContentAttributeInstance> attributeInstances)
  {
    this.attributes = attributeInstances;
  }
  
  @Override
  public Map<String, Object> getMetadata()
  {
    return metadata;
  }
  
  @JsonDeserialize(as = HashMap.class)
  @Override
  public void setMetadata(Map<String, Object> metadata)
  {
    this.metadata = metadata;
  }

  @Override
  public IAssetInformationModel getAssetInformation()
  {
    return assetInformation;
  }
  
  @Override
  @JsonDeserialize(as = AssetInformationModel.class)
  public void setAssetInformation(IAssetInformationModel assetInformation)
  {
    this.assetInformation = assetInformation;
    
  }
}
