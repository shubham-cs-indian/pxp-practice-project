package com.cs.core.runtime.strategy.offboarding;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassNatureRelationshipModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IOrientDBGetAllSideKlassIdForNatureRelationshipStrategy
    extends IConfigStrategy<IModel, IListModel<IKlassNatureRelationshipModel>> {
  
}
