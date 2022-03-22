package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.filter.IKlassInstanceFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTreeInformationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetMarketInstanceTreeElements extends
IRuntimeInteractor<IKlassInstanceFilterModel, IListModel<IKlassInstanceTreeInformationModel>>{
  
}
