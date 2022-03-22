package com.cs.core.config.attributiontaxonomy;

import com.cs.core.config.attributiontaxonomy.IGetAllowedTagValuesForMasterTaxonomyService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetAllowedTagValuesForMasterTaxonomyStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllowedTagValuesForMasterTaxonomyService
    extends AbstractGetAllowedTagValuesForTagTaxonomy<IIdParameterModel, IListModel<ITagModel>>
    implements IGetAllowedTagValuesForMasterTaxonomyService {
  
  @Autowired
  protected IGetAllowedTagValuesForMasterTaxonomyStrategy getAllowedTagValuesForMasterTaxonomyStrategy;
  
  @Override
  public IListModel<ITagModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IListModel<ITagModel> executeGetAllowedTagValuesForMasterTaxonomy(
      IIdParameterModel model) throws Exception
  {
    return getAllowedTagValuesForMasterTaxonomyStrategy.execute(model);
  }
}
