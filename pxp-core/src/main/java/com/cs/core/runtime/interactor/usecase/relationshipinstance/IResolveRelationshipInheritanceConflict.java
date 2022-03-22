package com.cs.core.runtime.interactor.usecase.relationshipinstance;

import com.cs.core.runtime.interactor.entity.relationshipinstance.IResolveRelationshipConflictsRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IResolveRelationshipInheritanceConflict  extends IRuntimeInteractor<IResolveRelationshipConflictsRequestModel, IGetKlassInstanceCustomTabModel> {
}
