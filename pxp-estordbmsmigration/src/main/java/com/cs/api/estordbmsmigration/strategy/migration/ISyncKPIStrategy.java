package com.cs.api.estordbmsmigration.strategy.migration;

import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSRequestModel;
import com.cs.api.estordbmsmigration.model.migration.ISyncKPIResponseModel;
import com.cs.core.config.strategy.configuration.base.IStrategy;

public interface ISyncKPIStrategy extends IStrategy<ISyncCongifToRDBMSRequestModel, ISyncKPIResponseModel> {
  
}
