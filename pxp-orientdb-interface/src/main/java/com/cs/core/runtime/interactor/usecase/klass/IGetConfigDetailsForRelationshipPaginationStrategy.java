package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.klass.IConfigDetailsForRelationshipPaginationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestForRelationshipsModel;

public interface IGetConfigDetailsForRelationshipPaginationStrategy extends
    IConfigStrategy<IMulticlassificationRequestForRelationshipsModel, IConfigDetailsForRelationshipPaginationModel> {
}
