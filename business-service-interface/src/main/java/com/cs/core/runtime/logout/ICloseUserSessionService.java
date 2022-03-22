package com.cs.core.runtime.logout;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;

public interface ICloseUserSessionService extends IRuntimeService<IUserSessionModel, IVoidModel> {
}
