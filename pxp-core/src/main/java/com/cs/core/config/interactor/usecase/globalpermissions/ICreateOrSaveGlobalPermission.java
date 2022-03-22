package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionModel;

public interface ICreateOrSaveGlobalPermission extends
    IConfigInteractor<ISaveGlobalPermissionModel, ICreateOrSaveGlobalPermissionResponseModel> {
  
}
