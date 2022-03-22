package com.cs.core.runtime.interactor.usecase.task;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.language.IUpdateSchemaOnLangaugeCreateModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IUpdateSchemaOnLanguageCreateTaxonomyTask
    extends IConfigInteractor<IUpdateSchemaOnLangaugeCreateModel, IIdsListParameterModel> {
}
