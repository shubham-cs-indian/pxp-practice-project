package com.cs.core.config.taxonomy;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetTaxonomyHierarchyForSelectedTaxonomyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTaxonomyHierarchyForSelectedTaxonomyService extends AbstractRuntimeService<IConfigDetailsForSwitchTypeRequestModel, IGetConfigDetailsModel>
    implements IGetTaxonomyHierarchyForSelectedTaxonomyService {
  
  @Autowired
  protected IGetTaxonomyHierarchyForSelectedTaxonomyStrategy getTaxonomyHierarchyForSelectedTaxonomyStrategy;
  
  @Override
  public IGetConfigDetailsModel executeInternal(IConfigDetailsForSwitchTypeRequestModel requestModel)
      throws Exception
  {
    requestModel.setUserId(context.getUserId());
    return getTaxonomyHierarchyForSelectedTaxonomyStrategy.execute(requestModel);
  }
}
