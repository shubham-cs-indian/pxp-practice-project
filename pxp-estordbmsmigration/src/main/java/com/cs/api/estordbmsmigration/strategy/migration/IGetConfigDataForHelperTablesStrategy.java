package com.cs.api.estordbmsmigration.strategy.migration;

import com.cs.api.estordbmsmigration.model.migration.IConfigDataForHelperTablesModel;
import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSRequestModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IStrategy;

public interface IGetConfigDataForHelperTablesStrategy
    extends IStrategy<ISyncCongifToRDBMSRequestModel, IListModel<IConfigDataForHelperTablesModel>> {
  
}
