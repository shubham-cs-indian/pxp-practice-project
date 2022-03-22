package com.cs.core.config.permission;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionWithHierarchyModel;
import com.cs.core.config.interactor.model.permission.IPermissionWithHierarchyRequestModel;
import com.cs.core.config.strategy.usecase.permission.IGetPermissionWithHierarchyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPermissionWithHierarchyService extends AbstractGetConfigService<IPermissionWithHierarchyRequestModel, IListModel<IGetPermissionWithHierarchyModel>>
    implements IGetPermissionWithHierarchyService {
  
  @Autowired
  protected IGetPermissionWithHierarchyStrategy getPermissionWithHierarchyStrategy;
  
  @Override
  public IListModel<IGetPermissionWithHierarchyModel> executeInternal(
      IPermissionWithHierarchyRequestModel requestModel) throws Exception
  {
    return getPermissionWithHierarchyStrategy.execute(requestModel);
  }
}
