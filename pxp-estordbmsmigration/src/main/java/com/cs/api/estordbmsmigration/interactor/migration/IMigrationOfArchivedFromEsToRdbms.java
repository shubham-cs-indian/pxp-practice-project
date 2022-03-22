package com.cs.api.estordbmsmigration.interactor.migration;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IMigrationOfArchivedFromEsToRdbms {

  public IVoidModel execute(IVoidModel dataModel) throws Exception;
}
