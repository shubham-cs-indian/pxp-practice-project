package com.cs.core.runtime.interactor.usecase.login;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ILoginUser extends IRuntimeInteractor<IUserSessionModel, IVoidModel> {
}
