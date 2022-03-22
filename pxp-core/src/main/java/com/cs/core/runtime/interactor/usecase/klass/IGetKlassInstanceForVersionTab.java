package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionGetModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetKlassInstanceForVersionTab extends
    IRuntimeInteractor<IKlassInstanceVersionGetModel, IGetKlassInstanceVersionsForTimeLineModel> {
}
