package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;

public interface ICloneMappings extends
    ICreateConfigInteractor<IListModel<IConfigCloneEntityInformationModel>, IBulkSaveMappingsResponseModel> {
  
}
