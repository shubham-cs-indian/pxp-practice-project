package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.AssetMetadataAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.IAssetMetadataAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryAttributeModel;

public class AssetMetadataAttributeModel extends AbstractMandatoryAttributeModel
    implements IAssetMetadataAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public AssetMetadataAttributeModel()
  {
    super(new AssetMetadataAttribute(), Renderer.TEXT.toString());
  }
  
  public AssetMetadataAttributeModel(IAssetMetadataAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
