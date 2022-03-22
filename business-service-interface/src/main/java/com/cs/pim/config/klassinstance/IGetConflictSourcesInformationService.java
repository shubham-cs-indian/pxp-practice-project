package com.cs.pim.config.klassinstance;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.runtime.interactor.model.klassinstance.IConflictSourcesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetConflictSourcesInformationModel;

public interface IGetConflictSourcesInformationService
    extends IGetConfigService<IConflictSourcesRequestModel, IGetConflictSourcesInformationModel> {
}
