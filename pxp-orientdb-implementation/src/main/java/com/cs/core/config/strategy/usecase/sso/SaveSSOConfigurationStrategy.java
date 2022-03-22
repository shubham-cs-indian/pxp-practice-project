package com.cs.core.config.strategy.usecase.sso;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.BulkSaveSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.IBulkSaveSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveSSOConfigurationStrategy extends OrientDBBaseStrategy
    implements ISaveSSOConfigurationStrategy {
  
  @Override
  public IBulkSaveSSOConfigurationResponseModel execute(
      IListModel<ICreateSSOConfigurationModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(SAVE_SSO_CONFIGURATION, requestMap, BulkSaveSSOConfigurationResponseModel.class);
  }
}
