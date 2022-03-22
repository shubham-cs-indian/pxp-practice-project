package com.cs.api.estordbmsmigration.interactor.migration;

import com.cs.api.estordbmsmigration.model.migration.IMigrationFromEsToRdbmsRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IMigrationFromEsToRdbms {

  public IVoidModel execute(IMigrationFromEsToRdbmsRequestModel dataModel) throws Exception;
}
