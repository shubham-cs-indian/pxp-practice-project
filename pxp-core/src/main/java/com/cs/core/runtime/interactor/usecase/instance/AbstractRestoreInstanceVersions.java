package com.cs.core.runtime.interactor.usecase.instance;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

public abstract class AbstractRestoreInstanceVersions<P extends IMoveKlassInstanceVersionsModel, R extends IMoveKlassInstanceVersionsSuccessModel>
    extends AbstractRuntimeInteractor<P, R> {
  
}
