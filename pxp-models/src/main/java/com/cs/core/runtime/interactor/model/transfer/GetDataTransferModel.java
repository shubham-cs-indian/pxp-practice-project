package com.cs.core.runtime.interactor.model.transfer;

import java.util.ArrayList;
import java.util.List;

public class GetDataTransferModel implements IGetDataTransferModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    independentAttributeIdsToTransfer;
  protected List<String>    dependentAttributeIdsToTransfer;
  protected List<String>    tagIdsToTransfer;
  
  protected List<String>    independentAttributeIdsToInheritaance;
  protected List<String>    dependentAttributeIdsToInheritaance;
  protected List<String>    tagIdsToInheritaance;
  
  protected String          contentId;
  protected String          baseType;
  protected List<String>    modifiedLanguageCodes;
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseTye)
  {
    this.baseType = baseTye;
  }
  
  @Override
  public List<String> getModifiedLanguageCodes()
  {
    if (modifiedLanguageCodes == null) {
      modifiedLanguageCodes = new ArrayList<>();
    }
    return modifiedLanguageCodes;
  }
  
  @Override
  public void setModifiedLanguageCodes(List<String> modifiedLanguageCodes)
  {
    this.modifiedLanguageCodes = modifiedLanguageCodes;
  }
  
  @Override
  public List<String> getIndependentAttributeIdsToInheritaance()
  {
    if (independentAttributeIdsToInheritaance == null) {
      independentAttributeIdsToInheritaance = new ArrayList<>();
    }
    return independentAttributeIdsToInheritaance;
  }
  
  @Override
  public void setIndependentAttributeIdsToInheritaance(
      List<String> independentAttributeIdsToInheritaance)
  {
    this.independentAttributeIdsToInheritaance = independentAttributeIdsToInheritaance;
  }
  
  @Override
  public List<String> getDependentAttributeIdsToInheritaance()
  {
    if (dependentAttributeIdsToInheritaance == null) {
      dependentAttributeIdsToInheritaance = new ArrayList<>();
    }
    return dependentAttributeIdsToInheritaance;
  }
  
  @Override
  public void setDependentAttributeIdsToInheritaance(
      List<String> dependentAttributeIdsToInheritaance)
  {
    this.dependentAttributeIdsToInheritaance = dependentAttributeIdsToInheritaance;
  }
  
  @Override
  public List<String> getTagIdsToInheritaance()
  {
    if (tagIdsToInheritaance == null) {
      tagIdsToInheritaance = new ArrayList<>();
    }
    return tagIdsToInheritaance;
  }
  
  @Override
  public void setTagIdsToInheritaance(List<String> tagIdsToInheritaance)
  {
    this.tagIdsToInheritaance = tagIdsToInheritaance;
  }
  
  @Override
  public List<String> getIndependentAttributeIdsToTransfer()
  {
    if (independentAttributeIdsToTransfer == null) {
      independentAttributeIdsToTransfer = new ArrayList<>();
    }
    return independentAttributeIdsToTransfer;
  }
  
  @Override
  public void setIndependentAttributeIdsToTransfer(List<String> independentAttributeIdsToTransfer)
  {
    this.independentAttributeIdsToTransfer = independentAttributeIdsToTransfer;
  }
  
  @Override
  public List<String> getDependentAttributeIdsToTransfer()
  {
    if (dependentAttributeIdsToTransfer == null) {
      dependentAttributeIdsToTransfer = new ArrayList<>();
    }
    return dependentAttributeIdsToTransfer;
  }
  
  @Override
  public void setDependentAttributeIdsToTransfer(List<String> dependentAttributeIdsToTransfer)
  {
    this.dependentAttributeIdsToTransfer = dependentAttributeIdsToTransfer;
  }
  
  @Override
  public List<String> getTagIdsToTransfer()
  {
    if (tagIdsToTransfer == null) {
      tagIdsToTransfer = new ArrayList<>();
    }
    return tagIdsToTransfer;
  }
  
  @Override
  public void setTagIdsToTransfer(List<String> tagIdsToTransfer)
  {
    this.tagIdsToTransfer = tagIdsToTransfer;
  }
}
