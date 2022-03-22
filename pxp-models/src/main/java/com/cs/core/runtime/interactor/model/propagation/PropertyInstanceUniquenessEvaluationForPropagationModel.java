package com.cs.core.runtime.interactor.model.propagation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class PropertyInstanceUniquenessEvaluationForPropagationModel
    implements IPropertyInstanceUniquenessEvaluationForPropagationModel {
  
  private static final long                                            serialVersionUID = 1L;
  protected String                                                     instanceId;
  protected String                                                     baseType;
  protected Map<String, IAttributeDetailsForUniquenessEvaluationModel> attributes;
  
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.instanceId = instanceId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Map<String, IAttributeDetailsForUniquenessEvaluationModel> getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeDetailsForUniquenessEvaluationModel.class)
  public void setAttributes(Map<String, IAttributeDetailsForUniquenessEvaluationModel> attributes)
  {
    this.attributes = attributes;
  }
}
