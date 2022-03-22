package com.cs.core.runtime.interactor.usecase.relationshipinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.bds.runtime.usecase.relationshipInheritance.IResolveRelationshipInheritanceConflictService;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IResolveRelationshipConflictsRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class ResolveRelationshipInheritanceConflict
    extends AbstractRuntimeInteractor<IResolveRelationshipConflictsRequestModel, IGetKlassInstanceCustomTabModel>
    implements IResolveRelationshipInheritanceConflict {

  @Autowired
  protected IResolveRelationshipInheritanceConflictService resolveRelationshipInheritanceConflictService;
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeInternal(IResolveRelationshipConflictsRequestModel model) throws Exception
  {
    return resolveRelationshipInheritanceConflictService.execute(model);
  }
   
}
