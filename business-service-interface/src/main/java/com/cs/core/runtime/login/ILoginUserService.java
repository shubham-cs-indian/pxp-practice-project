package com.cs.core.runtime.login;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;

public interface ILoginUserService extends IRuntimeService<IUserSessionModel, IVoidModel> {
}
