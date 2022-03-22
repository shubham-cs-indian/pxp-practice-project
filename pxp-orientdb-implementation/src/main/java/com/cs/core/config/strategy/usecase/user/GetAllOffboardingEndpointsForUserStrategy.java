package com.cs.core.config.strategy.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IGetOffboardingEndpointsByUserRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetAllOffboardingEndpointsForUserStrategy extends OrientDBBaseStrategy
    implements IGetAllOffboardingEndpointsForUserStrategy {
  
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Override
  public IListModel<IConfigEntityInformationModel> execute(IGetOffboardingEndpointsByUserRequestModel model)
      throws Exception
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    model.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    return execute(GET_ALL_OFFBOARDING_ENDPOINT_FOR_USER, model,
        new TypeReference<ListModel<ConfigEntityInformationModel>>()
        {
          
        });
  }
}
