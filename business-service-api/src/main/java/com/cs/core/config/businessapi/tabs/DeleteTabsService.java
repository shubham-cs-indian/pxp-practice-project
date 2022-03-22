package com.cs.core.config.businessapi.tabs;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.tabs.IDeleteTabsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteTabsService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> implements IDeleteTabsService {
  
  @Autowired
  protected IDeleteTabsStrategy deleteTabStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getTabEntityConfigurationStrategy;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    IGetEntityConfigurationResponseModel getEntityResponse = getTabEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(model.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();

    if (!referenceData.keySet()
        .isEmpty()) {
        throw new EntityConfigurationDependencyException();
    }

    return deleteTabStrategy.execute(model);
  }
}
