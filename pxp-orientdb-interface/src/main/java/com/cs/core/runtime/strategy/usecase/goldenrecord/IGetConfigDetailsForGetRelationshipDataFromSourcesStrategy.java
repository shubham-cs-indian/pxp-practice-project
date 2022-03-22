package com.cs.core.runtime.strategy.usecase.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IConfigDetailsForGetRelationshipDataModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetConfigDetailsForGetRelationshipDataFromSourcesStrategy
    extends IRuntimeStrategy<IIdsListParameterModel, IConfigDetailsForGetRelationshipDataModel> {
  
}
