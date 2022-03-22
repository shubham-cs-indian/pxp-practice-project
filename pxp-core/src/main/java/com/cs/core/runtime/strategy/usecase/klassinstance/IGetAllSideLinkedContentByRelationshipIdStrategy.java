package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentTransferHelperModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipIdSideIdModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetAllSideLinkedContentByRelationshipIdStrategy
    extends IRuntimeStrategy<IRelationshipIdSideIdModel, IListModel<IContentTransferHelperModel>> {
  
}
