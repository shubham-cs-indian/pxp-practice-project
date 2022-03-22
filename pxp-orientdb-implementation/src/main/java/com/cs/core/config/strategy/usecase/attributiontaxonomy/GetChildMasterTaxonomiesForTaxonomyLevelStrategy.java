package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetChildMasterTaxonomiesForTaxonomyLevelStrategy extends OrientDBBaseStrategy
    implements IGetChildMasterTaxonomiesForTaxonomyLevelStrategy {
  
  @Override
  public IListModel<String> execute(IIdParameterModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_CHILD_MASTER_TAXONOMIES_FOR_TAXONOMY_LEVEL, model,
        new TypeReference<ListModel<String>>()
        {
          
        });
  }
}
