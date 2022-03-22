package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.filter.IKlassInstanceQuickListModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IQuickListCollectionKlassInstances extends
    IRuntimeInteractor<IKlassInstanceQuickListModel, IListModel<IKlassInstanceInformationModel>> {
}
