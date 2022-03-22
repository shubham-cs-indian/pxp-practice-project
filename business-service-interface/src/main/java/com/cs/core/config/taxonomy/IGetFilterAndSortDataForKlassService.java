package com.cs.core.config.taxonomy;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;

public interface IGetFilterAndSortDataForKlassService
    extends IGetConfigService<IIdsListParameterModel, IGetFilterInformationModel> {
}
