package com.cs.core.runtime.strategy.model.couplingtype;

import java.util.List;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPropertiesIdCodeCouplingTypeModel extends IModel {
  
  public static final String INDEPENDENT_ATTRIBUTES = "independentAttributes";
  public static final String TAGS                   = "tags";
  public static final String DEPENDENT_ATTRIBUTES   = "dependentAttributes";
  
  public List<IIdCodeCouplingTypeModel> getIndependentAttributes();
  
  public void setIndependentAttributes(List<IIdCodeCouplingTypeModel> independentAttributes);
  
  public List<IIdCodeCouplingTypeModel> getTags();
  
  public void setTags(List<IIdCodeCouplingTypeModel> tags);
  
  public List<IIdCodeCouplingTypeModel> getDependentAttributes();
  
  public void setDependentAttributes(List<IIdCodeCouplingTypeModel> dependentAttributes);
}
