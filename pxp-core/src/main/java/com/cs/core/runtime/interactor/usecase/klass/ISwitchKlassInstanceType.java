package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISwitchKlassInstanceType
    extends IRuntimeInteractor<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel> {
}
