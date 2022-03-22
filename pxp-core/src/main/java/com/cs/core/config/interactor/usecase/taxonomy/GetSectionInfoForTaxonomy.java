package com.cs.core.config.interactor.usecase.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.taxonomy.IGetSectionInfoForTaxonomyService;

@Service
public class GetSectionInfoForTaxonomy
    extends AbstractGetConfigInteractor<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel>
    implements IGetSectionInfoForTaxonomy {
  
  @Autowired
  protected IGetSectionInfoForTaxonomyService getSectionInfoForTaxonomyService;
  
  @Override
  public IGetSectionInfoModel executeInternal(IGetSectionInfoForTypeRequestModel dataModel) throws Exception
  {
    return getSectionInfoForTaxonomyService.execute(dataModel);
  }
  
}
