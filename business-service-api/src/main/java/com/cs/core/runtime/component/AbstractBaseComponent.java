package com.cs.core.runtime.component;

import com.cs.core.runtime.component.IBaseComponent;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractBaseComponent<P extends IModel, R extends IModel>
    implements IBaseComponent<P, R> {
}
