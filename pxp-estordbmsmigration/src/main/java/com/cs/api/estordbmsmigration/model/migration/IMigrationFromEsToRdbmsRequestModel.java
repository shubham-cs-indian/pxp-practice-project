package com.cs.api.estordbmsmigration.model.migration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IMigrationFromEsToRdbmsRequestModel extends IModel {
  
  public void setShouldMigrateArchive(Boolean shouldMigrateArchive);
  
  public Boolean getShouldMigrateArchive();
}

