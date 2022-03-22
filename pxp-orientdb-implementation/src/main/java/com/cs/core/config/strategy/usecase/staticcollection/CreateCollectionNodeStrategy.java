package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.interactor.model.collections.CollectionNodeModel;
import com.cs.core.config.interactor.model.collections.ICollectionNodeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateCollectionNodeStrategy extends OrientDBBaseStrategy
    implements ICreateStaticCollectionNodeStrategy {
  
  @Override
  public ICollectionNodeModel execute(ICollectionNodeModel model) throws Exception
  {
    return execute(CREATE_COLLECTION_NODE, model, CollectionNodeModel.class);
  }
}
