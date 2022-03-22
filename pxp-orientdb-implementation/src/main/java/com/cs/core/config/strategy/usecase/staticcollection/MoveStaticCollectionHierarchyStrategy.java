package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.interactor.model.collections.IMoveCollectionNodeModel;
import com.cs.core.config.interactor.model.collections.MoveCollectionNodeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class MoveStaticCollectionHierarchyStrategy extends OrientDBBaseStrategy
    implements IMoveStaticCollectionHierarchyStrategy {
  
  @Override
  public IMoveCollectionNodeModel execute(IMoveCollectionNodeModel model) throws Exception
  {
    return execute(MOVE_STATIC_COLLECTION, model, MoveCollectionNodeModel.class);
  }
}
