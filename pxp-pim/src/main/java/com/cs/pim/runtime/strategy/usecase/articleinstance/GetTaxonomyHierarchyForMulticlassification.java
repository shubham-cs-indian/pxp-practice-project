package com.cs.pim.runtime.strategy.usecase.articleinstance;


import com.cs.core.config.taxonomy.IGetTaxonomyHierarchyForMulticlassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractGetTaxonomyHierarchyForMulticlassification;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetTaxonomyHierarchyForMulticlassification;

@Service
public class GetTaxonomyHierarchyForMulticlassification extends
    AbstractGetTaxonomyHierarchyForMulticlassification<IIdPaginationModel, IConfigEntityTreeInformationModel>
    implements IGetTaxonomyHierarchyForMulticlassification {

  @Autowired
  protected IGetTaxonomyHierarchyForMulticlassificationService getTaxonomyHierarchyForMulticlassificationService;

  @Override
  public IConfigEntityTreeInformationModel executeInternal(IIdPaginationModel dataModel) throws Exception
  {
    return getTaxonomyHierarchyForMulticlassificationService.execute(dataModel);
  }
}
