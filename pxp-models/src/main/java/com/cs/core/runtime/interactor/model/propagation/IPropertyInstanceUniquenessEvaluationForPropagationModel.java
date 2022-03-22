package com.cs.core.runtime.interactor.model.propagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IPropertyInstanceUniquenessEvaluationForPropagationModel extends IModel {
  
  public static final String INSTANCE_ID = "instanceId";
  public static final String BASETYPE    = "baseType";
  public static final String ATTRIBUTES  = "attributes";
  
  public String getInstanceId();
  
  public void setInstanceId(String instanceId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  // key is attributeId and value is attributeDetailModelForUniquenessEvaluation
  public Map<String, IAttributeDetailsForUniquenessEvaluationModel> getAttributes();
  
  public void setAttributes(Map<String, IAttributeDetailsForUniquenessEvaluationModel> attributes);
}
