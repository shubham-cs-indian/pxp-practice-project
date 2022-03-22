package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.config.taxonomy.IGetTaxonomyHierarchyForSelectedTaxonomyService;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.getTaxonomyHierarchyForSelectedTaxonomy.IGetTaxonomyHierarchyForSelectedTaxonomy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTaxonomyHierarchyForSelectedTaxonomy extends
    AbstractRuntimeInteractor<IConfigDetailsForSwitchTypeRequestModel, IGetConfigDetailsModel>
    implements IGetTaxonomyHierarchyForSelectedTaxonomy {
  
  @Autowired
  protected IGetTaxonomyHierarchyForSelectedTaxonomyService getTaxonomyHierarchyForSelectedTaxonomyService;
  
  @Override
  public IGetConfigDetailsModel executeInternal(IConfigDetailsForSwitchTypeRequestModel requestModel)
      throws Exception
  {
    return getTaxonomyHierarchyForSelectedTaxonomyService.execute(requestModel);
  }
}
