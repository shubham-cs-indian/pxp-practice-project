package com.cs.core.initialize;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.ApplicationStatusForMigration;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IMigrationModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.configuration.MigrationModel;
import com.cs.core.runtime.strategy.usecase.migration.IMigrationExecutedStrategy;
import com.cs.core.runtime.strategy.usecase.migration.IMigrationStrategy;
import com.cs.core.runtime.strategy.usecase.migration.ISaveMigrationStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeConfigMigrationService implements IInitializeConfigMigrationService {
  
  @Autowired
  protected IMigrationExecutedStrategy    configCheckMigrationExecutedStrategy;
  
  @Autowired
  protected IMigrationStrategy            configMigrationStrategy;
  
  @Autowired
  protected ISaveMigrationStrategy        configSaveMigrationStrategy;
  
  @Autowired
  protected ApplicationStatusForMigration applicationStatus;
  
  @Override
  public void execute() throws Exception
  {
    InputStream stream = InitializeConfigMigrationService.class.getClassLoader()
        .getResourceAsStream("configmigration.json");
    List<IMigrationModel> migrations = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<MigrationModel>>()
        {
        });
    
    if (applicationStatus.isCleanInstallation()) {
      // If its a clean installation, then add all entries of migration from
      // JSON directly to database. This will make sure these migrations are not
      // run unnecessarily
      IListModel<IMigrationModel> model = new ListModel<>();
      model.setList(migrations);
      configSaveMigrationStrategy.execute(model);
    }
    else {
      // Run each migration from JSON (That was never run before & mark those
      // migrations as done in database)
      List<String> ids = new ArrayList<>();
      for (IMigrationModel migration : migrations) {
        String id = migration.getId();
        ids.add(id);
      }
      IIdsListParameterModel model = new IdsListParameterModel();
      model.setIds(ids);
      
      IIdsListParameterModel response = configCheckMigrationExecutedStrategy.execute(model);
      List<String> migrationToBeSaved = response.getIds();
      for (IMigrationModel migration : migrations) {
        String migrationId = migration.getId();
        if (migrationToBeSaved.contains(migrationId)) {
          configMigrationStrategy.execute(migration);
        }
      }
    }
  }
}
