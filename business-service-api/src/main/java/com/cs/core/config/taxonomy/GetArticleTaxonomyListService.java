package com.cs.core.config.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.taxonomy.IGetArticleTaxonomyListModel;
import com.cs.core.config.strategy.usecase.klass.IGetKlassTaxonomyListStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetArticleTaxonomyListService
    extends AbstractGetKlassTaxonomyListService<IModel, IGetArticleTaxonomyListModel>
    implements IGetArticleTaxonomyListService {
  
  @Autowired
  IGetKlassTaxonomyListStrategy getArticleTaxonomyListStrategy;
  
  @Override
  public IGetArticleTaxonomyListModel executeInternal(IModel model) throws Exception
  {
    return super.executeInternal(null);
  }
  
  @Override
  protected IGetArticleTaxonomyListModel executeGetArticleTaxonomyList(IModel model)
      throws Exception
  {
    
    return getArticleTaxonomyListStrategy.execute(null);
  }
}
