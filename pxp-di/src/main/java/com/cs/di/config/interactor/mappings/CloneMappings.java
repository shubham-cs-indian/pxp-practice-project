package com.cs.di.config.interactor.mappings;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.di.config.mappings.ICloneMappingsService;

@Service
public class CloneMappings extends
    AbstractCreateConfigInteractor<IListModel<IConfigCloneEntityInformationModel>, IBulkSaveMappingsResponseModel>
    implements ICloneMappings {
  
  @Autowired
  protected ICloneMappingsService cloneMappingsService;
  
  @Override
  public IBulkSaveMappingsResponseModel executeInternal(IListModel<IConfigCloneEntityInformationModel> dataModel)
      throws Exception
  {
    return cloneMappingsService.execute(dataModel);
  }
  
}
