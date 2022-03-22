package com.cs.core.runtime.interactor.model.transfer;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetDataTransferModel extends IModel {
  
  public static final String INDEPENDENT_ATTRIBUTE_IDS_TO_TRANSFER    = "independentAttributeIdsToTransfer";
  public static final String DEPENDENT_ATTRIBUTE_IDS_TO_TRANSFER      = "dependentAttributeIdsToTransfer";
  public static final String TAG_IDS_TO_TRANSFER                      = "tagIdsToTransfer";
  
  public static final String INDEPENDENT_ATTRIBUTE_IDS_TO_INHERITANCE = "independentAttributeIdsToInheritaance";
  public static final String DEPENDENT_ATTRIBUTE_IDS_TO_INHERITANCE   = "dependentAttributeIdsToInheritaance";
  public static final String TAG_IDS_TO_INHERITANCE                   = "tagIdsToInheritaance";
  
  public static final String CONTENT_ID                               = "contentId";
  public static final String BASE_TYPE                                = "baseType";
  public static final String MODIFIED_LANGUAGE_CODES                  = "modifiedLanguageCodes";
  
  public List<String> getIndependentAttributeIdsToTransfer();
  
  public void setIndependentAttributeIdsToTransfer(List<String> independentAttributeIdsToTransfer);
  
  public List<String> getDependentAttributeIdsToTransfer();
  
  public void setDependentAttributeIdsToTransfer(List<String> dependentAttributeIds);
  
  public List<String> getTagIdsToTransfer();
  
  public void setTagIdsToTransfer(List<String> tagIds);
  
  public List<String> getIndependentAttributeIdsToInheritaance();
  
  public void setIndependentAttributeIdsToInheritaance(
      List<String> independentAttributeIdsToInheritaance);
  
  public List<String> getDependentAttributeIdsToInheritaance();
  
  public void setDependentAttributeIdsToInheritaance(
      List<String> dependentAttributeIdsToInheritaance);
  
  public List<String> getTagIdsToInheritaance();
  
  public void setTagIdsToInheritaance(List<String> tagIdsToInheritaance);
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseTye);
  
  public List<String> getModifiedLanguageCodes();
  
  public void setModifiedLanguageCodes(List<String> modifiedLanguageCodes);
}
