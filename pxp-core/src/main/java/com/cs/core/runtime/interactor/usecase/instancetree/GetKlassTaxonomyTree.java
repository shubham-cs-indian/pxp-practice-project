package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetKlassTaxonomyTreeService;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetKlassTaxonomyTree extends
    AbstractRuntimeInteractor<IGetKlassTaxonomyTreeRequestModel, IGetKlassTaxonomyTreeResponseModel>
    implements IGetKlassTaxonomyTree {


  
  @Autowired
  protected IGetKlassTaxonomyTreeService getKlassTaxonomyTreeService;

  @Override
  protected IGetKlassTaxonomyTreeResponseModel executeInternal(IGetKlassTaxonomyTreeRequestModel model) throws Exception
  {
    return getKlassTaxonomyTreeService.execute(model);
  }
 
}