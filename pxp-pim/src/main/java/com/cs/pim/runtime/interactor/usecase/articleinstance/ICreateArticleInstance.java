package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateArticleInstance
    extends IRuntimeInteractor<ICreateInstanceModel, IKlassInstanceInformationModel> {
  
}
