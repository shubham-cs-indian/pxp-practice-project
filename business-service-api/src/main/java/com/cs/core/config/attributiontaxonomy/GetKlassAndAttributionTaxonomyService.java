package com.cs.core.config.attributiontaxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.attributiontaxonomy.IGetKlassAndAttributionTaxonomyService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetKlassAndAttributionTaxonomyStrategy;

@Service
public class GetKlassAndAttributionTaxonomyService 
	extends AbstractGetConfigService<IGetTaxonomyRequestModel, IGetMasterTaxonomyWithoutKPModel>
	implements IGetKlassAndAttributionTaxonomyService {
  
  @Autowired
  protected IGetKlassAndAttributionTaxonomyStrategy getKlassAndAttributionTaxonomyStrategy;
  
  protected IGetMasterTaxonomyWithoutKPModel executeGetAttributionTaxonomy(
      IGetTaxonomyRequestModel idParameterModel) throws Exception
  {
    return getKlassAndAttributionTaxonomyStrategy.execute(idParameterModel);
  }
  
  @Override
  public IGetMasterTaxonomyWithoutKPModel executeInternal(IGetTaxonomyRequestModel dataModel)
      throws Exception
  {
    return executeGetAttributionTaxonomy(dataModel);
  }
}
