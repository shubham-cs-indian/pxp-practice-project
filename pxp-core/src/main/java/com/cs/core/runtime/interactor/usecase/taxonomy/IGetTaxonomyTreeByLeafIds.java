package com.cs.core.runtime.interactor.usecase.taxonomy;

import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetTaxonomyTreeByLeafIds
    extends IRuntimeInteractor<IGetTaxonomyTreeModel, ICategoryInformationModel> {
  
}
