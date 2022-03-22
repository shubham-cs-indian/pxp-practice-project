package com.cs.pim.runtime.articleinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

public interface ICreateArticleInstanceService
    extends IRuntimeService<ICreateInstanceModel, IKlassInstanceInformationModel> {
  
}
