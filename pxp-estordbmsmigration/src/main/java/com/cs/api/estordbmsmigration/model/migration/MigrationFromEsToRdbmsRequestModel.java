package com.cs.api.estordbmsmigration.model.migration;


public class MigrationFromEsToRdbmsRequestModel implements IMigrationFromEsToRdbmsRequestModel {

  private static final long serialVersionUID = 1L;
  Boolean shouldMigrateArchive = false;
  
  @Override
  public void setShouldMigrateArchive(Boolean shouldMigrateArchive)
  {
    this.shouldMigrateArchive = shouldMigrateArchive;
  }

  @Override
  public Boolean getShouldMigrateArchive()
  {
    return shouldMigrateArchive;
  }
  
}
