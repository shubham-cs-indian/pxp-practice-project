package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetTaxonomyHierarchyIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import org.springframework.stereotype.Component;

@Component("getTaxonomyHierarchyIdsStrategy")
public class GetTaxonomyHierarchyIdsStrategy extends OrientDBBaseStrategy
    implements IGetTaxonomyHierarchyIdsStrategy {
  
  @Override
  public IIIDsListModel execute(IIdLabelCodeModel model)
      throws Exception
  {
    return execute(GET_HIERARCHY_TAXONOMY_IDS, model, IIDsListModel.class);
  }
}
