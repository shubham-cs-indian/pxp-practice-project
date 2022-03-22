package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetKlassTaxonomyTreeForCollectionService;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetKlassTaxonomyTreeForCollection
    extends AbstractRuntimeInteractor<IGetKlassTaxonomyTreeRequestModel, IGetKlassTaxonomyTreeResponseModel>
    implements IGetKlassTaxonomyTreeForCollection {
  
  @Autowired
  protected IGetKlassTaxonomyTreeForCollectionService getKlassTaxonomyTreeForCollectionService;
  
  @Override
  protected IGetKlassTaxonomyTreeResponseModel executeInternal(IGetKlassTaxonomyTreeRequestModel model) throws Exception
  {
    return getKlassTaxonomyTreeForCollectionService.execute(model);
  }
  
}
