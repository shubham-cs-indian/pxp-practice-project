package com.cs.core.config.interactor.usecase.relationship;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkCreateRelationships
    extends ICreateConfigInteractor<IListModel<IRelationshipModel>, IPluginSummaryModel> {
  
}
