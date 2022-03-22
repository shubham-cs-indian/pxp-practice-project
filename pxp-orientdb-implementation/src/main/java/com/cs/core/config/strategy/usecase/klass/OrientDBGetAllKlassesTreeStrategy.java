package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.KlassTaxonomyTreeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassTaxonomyTreeModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAllKlassesTreeStrategy")
public class OrientDBGetAllKlassesTreeStrategy extends OrientDBBaseStrategy
    implements IGetAllKlassesTreeStrategy {
  
  @Override
  public IListModel<IKlassTaxonomyTreeModel> execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(GET_ALL_KLASSES_TREE, requestMap,
        new TypeReference<ListModel<KlassTaxonomyTreeModel>>()
        {
          
        });
  }
}
