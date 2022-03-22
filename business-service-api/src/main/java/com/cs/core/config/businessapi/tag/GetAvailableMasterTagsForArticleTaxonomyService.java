package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel;
import com.cs.core.config.strategy.usecase.tag.IGetAvailableMasterTagsForArticleTaxonomyStrategy;

@Service
public class GetAvailableMasterTagsForArticleTaxonomyService
    extends AbstractGetConfigService<IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel, IListModel<IGetEntityModel>>
    implements IGetAvailableMasterTagsForArticleTaxonomyService {
  
  @Autowired
  protected IGetAvailableMasterTagsForArticleTaxonomyStrategy getAvailableMasterTagsForArticleTaxonomyStrategy;
  
  @Override
  public IListModel<IGetEntityModel> executeInternal(IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel dataModel) throws Exception
  {
    return getAvailableMasterTagsForArticleTaxonomyStrategy.execute(dataModel);
  }
}
