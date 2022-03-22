package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.klass.IGetAllChildMajorTaxonomiesByParentIdService;
import com.cs.core.config.strategy.usecase.klass.IGetAllChildMajorTaxonomiesByParentIdStrategy;

@Service
public class GetAllChildMajorTaxonomiesByParentIdService extends
    AbstractGetConfigService<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel>
    implements IGetAllChildMajorTaxonomiesByParentIdService {

  
  @Autowired
  protected IGetAllChildMajorTaxonomiesByParentIdStrategy   getAllChildMajorTaxonomiesByParentIdStrategy;
  
  @Override
  public IGetMajorTaxonomiesResponseModel executeInternal(IGetChildMajorTaxonomiesRequestModel model) throws Exception
  {
    return getAllChildMajorTaxonomiesByParentIdStrategy.execute(model);
  }

}