package com.cs.core.config.permission;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionWithHierarchyModel;
import com.cs.core.config.interactor.model.permission.IPermissionWithHierarchyRequestModel;

public interface IGetPermissionWithHierarchyService extends
    IGetConfigService<IPermissionWithHierarchyRequestModel, IListModel<IGetPermissionWithHierarchyModel>> {
  
}
