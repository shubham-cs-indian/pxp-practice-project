package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractGetAllowedTagValuesForTagTaxonomy<P extends IModel, R extends IModel>
    extends AbstractGetConfigInteractor<P, R> {
  
  protected abstract IListModel<ITagModel> executeGetAllowedTagValuesForMasterTaxonomy(
      IIdParameterModel model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return (R) executeGetAllowedTagValuesForMasterTaxonomy((IIdParameterModel) model);
  }
}
