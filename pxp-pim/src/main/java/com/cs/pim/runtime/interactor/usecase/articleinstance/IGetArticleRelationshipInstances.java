package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassRelationshipInstancesModel;
import com.cs.core.runtime.interactor.model.relationship.IGetAllRelationshipInstancesModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetArticleRelationshipInstances extends
    IRuntimeInteractor<IKlassRelationshipInstancesModel, IGetAllRelationshipInstancesModel> {
  
}
