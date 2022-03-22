package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;

public interface ICloneMappingsService extends
    ICreateConfigService<IListModel<IConfigCloneEntityInformationModel>, IBulkSaveMappingsResponseModel> {
  
}
