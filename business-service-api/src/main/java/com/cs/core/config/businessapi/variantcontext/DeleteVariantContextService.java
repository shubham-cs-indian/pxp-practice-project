package com.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.variantcontext.IDeleteVariantContextStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantContextReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import coms.cs.core.config.businessapi.variantcontext.IDeleteVariantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DeleteVariantContextService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteVariantContextReturnModel>
  implements IDeleteVariantContextService {
  
  @Autowired
  protected IDeleteVariantContextStrategy deleteVariantContextStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy getContextEntityConfigurationStrategy;

  @Override
  public IBulkDeleteVariantContextReturnModel executeInternal(IIdsListParameterModel variantContextModel)
      throws Exception
  {

    IGetEntityConfigurationResponseModel getEntityResponse = getContextEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(variantContextModel.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();

    if (!referenceData.keySet()
        .isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }

    IBulkDeleteVariantContextReturnModel response = deleteVariantContextStrategy
        .execute(variantContextModel);
    /*List<String> relationshipIds = response.getRelationshipIds();
    if(!relationshipIds.isEmpty()) {
      IContentDiffModelToPrepareDataForBulkPropagation contentDiffToPrepareDataForBulkPropagationModel = new ContentDiffModelToPrepareDataForBulkPropagation();
      contentDiffToPrepareDataForBulkPropagationModel.setDeletedNatureRelationshipIds(relationshipIds);
      kafkaUtils.prepareMessageData(contentDiffToPrepareDataForBulkPropagationModel, PrepareDataForBulkPropagationTask.class.getName());
    }*/
    return response;
  }
}
