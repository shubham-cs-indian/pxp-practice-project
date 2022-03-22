package com.cs.core.config.interactor.usecase.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.taxonomy.IGetImmediateChildMajorTaxonomiesByParentIdService;
import com.cs.core.runtime.interactor.usecase.taxonomy.IGetImmediateChildMajorTaxonomiesByParentId;

@Service
public class GetImmediateChildMajorTaxonomiesByParentId extends
    AbstractGetConfigInteractor<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel>
    implements IGetImmediateChildMajorTaxonomiesByParentId {
  
  @Autowired
  protected IGetImmediateChildMajorTaxonomiesByParentIdService getImmediateChildMajorTaxonomiesByParentIdService;
  
  @Override
  public IGetMajorTaxonomiesResponseModel executeInternal(
      IGetChildMajorTaxonomiesRequestModel model) throws Exception
  {
    return getImmediateChildMajorTaxonomiesByParentIdService.execute(model);
  }
}
