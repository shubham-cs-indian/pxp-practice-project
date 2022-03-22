package com.cs.core.config.taxonomy;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetChildTaxonomiesByParentIdWithPaginationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetTaxonomyHierarchyForMulticlassificationService<P extends IIdPaginationModel, R extends IConfigEntityTreeInformationModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetChildTaxonomiesByParentIdWithPaginationStrategy getTaxonomyHierarchyStrategy;
  
  @SuppressWarnings("unchecked")
  @Override
  public R executeInternal(P dataModel) throws Exception
  {
    IConfigEntityTreeInformationModel taxonomyHierarchy = getTaxonomyHierarchyStrategy.execute(dataModel);
    return (R) taxonomyHierarchy;
  }
}
