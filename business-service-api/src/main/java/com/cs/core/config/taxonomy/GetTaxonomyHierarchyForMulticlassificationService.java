package com.cs.core.config.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import org.springframework.stereotype.Service;

@Service
public class GetTaxonomyHierarchyForMulticlassificationService extends AbstractGetTaxonomyHierarchyForMulticlassificationService<IIdPaginationModel, IConfigEntityTreeInformationModel>
    implements IGetTaxonomyHierarchyForMulticlassificationService {
  
  @Override
  public IConfigEntityTreeInformationModel executeInternal(IIdPaginationModel dataModel)
      throws Exception
  {
    return super.executeInternal(dataModel);
  }
}
