package com.cs.core.config.interactor.usecase.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateRoleCloneModel;
import com.cs.core.config.role.ICreateRoleCloneService;

@Service
public class CreateRoleClone
    extends AbstractCreateConfigInteractor<ICreateRoleCloneModel, ICreateOrSaveRoleResponseModel>
    implements ICreateRoleClone {
  
  @Autowired
  ICreateRoleCloneService createRoleCloneService;
  
  @Override
  protected ICreateOrSaveRoleResponseModel executeInternal(ICreateRoleCloneModel model)
      throws Exception
  {
    return createRoleCloneService.execute(model);
  }
  
}
