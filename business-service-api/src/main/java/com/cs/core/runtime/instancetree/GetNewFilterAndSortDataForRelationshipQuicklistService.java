package com.cs.core.runtime.instancetree;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForRQFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRQFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterAndSortDataForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataForRQResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipRecordBuilder;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGetRQFilterAndSortDataStrategy;

@Service
public class GetNewFilterAndSortDataForRelationshipQuicklistService extends AbstractGetNewFilterAndSortData<IGetFilterAndSortDataForRelationshipQuicklistRequestModel, IGetNewFilterAndSortDataForRQResponseModel> 
  implements IGetNewFilterAndSortDataForRelationshipQuicklistService {
  
  @Autowired
  private IGetConfigDetailsForGetRQFilterAndSortDataStrategy getConfigDetailsForGetRQFilterAndSortDataStrategy;
  
  @Override
  protected IConfigDetailsForFilterAndSortInfoRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForRQFilterAndSortDataRequestModel();
  }
  
  @Override
  protected IConfigDetailsForFilterAndSortInfoRequestModel prepareConfigRequestModel(IGetFilterAndSortDataForRelationshipQuicklistRequestModel model, List<String> entities)
  {
    IConfigDetailsForFilterAndSortInfoRequestModel configRequestModel = super.prepareConfigRequestModel(model, entities);
    ((IConfigDetailsForRQFilterAndSortDataRequestModel)configRequestModel).setRelationshipId(model.getRelationshipId());
    ((IConfigDetailsForRQFilterAndSortDataRequestModel)configRequestModel).setTargetId(model.getTargetIds().get(0));
    ((IConfigDetailsForRQFilterAndSortDataRequestModel)configRequestModel).setSideId(model.getSideId());
    return configRequestModel;
  }

  @Override
  protected void prepareRuntimeRequestModel(IGetFilterAndSortDataForRelationshipQuicklistRequestModel model,
      IGetNewFilterAndSortDataResponseModel configDetails)
  {
    super.prepareRuntimeRequestModel(model, configDetails);
    model.setTargetIds(((IGetNewFilterAndSortDataForRQResponseModel)configDetails).getTargetIds());
    model.setIsTargetTaxonomy(((IGetNewFilterAndSortDataForRQResponseModel)configDetails).getIsTargetTaxonomy());
  }
  
  @Override
  protected IGetNewFilterAndSortDataResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForFilterAndSortInfoRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetRQFilterAndSortDataStrategy.execute((IConfigDetailsForRQFilterAndSortDataRequestModel)configRequestModel);
  }

  @Override
  protected void additionalInformationForRelationshipFilter(
      IGetFilterAndSortDataForRelationshipQuicklistRequestModel model,
      IGetNewFilterAndSortDataResponseModel responseModel)
  {
    model.setModuleId(InstanceTreeUtils.getModuleIdByEntityId(responseModel.getAllowedEntities().get(0)));
    model.getKlassIdsHavingRP().addAll(((IGetNewFilterAndSortDataForRQResponseModel) responseModel).getTargetIds());
  }

  @Override
  protected void fillFilterData(IGetFilterAndSortDataForRelationshipQuicklistRequestModel model,
      IGetNewFilterAndSortDataResponseModel responseModel) throws Exception
  {
    List<String> targetIds = ((IGetNewFilterAndSortDataForRQResponseModel) responseModel).getTargetIds();
    if (model.getSelectedTypes().isEmpty() && model.getSelectedTaxonomyIds().isEmpty()) {
      if (((IGetNewFilterAndSortDataForRQResponseModel) responseModel).getIsTargetTaxonomy()) {
        model.setSelectedTaxonomyIds(targetIds);
      }
      else {
        model.setSelectedTypes(targetIds);
      }
    }
    evaluateAndFillFilterData(model, responseModel);
  }

  @Override
  protected void fillUsecaseSpecificFilters(IGetFilterAndSortDataForRelationshipQuicklistRequestModel dataModel,
      IGetNewFilterAndSortDataResponseModel configDetails, ISearchDTOBuilder searchBuilder)
      throws Exception
  { 
    IRelationship relationship = ((IGetNewFilterAndSortDataForRQResponseModel) configDetails)
        .getRelationshipConfig();
    String side = RelationshipRecordBuilder.getRelationshipSide(dataModel.getSideId(), relationship)
        .equals(RelationSide.SIDE_1) ? CommonConstants.RELATIONSHIP_SIDE_1 : CommonConstants.RELATIONSHIP_SIDE_2;
    String baseEntityIID = dataModel.getInstanceId();
    Collection<Long> idsToExclude = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getOtherSideInstanceIIds(side, baseEntityIID, relationship.getPropertyIID());
    idsToExclude.add(Long.parseLong(baseEntityIID));
    searchBuilder.setIdsToExclude(idsToExclude);
  }

}
