package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.runtime.interactor.model.component.klassinstance.IDiAttributeVariantModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.IDiEmbeddedVariantsModel;

public class JmsImportVariantModel extends JmsImportTaskModel implements IJmsImportVariantModel {
  
  private static final long          serialVersionUID = 1L;
  protected IDiEmbeddedVariantsModel embeddedVariantModel;
  protected IDiAttributeVariantModel attributeVariantModel;
  
  @Override
  public IDiEmbeddedVariantsModel getEmbeddedVariantModel()
  {
    return embeddedVariantModel;
  }
  
  @Override
  public void setEmbeddedVariantModel(IDiEmbeddedVariantsModel embeddedVariantModel)
  {
    this.embeddedVariantModel = embeddedVariantModel;
  }
  
  @Override
  public IDiAttributeVariantModel getAttributeVariantModel()
  {
    return attributeVariantModel;
  }
  
  @Override
  public void setAttributeVariantModel(IDiAttributeVariantModel attributeVariantModel)
  {
    this.attributeVariantModel = attributeVariantModel;
  }
}
