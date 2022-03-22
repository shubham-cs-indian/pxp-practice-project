package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IBulkSaveMappings extends
    ISaveConfigInteractor<IListModel<IConfigEntityInformationModel>, IBulkSaveMappingsResponseModel> {
  
}
