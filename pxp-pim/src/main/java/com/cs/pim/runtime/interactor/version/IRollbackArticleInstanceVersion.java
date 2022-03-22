package com.cs.pim.runtime.interactor.version;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IRollbackArticleInstanceVersion extends
    IRuntimeInteractor<IKlassInstanceVersionRollbackModel, IGetKlassInstanceModel> {
  
}
