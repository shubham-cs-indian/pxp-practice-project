package com.cs.core.runtime.taskinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.taskinstance.IPermissionsForEntityStatusModel;
import com.cs.core.runtime.interactor.model.taskinstance.ISaveTaskInstanceModel;

public interface IAuthorizeUserAndRoleForTaskInstanceService extends IRuntimeService<ISaveTaskInstanceModel, IPermissionsForEntityStatusModel> {
  
}
