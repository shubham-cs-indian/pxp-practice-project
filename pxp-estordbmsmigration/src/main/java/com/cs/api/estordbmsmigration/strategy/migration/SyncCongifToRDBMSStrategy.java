package com.cs.api.estordbmsmigration.strategy.migration;

import org.springframework.stereotype.Component;

import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSRequestModel;
import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSResponseModel;
import com.cs.api.estordbmsmigration.model.migration.SyncCongifToRDBMSResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class SyncCongifToRDBMSStrategy extends OrientDBBaseStrategy implements ISyncCongifToRDBMSStrategy {
  
  @Override
  public ISyncCongifToRDBMSResponseModel execute(ISyncCongifToRDBMSRequestModel model) throws Exception
  {
    return execute("SyncConfigToRDBMS", model, SyncCongifToRDBMSResponseModel.class);
  }
  
  
  
}
