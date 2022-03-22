package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IPropagableContextKlassInformationModel extends IKlassInformationModel {
  
  public static String PROPAGABLE_ATTRIBUTES = "propagableAttributes";
  public static String PROPAGABLE_TAGS       = "propagableTags";
  
  public Map<String, IIdAndCouplingTypeModel> getPropagableAttributes();
  
  public void setPropagableAttributes(Map<String, IIdAndCouplingTypeModel> propagableAttributes);
  
  public Map<String, IIdAndCouplingTypeModel> getPropagableTags();
  
  public void setPropagableTags(Map<String, IIdAndCouplingTypeModel> propagableTags);
}
