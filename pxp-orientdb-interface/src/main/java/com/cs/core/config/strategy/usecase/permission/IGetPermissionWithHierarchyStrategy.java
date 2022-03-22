package com.cs.core.config.strategy.usecase.permission;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionWithHierarchyModel;
import com.cs.core.config.interactor.model.permission.IPermissionWithHierarchyRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetPermissionWithHierarchyStrategy extends
    IConfigStrategy<IPermissionWithHierarchyRequestModel, IListModel<IGetPermissionWithHierarchyModel>> {
  
}
