package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;

public interface IGetConfigDetailsForRelationshipRestoreStrategy extends
    IConfigStrategy<IGetConfigDetailsForSaveRelationshipInstancesRequestModel, IGetConfigDetailsForSaveRelationshipInstancesResponseModel> {
}
