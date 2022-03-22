package com.cs.core.config.strategy.usecase.migration;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IMigrationModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.strategy.usecase.migration.ISaveMigrationStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigSaveMigrationStrategy extends OrientDBBaseStrategy
    implements ISaveMigrationStrategy {
  
  @Override
  public IIdsListParameterModel execute(IListModel<IMigrationModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model.getList());
    return execute(OrientDBBaseStrategy.SAVE_MIGRATION, requestMap, IdsListParameterModel.class);
  }
}
