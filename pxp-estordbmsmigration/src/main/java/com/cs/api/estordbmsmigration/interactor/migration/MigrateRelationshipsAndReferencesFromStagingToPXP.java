package com.cs.api.estordbmsmigration.interactor.migration;

import org.springframework.stereotype.Service;

@Service
public class MigrateRelationshipsAndReferencesFromStagingToPXP extends AbstractMigrationRunProcedure
    implements IMigrateFromStagingToPXP {
  
  @Override
  protected String getProcedureName()
  {
    return "staging.sp_migrateallrelationshipsandreferencesfromestordbms";
  }
  
}
