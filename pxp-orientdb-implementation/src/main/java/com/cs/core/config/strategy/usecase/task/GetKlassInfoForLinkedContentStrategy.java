package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.taskinstance.ConfigTaskContentTypeResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskContentTypeResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskReferencesModel;
import org.springframework.stereotype.Component;

@Component
public class GetKlassInfoForLinkedContentStrategy extends OrientDBBaseStrategy
    implements IGetKlassInfoForLinkedContentStrategy {
  
  @Override
  public IConfigTaskContentTypeResponseModel execute(IConfigTaskReferencesModel model)
      throws Exception
  {
    return execute(GET_KLASS_INFO_OF_LINKED_CONTENT, model,
        ConfigTaskContentTypeResponseModel.class);
  }
}
