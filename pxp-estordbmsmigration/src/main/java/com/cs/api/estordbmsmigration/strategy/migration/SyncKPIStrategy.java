package com.cs.api.estordbmsmigration.strategy.migration;

import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSRequestModel;
import com.cs.api.estordbmsmigration.model.migration.ISyncKPIResponseModel;
import com.cs.api.estordbmsmigration.model.migration.SyncKPIResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SyncKPIStrategy extends OrientDBBaseStrategy implements ISyncKPIStrategy {
  
  @Override
  public ISyncKPIResponseModel execute(ISyncCongifToRDBMSRequestModel model) throws Exception
  {
    return execute("GetKeyPerformanceIndexForMigration", model, SyncKPIResponseModel.class);
  }
  
}
