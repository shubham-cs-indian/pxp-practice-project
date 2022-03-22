package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.ProjectKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetKlassesByIdsStrategy extends OrientDBBaseStrategy
    implements IGetKlassesStrategy {
  
  public static final String useCase = "GetKlassesById";
  
  @Override
  public IListModel<IKlass> execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    IListModel<IKlass> klassesList = execute(useCase, requestMap,
        new TypeReference<ListModel<ProjectKlass>>()
        {
          
        });
    if (klassesList.getList()
        .size() == 0) {
      throw new KlassNotFoundException();
    }
    return klassesList;
  }
}
