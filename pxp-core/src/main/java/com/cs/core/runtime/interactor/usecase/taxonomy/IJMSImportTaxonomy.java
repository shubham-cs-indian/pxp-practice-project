package com.cs.core.runtime.interactor.usecase.taxonomy;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.taxonomy.IJMSImportTaxanomyStatusModel;
import com.cs.core.config.interactor.model.taxonomy.IJMSImportTaxonomyModel;

public interface IJMSImportTaxonomy
    extends IGetConfigInteractor<IJMSImportTaxonomyModel, IJMSImportTaxanomyStatusModel> {
}
