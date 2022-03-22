package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IBulkSaveMappingsService extends
    ISaveConfigService<IListModel<IConfigEntityInformationModel>, IBulkSaveMappingsResponseModel> {
  
}
