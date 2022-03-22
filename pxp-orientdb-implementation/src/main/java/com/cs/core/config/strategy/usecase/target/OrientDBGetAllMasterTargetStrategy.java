package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.interactor.model.target.TargetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetAllMasterTargetStrategy extends OrientDBBaseStrategy
    implements IGetAllMasterTargetsStrategy {
  
  public static final String useCase = "GetAllMasterTargets";
  
  @Override
  public IListModel<ITargetModel> execute(ITargetModel model) throws Exception
  {
    List<ITargetModel> targetModelsList = new ArrayList<ITargetModel>();
    Map<String, Object> requestMap = new HashMap<>();
    ListModel<ITarget> targetList = execute(useCase, requestMap,
        new TypeReference<ListModel<ITarget>>()
        {
          
        });
    for (ITarget target : targetList.getList()) {
      targetModelsList.add(new TargetModel(target));
    }
    IListModel<ITargetModel> returnModel = new ListModel<>();
    returnModel.setList(targetModelsList);
    
    return returnModel;
  }
}
