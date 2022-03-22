package com.cs.di.config.mappings;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.mapping.IDeleteMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteMappingService
    extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteMappingService {

  @Autowired protected IDeleteMappingStrategy deleteMappingStrategy;

  @Autowired
  protected IGetEntityConfigurationStrategy      getMappingEntityConfigurationStrategy;

  @Override public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {

    IGetEntityConfigurationResponseModel getEntityResponse = getMappingEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(dataModel.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();

    if (!referenceData.keySet()
        .isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }

    return deleteMappingStrategy.execute(dataModel);
  }

}
