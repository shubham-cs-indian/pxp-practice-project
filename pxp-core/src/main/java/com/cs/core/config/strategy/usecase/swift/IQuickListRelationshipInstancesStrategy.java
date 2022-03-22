package com.cs.core.config.strategy.usecase.swift;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IQuickListRelationshipInstancesStrategy
    extends IRuntimeStrategy<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel> {
  
}