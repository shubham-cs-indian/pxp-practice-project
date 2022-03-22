
package com.cs.core.runtime.interactor.usecase.instance;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public abstract class AbstractCreateInstance<P extends ICreateInstanceModel, R extends IKlassInstanceInformationModel>
    extends AbstractRuntimeInteractor<P, R> {
  

}
