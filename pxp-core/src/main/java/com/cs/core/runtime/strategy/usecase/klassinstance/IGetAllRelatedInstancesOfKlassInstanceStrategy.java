package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.languageinstance.IBulkPropogationForDeletedTranslationsRequestModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetAllRelatedInstancesOfKlassInstanceStrategy extends
    IRuntimeStrategy<IBulkPropogationForDeletedTranslationsRequestModel, IListModel<IIdAndBaseType>> {
  
}
