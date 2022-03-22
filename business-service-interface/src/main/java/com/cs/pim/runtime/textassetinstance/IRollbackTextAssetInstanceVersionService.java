package com.cs.pim.runtime.textassetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

public interface IRollbackTextAssetInstanceVersionService
    extends IRuntimeService<IKlassInstanceVersionRollbackModel, IGetKlassInstanceModel> {
  
}
