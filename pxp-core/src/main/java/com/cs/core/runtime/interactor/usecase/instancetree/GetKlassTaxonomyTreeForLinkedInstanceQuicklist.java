package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetKlassTaxonomyTreeForLinkedInstanceQuicklistService;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeForLIQRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetKlassTaxonomyTreeForLinkedInstanceQuicklist extends
    AbstractRuntimeInteractor<IGetKlassTaxonomyTreeForLIQRequestModel, IGetKlassTaxonomyTreeResponseModel>
    implements IGetKlassTaxonomyTreeForLinkedInstanceQuicklist {
  
  @Autowired
  protected IGetKlassTaxonomyTreeForLinkedInstanceQuicklistService getKlassTaxonomyTreeForLinkedInstanceQuicklistService;

  @Override
  protected IGetKlassTaxonomyTreeResponseModel executeInternal(IGetKlassTaxonomyTreeForLIQRequestModel model) throws Exception
  {
    return getKlassTaxonomyTreeForLinkedInstanceQuicklistService.execute(model);
  }
  
 
}
