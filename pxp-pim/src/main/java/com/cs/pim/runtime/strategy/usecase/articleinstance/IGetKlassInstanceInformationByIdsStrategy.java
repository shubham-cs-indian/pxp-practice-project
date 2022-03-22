package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllKlassInstancesVariantsModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetKlassInstanceInformationByIdsStrategy extends
    IRuntimeStrategy<IGetAllKlassInstancesVariantsModel, IListModel<IKlassInstanceInformationModel>> {
  
}
