package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.goldenrecord.ICreateGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IAutoCreateGoldenRecord
    extends IRuntimeInteractor<ICreateGoldenRecordRequestModel, IGetKlassInstanceModel> {
  
}
