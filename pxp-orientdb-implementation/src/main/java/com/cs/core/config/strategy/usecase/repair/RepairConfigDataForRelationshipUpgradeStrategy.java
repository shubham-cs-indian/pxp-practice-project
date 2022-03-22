package com.cs.core.config.strategy.usecase.repair;

import com.cs.core.config.interactor.model.relationship.IRelationshipInfoModel;
import com.cs.core.config.interactor.model.relationship.RelationshipInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RepairConfigDataForRelationshipUpgradeStrategy extends OrientDBBaseStrategy
    implements IRepairConfigDataForRelationshipUpgradeStrategy {
  
  @Override
  public IRelationshipInfoModel execute(IModel model) throws Exception
  {
    return execute("Orient_Migration_Script_Relationship_Upgrade", new HashMap<>(),
        RelationshipInfoModel.class);
  }
}
