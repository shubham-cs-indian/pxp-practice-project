package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.attributiontaxonomy.IGetKlassAndAttributionTaxonomyService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;

@Service
public class GetKlassAndAttributionTaxonomy 
	extends AbstractGetConfigInteractor<IGetTaxonomyRequestModel, IGetMasterTaxonomyWithoutKPModel>
	implements IGetKlassAndAttributionTaxonomy {
  
  @Autowired
  protected IGetKlassAndAttributionTaxonomyService getKlassAndAttributionTaxonomyService;
  
  @Override
  public IGetMasterTaxonomyWithoutKPModel executeInternal(IGetTaxonomyRequestModel dataModel)
      throws Exception
  {
    return getKlassAndAttributionTaxonomyService.execute(dataModel);
  }
}
