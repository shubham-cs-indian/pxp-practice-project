package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetKlassTaxonomyTreeForRelationshipService;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeForRelationshipRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetKlassTaxonomyTreeForRelationship extends AbstractRuntimeInteractor<IGetKlassTaxonomyTreeForRelationshipRequestModel, 
    IGetKlassTaxonomyTreeResponseModel> implements IGetKlassTaxonomyTreeForRelationship {

  @Autowired
  protected IGetKlassTaxonomyTreeForRelationshipService getKlassTaxonomyTreeForRelationshipService; 
  
  @Override
  protected IGetKlassTaxonomyTreeResponseModel executeInternal(IGetKlassTaxonomyTreeForRelationshipRequestModel model) throws Exception
  {
    return getKlassTaxonomyTreeForRelationshipService.execute(model);
  }
  
 
}
