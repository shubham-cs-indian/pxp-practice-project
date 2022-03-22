package com.cs.core.runtime.interactor.model.contextdatatransfer;


import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.model.couplingtype.IPropertiesIdCodeCouplingTypeModel;

public interface IGetConfigDetailsForContextualDataTransferModel extends IModel {
  
  public static final String CONTEXTUAL_DATA_TRANSFER                 = "contextualDataTransfer";
  public static final String CONTEXTUAL_DATA_INHERITANCE              = "contextualDataInheritance";
  public static final String DEPENDENT_ATTRIBUTE_IDS_TO_TRANSFER      = "dependentAttributeIdsToTransfer";
  public static final String INDEPENDENT_ATTRIBUTE_IDS_TO_TRANSFER    = "independentAttributeIdsToTransfer";
  public static final String TAG_IDS_TO_TRANSFER                      = "tagIdsToTransfer";
  public static final String DEPENDENT_ATTRIBUTE_IDS_TO_INHERITANCE   = "dependentAttributeIdsToInheritance";
  public static final String INDEPENDENT_ATTRIBUTE_IDS_TO_INHERITANCE = "independentAttributeIdsToInheritance";
  public static final String TAG_IDS_TO_INHERITANCE                   = "tagIdsToInheritance";
  public static final String CONTEXT_KLASS_ID                         = "contextKlassId";
  
  // key: embeddedKlassId
  public Map<String, IPropertiesIdCodeCouplingTypeModel> getContextualDataTransfer();
  
  public void setContextualDataTransfer(
      Map<String, IPropertiesIdCodeCouplingTypeModel> contextualDataTransfer);
  
  public IPropertiesIdCodeCouplingTypeModel getContextualDataInheritance();
  
  public void setContextualDataInheritance(
      IPropertiesIdCodeCouplingTypeModel contextualDataInheritance);
  
  public List<String> getDependentAttributeIdsToTransfer();
  
  public void setDependentAttributeIdsToTransfer(List<String> dependentAttributeIdsToTransfer);
  
  public List<String> getIndependentAttributeIdsToTransfer();
  
  public void setIndependentAttributeIdsToTransfer(List<String> independentAttributeIdsToTransfer);
  
  public List<String> getTagIdsToTransfer();
  
  public void setTagIdsToTransfer(List<String> tagIdsToTransfer);
  
  public List<String> getDependentAttributeIdsToInheritance();
  
  public void setDependentAttributeIdsToInheritance(
      List<String> dependentAttributeIdsToInheritance);
  
  public List<String> getIndependentAttributeIdsToInheritance();
  
  public void setIndependentAttributeIdsToInheritance(
      List<String> independentAttributeIdsToInheritance);
  
  public List<String> getTagIdsToInheritance();
  
  public void setTagIdsToInheritance(List<String> tagIdsToInheritance);
  
  public String getContextKlassId();
  
  public void setContextKlassId(String contextKlassId);
}
