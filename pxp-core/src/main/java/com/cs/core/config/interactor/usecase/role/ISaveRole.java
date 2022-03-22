package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;

public interface ISaveRole extends ISaveConfigInteractor<IRoleSaveModel, ICreateOrSaveRoleResponseModel> {
  
}
