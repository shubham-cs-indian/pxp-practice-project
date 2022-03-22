package com.cs.core.runtime.interactor.model.contextdatatransfer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.strategy.model.couplingtype.IPropertiesIdCodeCouplingTypeModel;
import com.cs.core.runtime.strategy.model.couplingtype.PropertiesIdCouplingTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDetailsForContextualDataTransferModel
    implements IGetConfigDetailsForContextualDataTransferModel {
  
  private static final long                                 serialVersionUID = 1L;
  protected Map<String, IPropertiesIdCodeCouplingTypeModel> contextualDataTransfer;
  protected IPropertiesIdCodeCouplingTypeModel              contextualDataInheritance;
  protected List<String>                                    dependentAttributeIdsToTransfer;
  protected List<String>                                    independentAttributeIdsToTransfer;
  protected List<String>                                    tagIdsToTransfer;
  protected List<String>                                    dependentAttributeIdsToInheritance;
  protected List<String>                                    independentAttributeIdsToInheritance;
  protected List<String>                                    tagIdsToInheritance;
  protected String                                          contextKlassId;
  
  @Override
  public Map<String, IPropertiesIdCodeCouplingTypeModel> getContextualDataTransfer()
  {
    if (contextualDataTransfer == null) {
      contextualDataTransfer = new HashMap<>();
    }
    return contextualDataTransfer;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertiesIdCouplingTypeModel.class)
  public void setContextualDataTransfer(
      Map<String, IPropertiesIdCodeCouplingTypeModel> contextualDataTransfer)
  {
    this.contextualDataTransfer = contextualDataTransfer;
  }
  
  @Override
  public IPropertiesIdCodeCouplingTypeModel getContextualDataInheritance()
  {
    if (contextualDataInheritance == null) {
      contextualDataInheritance = new PropertiesIdCouplingTypeModel();
    }
    return contextualDataInheritance;
  }
  
  @Override
  @JsonDeserialize(as = PropertiesIdCouplingTypeModel.class)
  public void setContextualDataInheritance(
      IPropertiesIdCodeCouplingTypeModel contextualDataInheritance)
  {
    this.contextualDataInheritance = contextualDataInheritance;
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
  
  @Override
  public List<String> getDependentAttributeIdsToInheritance()
  {
    if (dependentAttributeIdsToInheritance == null) {
      dependentAttributeIdsToInheritance = new ArrayList<>();
    }
    return dependentAttributeIdsToInheritance;
  }
  
  @Override
  public void setDependentAttributeIdsToInheritance(List<String> dependentAttributeIdsToInheritance)
  {
    this.dependentAttributeIdsToInheritance = dependentAttributeIdsToInheritance;
  }
  
  @Override
  public List<String> getIndependentAttributeIdsToInheritance()
  {
    if (independentAttributeIdsToInheritance == null) {
      independentAttributeIdsToInheritance = new ArrayList<>();
    }
    return independentAttributeIdsToInheritance;
  }
  
  @Override
  public void setIndependentAttributeIdsToInheritance(
      List<String> independentAttributeIdsToInheritance)
  {
    this.independentAttributeIdsToInheritance = independentAttributeIdsToInheritance;
  }
  
  @Override
  public List<String> getTagIdsToInheritance()
  {
    if (tagIdsToInheritance == null) {
      tagIdsToInheritance = new ArrayList<>();
    }
    return tagIdsToInheritance;
  }
  
  @Override
  public void setTagIdsToInheritance(List<String> tagIdsToInheritance)
  {
    this.tagIdsToInheritance = tagIdsToInheritance;
  }
  
  @Override
  public String getContextKlassId()
  {
    return contextKlassId;
  }
  
  @Override
  public void setContextKlassId(String contextKlassId)
  {
    this.contextKlassId = contextKlassId;
  }
}

