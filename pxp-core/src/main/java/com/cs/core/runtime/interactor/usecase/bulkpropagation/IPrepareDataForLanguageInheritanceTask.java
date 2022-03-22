package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import com.cs.core.config.interactor.model.language.IKlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IPrepareDataForLanguageInheritanceTask
    extends IRuntimeInteractor<IKlassInstanceDiffForLanguageInheritanceModel, IModel> {
}
