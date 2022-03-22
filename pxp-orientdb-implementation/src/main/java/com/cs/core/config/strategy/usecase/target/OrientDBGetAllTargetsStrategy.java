package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.entity.klass.IKlassBasic;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetAllTargetsStrategy extends OrientDBBaseStrategy
    implements IGetAllTargetsStrategy {
  
  public static final String useCase = "GetTargets";
  
  @Override
  public IListModel<IKlassInformationModel> execute(ITargetModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    ListModel<ITarget> targetList = execute(useCase, requestMap,
        new TypeReference<ListModel<ITarget>>()
        {
          
        });
    List<IKlassInformationModel> targetModelsList = new ArrayList<>();
    for (ITarget target : targetList.getList()) {
      targetModelsList.add(new KlassInformationModel((IKlassBasic) target));
    }
    IListModel<IKlassInformationModel> returnModel = new ListModel<>();
    returnModel.setList(targetModelsList);
    return returnModel;
  }
}
