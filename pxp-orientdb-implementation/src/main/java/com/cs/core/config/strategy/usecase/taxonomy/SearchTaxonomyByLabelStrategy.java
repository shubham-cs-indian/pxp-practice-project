package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.taxonomy.ITaxonomySearchStrategyModel;

import org.springframework.stereotype.Component;

@Component
public class SearchTaxonomyByLabelStrategy extends OrientDBBaseStrategy
    implements ISearchTaxonomyByLabelStrategy {
  
  public static final String useCase = "SearchTaxonomyByLabel";
  
  @SuppressWarnings("unchecked")
  @Override
  public IListModel<ICategoryInformationModel> execute(ITaxonomySearchStrategyModel model)
      throws Exception
  {
    return execute(useCase, model, ListModel.class);
  }
}
