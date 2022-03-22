package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.klass.IGetAllChildMajorTaxonomiesByParentIdService;

@Service
public class GetAllChildMajorTaxonomiesByParentId extends
    AbstractGetConfigInteractor<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel>
    implements IGetAllChildMajorTaxonomiesByParentId {

  
  @Autowired
  protected IGetAllChildMajorTaxonomiesByParentIdService   getAllChildMajorTaxonomiesByParentIdService;
  
  @Override
  public IGetMajorTaxonomiesResponseModel executeInternal(IGetChildMajorTaxonomiesRequestModel model) throws Exception
  {
    return getAllChildMajorTaxonomiesByParentIdService.execute(model);
  }

}