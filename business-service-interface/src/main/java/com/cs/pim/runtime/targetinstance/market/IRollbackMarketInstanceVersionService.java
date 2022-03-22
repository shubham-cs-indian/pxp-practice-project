package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

public interface IRollbackMarketInstanceVersionService extends IRuntimeService<IKlassInstanceVersionRollbackModel, IGetKlassInstanceModel> {
  
}
