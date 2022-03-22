package com.cs.di.config.mappings;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.imprt.config.store.strategy.base.mapping.ICloneMappingsStrategy;

@Service
public class CloneMappingsService extends
    AbstractCreateConfigService<IListModel<IConfigCloneEntityInformationModel>, IBulkSaveMappingsResponseModel>
    implements ICloneMappingsService {
  
  @Autowired
  protected ICloneMappingsStrategy cloneMappingsStrategy;
  
  @Override
  public IBulkSaveMappingsResponseModel executeInternal(IListModel<IConfigCloneEntityInformationModel> dataModel)
      throws Exception
  {
    return cloneMappingsStrategy.execute(dataModel);
  }
  
}
