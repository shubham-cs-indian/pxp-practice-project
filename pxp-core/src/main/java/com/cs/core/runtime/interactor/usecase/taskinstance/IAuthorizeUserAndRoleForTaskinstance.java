package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.IPermissionsForEntityStatusModel;
import com.cs.core.runtime.interactor.model.taskinstance.ISaveTaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IAuthorizeUserAndRoleForTaskinstance extends
IRuntimeInteractor<ISaveTaskInstanceModel, IPermissionsForEntityStatusModel>{
  
}
