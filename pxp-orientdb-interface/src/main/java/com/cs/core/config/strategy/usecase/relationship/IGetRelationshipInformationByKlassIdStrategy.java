package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInformationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetRelationshipInformationByKlassIdStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<IRelationshipInformationModel>> {
  
}
