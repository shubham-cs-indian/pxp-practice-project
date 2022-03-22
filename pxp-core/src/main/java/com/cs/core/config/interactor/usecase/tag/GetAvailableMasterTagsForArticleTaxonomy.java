package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.tag.IGetAvailableMasterTagsForArticleTaxonomyService;
import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel;

@Service
public class GetAvailableMasterTagsForArticleTaxonomy extends
    AbstractGetConfigInteractor<IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel, IListModel<IGetEntityModel>>
    implements IGetAvailableMasterTagsForArticleTaxonomy {
  
  @Autowired
  protected IGetAvailableMasterTagsForArticleTaxonomyService getAvailableMasterTagsForArticleTaxonomyService;
  
  @Override
  public IListModel<IGetEntityModel> executeInternal(
      IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel dataModel) throws Exception
  {
    return getAvailableMasterTagsForArticleTaxonomyService.execute(dataModel);
  }
}
