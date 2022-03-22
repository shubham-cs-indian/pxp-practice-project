package com.cs.core.config.interactor.usecase.permission;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionWithHierarchyModel;
import com.cs.core.config.interactor.model.permission.IPermissionWithHierarchyRequestModel;

public interface IGetPermissionWithHierarchy extends
    IGetConfigInteractor<IPermissionWithHierarchyRequestModel, IListModel<IGetPermissionWithHierarchyModel>> {
  
}
