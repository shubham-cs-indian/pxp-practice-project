package com.cs.core.runtime.interactor.model.propagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAttributeDetailsForUniquenessEvaluationModel extends IModel {
  
  public static final String ATTRIBUTE_ID          = "attributeId";
  public static final String INSTANCE_ID_TO_ADD    = "instanceIdToAdd";
  public static final String INSTANCE_ID_TO_REMOVE = "instanceIdToRemove";
  public static final String TYPE_IDS              = "typeIds";
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public String getInstanceIdToAdd();
  
  public void setInstanceIdToAdd(String instanceIdToAdd);
  
  public String getInstanceIdToRemove();
  
  public void setInstanceIdToRemove(String instanceIdToRemove);
  
  public List<String> getTypeIds();
  
  public void setTypeIds(List<String> typeIds);
}
