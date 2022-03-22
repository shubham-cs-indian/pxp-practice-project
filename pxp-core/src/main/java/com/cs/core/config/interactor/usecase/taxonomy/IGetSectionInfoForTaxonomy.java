package com.cs.core.config.interactor.usecase.taxonomy;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;

public interface IGetSectionInfoForTaxonomy
    extends IGetConfigInteractor<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel> {
  
}
