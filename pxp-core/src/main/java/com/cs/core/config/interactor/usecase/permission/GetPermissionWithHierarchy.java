package com.cs.core.config.interactor.usecase.permission;

import com.cs.core.config.permission.IGetPermissionWithHierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionWithHierarchyModel;
import com.cs.core.config.interactor.model.permission.IPermissionWithHierarchyRequestModel;
import com.cs.core.config.strategy.usecase.permission.IGetPermissionWithHierarchyStrategy;

@Service
public class GetPermissionWithHierarchy extends
    AbstractGetConfigInteractor<IPermissionWithHierarchyRequestModel, IListModel<IGetPermissionWithHierarchyModel>>
    implements IGetPermissionWithHierarchy {
  
  @Autowired
  protected IGetPermissionWithHierarchyService getPermissionWithHierarchyService;
  
  @Override
  public IListModel<IGetPermissionWithHierarchyModel> executeInternal(
      IPermissionWithHierarchyRequestModel requestModel) throws Exception
  {
    return getPermissionWithHierarchyService.execute(requestModel);
  }
}
