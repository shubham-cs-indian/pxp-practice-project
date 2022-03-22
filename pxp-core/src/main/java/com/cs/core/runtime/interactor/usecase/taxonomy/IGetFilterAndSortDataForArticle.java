package com.cs.core.runtime.interactor.usecase.taxonomy;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;

public interface IGetFilterAndSortDataForArticle
    extends IGetConfigInteractor<IIdsListParameterModel, IGetFilterInformationModel> {
}
