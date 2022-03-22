package com.cs.imprt.config.store.strategy.base.mapping;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICloneMappingsStrategy extends
    IConfigStrategy<IListModel<IConfigCloneEntityInformationModel>, IBulkSaveMappingsResponseModel> {
  
}
