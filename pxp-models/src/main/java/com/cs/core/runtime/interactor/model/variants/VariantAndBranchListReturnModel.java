package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.model.variantcontext.IVariantAndBranchListReturnModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceRelationshipTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipTreeStrategyModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class VariantAndBranchListReturnModel implements IVariantAndBranchListReturnModel {
  
  private static final long                                                serialVersionUID = 1L;
  
  protected Map<String, Object>                                            variantAndBranchListMap;
  protected List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships;
  protected IExceptionModel                                                failure;
  
  @Override
  public Map<String, Object> getVariantAndBranchListMap()
  {
    return variantAndBranchListMap;
  }
  
  @Override
  public void setVariantAndBranchListMap(Map<String, Object> variantAndBranchListMap)
  {
    this.variantAndBranchListMap = variantAndBranchListMap;
  }
  
  @Override
  public List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> getContentRelationships()
  {
    return contentRelationships;
  }
  
  @JsonDeserialize(contentAs = GetKlassInstanceRelationshipTreeStrategyModel.class)
  @Override
  public void setContentRelationships(
      List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
}
