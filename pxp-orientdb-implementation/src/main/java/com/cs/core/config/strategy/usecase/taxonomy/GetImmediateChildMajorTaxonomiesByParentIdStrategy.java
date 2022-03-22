package com.cs.core.config.strategy.usecase.taxonomy;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.klass.GetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetImmediateChildMajorTaxonomiesByParentIdStrategy extends OrientDBBaseStrategy
    implements IGetImmediateChildMajorTaxonomiesByParentIdStrategy {
  
  public static final String useCase = "GetImmediateChildMajorTaxonomiesByParentId";
  
  @Override
  public IGetMajorTaxonomiesResponseModel execute(
      IGetChildMajorTaxonomiesRequestModel model) throws Exception
  {
    return execute(useCase, model, GetMajorTaxonomiesResponseModel.class);
  }
}
