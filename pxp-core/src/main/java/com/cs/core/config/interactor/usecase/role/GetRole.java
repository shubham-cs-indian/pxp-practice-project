package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.role.IGetRoleModel;
import com.cs.core.config.role.IGetRoleService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetRole extends AbstractGetConfigInteractor<IIdParameterModel, IGetRoleModel>
    implements IGetRole {

  @Autowired
  protected IGetRoleService getRoleService;

  @Override
  public IGetRoleModel executeInternal(IIdParameterModel roleModel) throws Exception
  {
    return getRoleService.execute(roleModel);
  }
}
