package com.cs.core.config.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.strategy.usecase.taxonomy.IGetImmediateChildMajorTaxonomiesByParentIdStrategy;

@Service
public class GetImmediateChildMajorTaxonomiesByParentIdService extends
    AbstractGetConfigService<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel>
    implements IGetImmediateChildMajorTaxonomiesByParentIdService {
  
  @Autowired
  protected IGetImmediateChildMajorTaxonomiesByParentIdStrategy getImmediateChildMajorTaxonomiesByParentIdStrategy;
  
  @Override
  public IGetMajorTaxonomiesResponseModel executeInternal(
      IGetChildMajorTaxonomiesRequestModel model) throws Exception
  {
    return getImmediateChildMajorTaxonomiesByParentIdStrategy.execute(model);
  }
}
