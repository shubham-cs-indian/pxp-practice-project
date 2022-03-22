package com.cs.core.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveArticleInstanceRelationships
    extends IRuntimeInteractor<ISaveRelationshipInstanceModel, IGetKlassInstanceModel> {
}
