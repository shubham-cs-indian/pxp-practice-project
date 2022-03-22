package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.config.role.IGetAllRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllRoles extends AbstractGetConfigInteractor<IRoleModel, IListModel<IRoleModel>>
    implements IGetAllRoles {

  @Autowired
  protected IGetAllRolesService getAllRolesService;

  @Override
  public IListModel<IRoleModel> executeInternal(IRoleModel dataModel) throws Exception
  {
    return getAllRolesService.execute(dataModel);
  }
}
