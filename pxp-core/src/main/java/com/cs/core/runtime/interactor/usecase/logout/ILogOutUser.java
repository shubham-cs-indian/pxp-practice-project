package com.cs.core.runtime.interactor.usecase.logout;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ILogOutUser extends IRuntimeInteractor<IUserSessionModel, IVoidModel> {
}
