package com.cs.core.runtime.imprt.revisioncreation;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateZeroRevisionForBaseEntity extends IRuntimeInteractor<IVoidModel, IVoidModel> {
  
  public IVoidModel execute(IVoidModel dataModel) throws Exception;
}
