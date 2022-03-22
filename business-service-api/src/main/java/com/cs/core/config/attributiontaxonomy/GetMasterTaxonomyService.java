package com.cs.core.config.attributiontaxonomy;

import com.cs.core.config.attributiontaxonomy.IGetMasterTaxonomyService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetMasterTaxonomyStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetMasterTaxonomyService
    extends AbstractGetTagTaxonomy<IGetTaxonomyRequestModel, IGetMasterTaxonomyWithoutKPModel>
    implements IGetMasterTaxonomyService {
  
  @Autowired
  protected IGetMasterTaxonomyStrategy getMasterTaxonomyStrategy;
  
  @Override
  public IGetMasterTaxonomyWithoutKPModel executeInternal(IGetTaxonomyRequestModel dataModel)
      throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  protected IGetMasterTaxonomyWithoutKPModel executeGetAttributionTaxonomy(
      IGetTaxonomyRequestModel dataModel) throws Exception
  {
    return getMasterTaxonomyStrategy.execute(dataModel);
  }
}
