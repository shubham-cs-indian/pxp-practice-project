package com.cs.core.config.strategy.usecase.causeeffectrule;

import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.CauseEffectRulesInformationModel;
import com.cs.core.config.interactor.model.datarule.ICauseEffectRulesInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetCauseEffectRulesStrategy extends OrientDBBaseStrategy
    implements IGetAllCauseEffectRulesStrategy {
  
  public static final String useCase = "GetCauseEffectRules";
  
  @Override
  public IListModel<ICauseEffectRulesInformationModel> execute(ICauseEffectRulesModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    ;
    return super.execute(useCase, requestMap,
        new TypeReference<ListModel<CauseEffectRulesInformationModel>>()
        {
          
        });
  }
}
