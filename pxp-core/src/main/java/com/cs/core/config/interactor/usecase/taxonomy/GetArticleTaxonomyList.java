package com.cs.core.config.interactor.usecase.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.taxonomy.IGetArticleTaxonomyListModel;
import com.cs.core.config.taxonomy.IGetArticleTaxonomyListService;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.IGetArticleTaxonomyList;

@Service
public class GetArticleTaxonomyList
    extends AbstractGetConfigInteractor<IModel, IGetArticleTaxonomyListModel>
    implements IGetArticleTaxonomyList {
  
  @Autowired
  IGetArticleTaxonomyListService getArticleTaxonomyListService;
  
  @Override
  public IGetArticleTaxonomyListModel executeInternal(IModel model) throws Exception
  {
    return getArticleTaxonomyListService.execute(model);
  }
  
}
