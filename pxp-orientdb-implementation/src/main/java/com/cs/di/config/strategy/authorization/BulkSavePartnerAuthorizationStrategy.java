package com.cs.di.config.strategy.authorization;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.di.config.model.authorization.BulkSavePartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IBulkSavePartnerAuthorizationModel;

@Component
public class BulkSavePartnerAuthorizationStrategy extends OrientDBBaseStrategy
    implements IBulkSavePartnerAuthorizationStrategy {
  
  @Override
  public IBulkSavePartnerAuthorizationModel execute(IListModel<IIdLabelCodeModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IListModel.LIST, model);
    return execute(BULK_SAVE_PARTNER_AUTHORIZATION_MAPPING, requestMap, BulkSavePartnerAuthorizationModel.class);
  }
}
