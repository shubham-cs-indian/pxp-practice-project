package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigTaskInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllTaskService  extends IGetConfigService<IIdParameterModel, IListModel<IConfigTaskInformationModel>>{

}
