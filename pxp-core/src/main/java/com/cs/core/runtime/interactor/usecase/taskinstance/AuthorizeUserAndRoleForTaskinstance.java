package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.taskinstance.IPermissionsForEntityStatusModel;
import com.cs.core.runtime.interactor.model.taskinstance.ISaveTaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.taskinstance.IAuthorizeUserAndRoleForTaskInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("xyz")
public class AuthorizeUserAndRoleForTaskinstance  extends AbstractRuntimeInteractor<ISaveTaskInstanceModel, IPermissionsForEntityStatusModel>
implements IAuthorizeUserAndRoleForTaskinstance {


  @Autowired
  protected IAuthorizeUserAndRoleForTaskInstanceService authorizeUserAndRoleForTaskInstanceService;

  @Override
  public IPermissionsForEntityStatusModel executeInternal(ISaveTaskInstanceModel dataModel) throws Exception
  {

    return authorizeUserAndRoleForTaskInstanceService.execute(dataModel);
  }



}
