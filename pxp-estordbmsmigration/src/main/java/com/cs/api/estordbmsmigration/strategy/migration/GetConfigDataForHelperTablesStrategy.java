package com.cs.api.estordbmsmigration.strategy.migration;

import org.springframework.stereotype.Component;

import com.cs.api.estordbmsmigration.model.migration.ConfigDataForHelperTablesModel;
import com.cs.api.estordbmsmigration.model.migration.IConfigDataForHelperTablesModel;
import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSRequestModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetConfigDataForHelperTablesStrategy extends OrientDBBaseStrategy implements IGetConfigDataForHelperTablesStrategy {
  
  @Override
  public IListModel<IConfigDataForHelperTablesModel> execute(ISyncCongifToRDBMSRequestModel model) throws Exception
  {
    return execute("GetConfigDataForHelperTables", model, new TypeReference<ListModel<ConfigDataForHelperTablesModel>>(){});
  }
}
