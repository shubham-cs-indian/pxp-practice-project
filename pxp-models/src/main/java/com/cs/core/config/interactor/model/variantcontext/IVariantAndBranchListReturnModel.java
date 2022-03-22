package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipTreeStrategyModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

import java.util.List;
import java.util.Map;

public interface IVariantAndBranchListReturnModel extends IModel {
  
  public Map<String, Object> getVariantAndBranchListMap();
  
  public void setVariantAndBranchListMap(Map<String, Object> variantAndBranchListMap);
  
  public List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> getContentRelationships();
  
  public void setContentRelationships(
      List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships);
  
  public IExceptionModel getFailure();
  
  public void setFailure(IExceptionModel failure);
}
