package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.GetChildKlassIdsInHierarchyModel;
import com.cs.core.config.interactor.model.klass.IGetChildKlassIdsInHierarchyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetChildKlassIdsInHierarchyStrategy extends OrientDBBaseStrategy
    implements IGetChildKlassIdsInHierarchyStrategy {
  
  @Override
  public IGetChildKlassIdsInHierarchyModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_CHILD_KLASS_IDS_IN_HIERARCHY, model, GetChildKlassIdsInHierarchyModel.class);
  }
}
