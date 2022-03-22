package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.templating.ConfigDetailsForHierarchyRelationshipQuicklistModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForHierarchyRelationshipQuicklistModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForKlassTaxonomyRelationshipQuicklistStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForKlassTaxonomyRelationshipQuicklistStrategy extends
    OrientDBBaseStrategy implements IGetConfigDetailsForKlassTaxonomyRelationshipQuicklistStrategy {
  
  @Override
  public IConfigDetailsForHierarchyRelationshipQuicklistModel execute(IGetTargetKlassesModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_HIERARCHY_RELATIONSHIP_QUICKLIST, model,
        ConfigDetailsForHierarchyRelationshipQuicklistModel.class);
  }
}
