package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;
import com.cs.core.config.role.ISaveRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveRole
    extends AbstractSaveConfigInteractor<IRoleSaveModel, ICreateOrSaveRoleResponseModel>
    implements ISaveRole {
  
  @Autowired
  protected ISaveRoleService saveRoleService;
  
  @Override
  public ICreateOrSaveRoleResponseModel executeInternal(IRoleSaveModel model) throws Exception
  {
    return saveRoleService.execute(model);
  }
}
