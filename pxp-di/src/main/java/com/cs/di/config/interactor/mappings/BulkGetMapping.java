package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.mappings.IBulkGetMappingService;

@Service
public class BulkGetMapping
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IMappingModel>>
    implements IBulkGetMapping {

  @Autowired protected IBulkGetMappingService bulkGetMappingService;

  @Override public IListModel<IMappingModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {

    return bulkGetMappingService.execute(dataModel);
  }

}
