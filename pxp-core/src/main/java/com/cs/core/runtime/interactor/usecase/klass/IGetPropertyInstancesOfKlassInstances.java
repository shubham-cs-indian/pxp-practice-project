package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTableViewGetPropertiesModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetPropertyInstancesOfKlassInstances extends
    IRuntimeInteractor<IKlassInstanceTableViewGetPropertiesModel, IListModel<IKlassInstance>> {
}
