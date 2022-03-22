package com.cs.pim.runtime.interactor.version;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IRestoreArticleInstanceVersions extends
    IRuntimeInteractor<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel> {
  
}
