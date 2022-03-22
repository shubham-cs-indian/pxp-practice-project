package com.cs.core.runtime.strategy.usecase.klassinstance.version;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IKlassInstanceVersionsArchiveStrategy extends
    IRuntimeStrategy<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel> {
  
}
