package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.attributiontaxonomy.IBulkCreateMasterTaxonomyService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IBulkCreateTaxonomyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

@Service
public class BulkCreateMasterTaxonomy
    extends AbstractCreateConfigInteractor<IListModel<ICreateMasterTaxonomyModel>, IBulkCreateTaxonomyResponseModel>
    implements IBulkCreateMasterTaxonomy {
  
  @Autowired
  protected IBulkCreateMasterTaxonomyService createMasterTaxonomyService;
  
  @Override
  public IBulkCreateTaxonomyResponseModel executeInternal(IListModel<ICreateMasterTaxonomyModel> dataModel) throws Exception
  {
    return createMasterTaxonomyService.execute(dataModel);
  }
  
}
