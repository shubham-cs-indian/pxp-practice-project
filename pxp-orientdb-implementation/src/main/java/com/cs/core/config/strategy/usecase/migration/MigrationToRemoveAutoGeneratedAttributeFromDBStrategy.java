package com.cs.core.config.strategy.usecase.migration;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;

@Component
public class MigrationToRemoveAutoGeneratedAttributeFromDBStrategy extends OrientDBBaseStrategy implements
IMigrationToRemoveAutoGeneratedAttributeFromDBStrategy  {

  @Override
  public IIdsListParameterModel execute(IVoidModel model) throws Exception
  {
    return execute(ORIENT_MIGRATION_FOR_REMOVING_AUTO_GENERATED_ATTRIBUTES,  model, IdsListParameterModel.class);
  }

}
