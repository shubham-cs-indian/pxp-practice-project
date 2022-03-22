package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionDetailsModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IGetStaticCollectionDetailsService;

@Service
public class GetStaticCollectionDetails extends AbstractRuntimeInteractor<IIdParameterModel, IGetStaticCollectionDetailsModel>
    implements IGetStaticCollectionDetails {
  
  @Autowired
  protected IGetStaticCollectionDetailsService getStaticCollectionDetailsService;
  
  public IGetStaticCollectionDetailsModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getStaticCollectionDetailsService.execute(idModel);
  }
  
}
