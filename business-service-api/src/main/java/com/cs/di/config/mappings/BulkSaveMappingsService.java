package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.strategy.usecase.mapping.IBulkSaveMappingsStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

@Service
public class BulkSaveMappingsService extends
    AbstractSaveConfigService<IListModel<IConfigEntityInformationModel>, IBulkSaveMappingsResponseModel>
    implements IBulkSaveMappingsService {
  
  @Autowired
  protected IBulkSaveMappingsStrategy bulkSaveMappingsStrategy;
  
  @Override
  public IBulkSaveMappingsResponseModel executeInternal(IListModel<IConfigEntityInformationModel> dataModel)
      throws Exception
  {
    return bulkSaveMappingsStrategy.execute(dataModel);
  }

}
