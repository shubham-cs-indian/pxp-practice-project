package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.strategy.usecase.mapping.IBulkGetMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class BulkGetMappingService
    extends AbstractGetConfigService<IIdParameterModel, IListModel<IMappingModel>>
    implements IBulkGetMappingService {

  @Autowired protected IBulkGetMappingStrategy bulkGetMappingStrategy;

  @Override public IListModel<IMappingModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {

    return bulkGetMappingStrategy.execute(dataModel);
  }

}
