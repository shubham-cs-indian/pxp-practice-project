package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.runtime.interactor.model.component.klassinstance.IDiAttributeVariantModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.IDiEmbeddedVariantsModel;

public interface IJmsImportVariantModel extends IJmsImportTaskModel {
  
  public static final String EMBEDDED_VARIANT_MODEL  = "embeddedVariantModel";
  public static final String ATTRIBUTE_VARIANT_MODEL = "attributeVariantModel";
  
  public IDiEmbeddedVariantsModel getEmbeddedVariantModel();
  
  public void setEmbeddedVariantModel(IDiEmbeddedVariantsModel embeddedVariantModel);
  
  public IDiAttributeVariantModel getAttributeVariantModel();
  
  public void setAttributeVariantModel(IDiAttributeVariantModel attributeVariantModel);
}
