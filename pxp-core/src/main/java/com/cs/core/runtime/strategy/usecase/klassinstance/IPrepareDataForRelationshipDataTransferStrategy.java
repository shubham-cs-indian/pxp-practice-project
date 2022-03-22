package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.relationship.IGetRelationshipDataTransferModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IPrepareDataForRelationshipDataTransferStrategy extends
    IRuntimeStrategy<IGetRelationshipDataTransferModel, IListModel<IContentsPropertyDiffModel>> {
  
}
