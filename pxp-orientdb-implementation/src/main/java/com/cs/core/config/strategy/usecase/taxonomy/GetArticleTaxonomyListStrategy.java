package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.taxonomy.GetArticleTaxonomyListModel;
import com.cs.core.config.interactor.model.taxonomy.IGetArticleTaxonomyListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassTaxonomyListStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.stereotype.Component;

@Component("getArticleTaxonomyListStrategy")
public class GetArticleTaxonomyListStrategy extends OrientDBBaseStrategy
    implements IGetKlassTaxonomyListStrategy {
  
  @Override
  public IGetArticleTaxonomyListModel execute(IModel model) throws Exception
  {
    return execute(GET_ARTICLE_TAXONOMY_LIST, model, GetArticleTaxonomyListModel.class);
  }
}
