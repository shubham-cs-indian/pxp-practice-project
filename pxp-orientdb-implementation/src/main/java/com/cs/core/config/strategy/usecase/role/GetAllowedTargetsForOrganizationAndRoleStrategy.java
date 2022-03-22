package com.cs.core.config.strategy.usecase.role;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.klass.GetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.role.IGetAllowedTargetsForRoleRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetAllowedTargetsForOrganizationAndRoleStrategy extends OrientDBBaseStrategy
    implements IGetAllowedTargetsForOrganizationAndRoleStrategy {
  
  @Override
  public IGetMajorTaxonomiesResponseModel execute(IGetAllowedTargetsForRoleRequestModel model)
      throws Exception
  {
    return execute(GET_ALLOWED_TARGETS_FOR_ROLE, model,
        new TypeReference<GetMajorTaxonomiesResponseModel>()
        {
          
        });
  }
}
