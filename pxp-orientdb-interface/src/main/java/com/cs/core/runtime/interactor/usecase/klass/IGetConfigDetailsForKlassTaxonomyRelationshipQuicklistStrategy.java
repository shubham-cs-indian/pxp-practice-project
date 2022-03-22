package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForHierarchyRelationshipQuicklistModel;

public interface IGetConfigDetailsForKlassTaxonomyRelationshipQuicklistStrategy extends
    IConfigStrategy<IGetTargetKlassesModel, IConfigDetailsForHierarchyRelationshipQuicklistModel> {
}
