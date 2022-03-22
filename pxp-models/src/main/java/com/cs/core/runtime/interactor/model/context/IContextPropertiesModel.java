package com.cs.core.runtime.interactor.model.context;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;

import java.util.List;

public interface IContextPropertiesModel extends IModel {
  
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
