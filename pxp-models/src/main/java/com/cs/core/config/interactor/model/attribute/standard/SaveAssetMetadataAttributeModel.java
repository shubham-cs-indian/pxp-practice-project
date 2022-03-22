package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.standard.attribute.AssetMetadataAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.IAssetMetadataAttribute;

public class SaveAssetMetadataAttributeModel extends AbstractSaveMandatoryAttributeModel
    implements IAssetMetadataAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveAssetMetadataAttributeModel()
  {
    super(new AssetMetadataAttribute(), Renderer.TEXT.toString());
  }
  
  public SaveAssetMetadataAttributeModel(IAssetMetadataAttribute attribute)
  {
    super(attribute, Renderer.TEXT.toString());
  }
}
