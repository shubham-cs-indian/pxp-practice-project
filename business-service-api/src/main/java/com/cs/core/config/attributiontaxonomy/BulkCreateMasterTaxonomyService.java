package com.cs.core.config.attributiontaxonomy;

import com.cs.core.config.attributiontaxonomy.IBulkCreateMasterTaxonomyService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IBulkCreateTaxonomyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IBulkCreateMasterTaxonomyStrategy;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkCreateMasterTaxonomyService extends
    AbstractBulkCreateTagTaxonomy<IListModel<ICreateMasterTaxonomyModel>, IBulkCreateTaxonomyResponseModel>
    implements IBulkCreateMasterTaxonomyService {
  
  @Autowired
  protected IBulkCreateMasterTaxonomyStrategy createMasterTaxonomyStrategy;


  @Override
  public IBulkCreateTaxonomyResponseModel executeInternal(
      IListModel<ICreateMasterTaxonomyModel> dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IBulkCreateTaxonomyResponseModel executeBulkCreateAttributionTaxonomy(
      IListModel<ICreateMasterTaxonomyModel> model) throws Exception
  {
    return createMasterTaxonomyStrategy.execute(model);
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.CREATEBULKATTRIBUTIONTAXONOMY;
  }
}
