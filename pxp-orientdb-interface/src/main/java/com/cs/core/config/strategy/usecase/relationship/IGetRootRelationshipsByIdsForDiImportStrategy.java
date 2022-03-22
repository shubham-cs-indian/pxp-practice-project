package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.dataintegration.IRelationshipsInfoModel;

public interface IGetRootRelationshipsByIdsForDiImportStrategy
    extends IConfigStrategy<IIdsListParameterModel, IRelationshipsInfoModel> {
  
}
