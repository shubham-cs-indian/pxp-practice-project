package com.cs.core.runtime.interactor.usecase.userSessionTermination;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;

public interface IUserSessionTerminationService extends IRuntimeService<IUserSessionModel, IVoidModel> {
}
