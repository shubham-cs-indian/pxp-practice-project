package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.di.config.mappings.IBulkSaveMappingsService;

@Service
public class BulkSaveMappings extends
    AbstractSaveConfigInteractor<IListModel<IConfigEntityInformationModel>, IBulkSaveMappingsResponseModel>
    implements IBulkSaveMappings {
  
  @Autowired
  protected IBulkSaveMappingsService bulkSaveMappingsService;
  
  @Override
  public IBulkSaveMappingsResponseModel executeInternal(IListModel<IConfigEntityInformationModel> dataModel)
      throws Exception
  {
    return bulkSaveMappingsService.execute(dataModel);
  }

}
