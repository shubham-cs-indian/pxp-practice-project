package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.attributiontaxonomy.IGetAllowedTagValuesForMasterTaxonomyService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllowedTagValuesForMasterTaxonomy extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<ITagModel>>
    implements IGetAllowedTagValuesForMasterTaxonomy {
  
  @Autowired
  protected IGetAllowedTagValuesForMasterTaxonomyService getAllowedTagValuesForMasterTaxonomyService;
  
  @Override
  public IListModel<ITagModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllowedTagValuesForMasterTaxonomyService.execute(dataModel);
  }
  
}
