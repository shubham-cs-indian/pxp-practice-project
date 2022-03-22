package com.cs.bds.runtime.usecase.relationshipInheritance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IResolveRelationshipConflictsRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;

public interface IResolveRelationshipInheritanceConflictService  extends IRuntimeService<IResolveRelationshipConflictsRequestModel, IGetKlassInstanceCustomTabModel> {
}
