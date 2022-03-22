package com.cs.core.config.businessapi.tag;

import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.tag.IGetAvailableMasterTagsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAvailableMasterTagsService
    extends AbstractGetAvailableTaxonomyTags<IIdParameterModel, IListModel<IGetEntityModel>>
    implements IGetAvailableMasterTagsService {
  
  @Autowired
  protected IGetAvailableMasterTagsStrategy addOrDeleteMasterTaxonomyLevelStrategy;
  
  @Override
  public IListModel<IGetEntityModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IListModel<IGetEntityModel> executeGetAvailableTaxonomyTabs(IIdParameterModel model)
      throws Exception
  {
    return addOrDeleteMasterTaxonomyLevelStrategy.execute(model);
  }
}
