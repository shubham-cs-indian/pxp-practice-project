package com.cs.bds.config.usecase.taxonomy;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;

public interface ITypeSwitchInstance  extends IRuntimeService<IKlassInstanceTypeSwitchModel, IConfigDetailsForSwitchTypeRequestModel>{
  
}
