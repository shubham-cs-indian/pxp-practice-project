package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateRoleModel;
import com.cs.core.config.role.ICreateRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateRole
    extends AbstractCreateConfigInteractor<ICreateRoleModel, ICreateOrSaveRoleResponseModel>
    implements ICreateRole {
  
  @Autowired
  protected ICreateRoleService createRoleService;
  
  @Override
  public ICreateOrSaveRoleResponseModel executeInternal(ICreateRoleModel roleModel) throws Exception
  {
    return createRoleService.execute(roleModel);
  }
}
