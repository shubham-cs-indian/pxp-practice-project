package com.cs.core.config.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.collections.IMoveCollectionNodeModel;
import com.cs.core.config.strategy.usecase.staticcollection.IMoveStaticCollectionHierarchyStrategy;

@Service
public class MoveStaticCollectionHierarchy
    extends AbstractSaveConfigInteractor<IMoveCollectionNodeModel, IMoveCollectionNodeModel>
    implements IMoveStaticCollectionHierarchy {
  
  @Autowired
  protected IMoveStaticCollectionHierarchyStrategy moveStaticCollectionHierarchyStrategy;
  
  @Override
  public IMoveCollectionNodeModel executeInternal(IMoveCollectionNodeModel model) throws Exception
  {
    return moveStaticCollectionHierarchyStrategy.execute(model);
  }
}
