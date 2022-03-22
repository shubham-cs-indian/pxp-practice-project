package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IBulkSaveMappingsStrategy extends
    IConfigStrategy<IListModel<IConfigEntityInformationModel>, IBulkSaveMappingsResponseModel> {
  
}
