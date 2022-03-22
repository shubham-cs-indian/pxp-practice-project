package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.datarule.GetAllDisplayAndRelavanceTagIds;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.datarule.IGetAllDisplayAndRelevanceTagIdsModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class OrientDBGetDisplayAndReleavanceTagIdsStrategy extends OrientDBBaseStrategy
    implements IGetDisplayAndRelevanceTagIdsStrategy {
  
  public static final String useCase = "GetAllDisplayAndRelevanceTagIds";
  
  @Override
  public IGetAllDisplayAndRelevanceTagIdsModel execute(ITagModel model) throws Exception
  {
    return execute(useCase, new HashMap<String, Object>(), GetAllDisplayAndRelavanceTagIds.class);
  }
}
