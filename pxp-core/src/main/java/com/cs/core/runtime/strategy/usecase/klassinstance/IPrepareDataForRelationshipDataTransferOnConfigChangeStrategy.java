package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IRelationshipDataTransferOnConfigChangeInputModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IPrepareDataForRelationshipDataTransferOnConfigChangeStrategy extends
    IRuntimeStrategy<IRelationshipDataTransferOnConfigChangeInputModel, IListModel<IContentsPropertyDiffModel>> {
  
}
