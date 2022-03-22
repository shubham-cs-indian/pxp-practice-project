package com.cs.core.runtime.instancetree;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetRQFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetRQFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetRQFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetRQFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipRecordBuilder;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGetRQFilterChildrenStrategy;

@Service
public class GetFilterChildrenForRelationshipQuicklistService extends AbstractGetFilterChildrenValues<IGetFilterChildrenForRelationshipQuicklistRequestModel, IGetFilterChildrenResponseModel> 
  implements IGetFilterChildrenForRelationshipQuicklistService{
  
  @Autowired
  private IGetConfigDetailsForGetRQFilterChildrenStrategy getConfigDetailsForGetRQFilterChildrenStrategy;
  
  @Autowired
  private RDBMSComponentUtils                             rdbmsComponentUtils;
  

  protected IConfigDetailsForGetFilterChildrenRequestModel prepareConfigRequestModel(IGetFilterChildrenForRelationshipQuicklistRequestModel model,
      List<String> entities)
  {
    IConfigDetailsForGetFilterChildrenRequestModel configRequestModel = super.prepareConfigRequestModel(model, entities);
    ((IConfigDetailsForGetRQFilterChildrenRequestModel)configRequestModel).setRelationshipId(model.getRelationshipId());
    ((IConfigDetailsForGetRQFilterChildrenRequestModel)configRequestModel).setType(model.getType());
    ((IConfigDetailsForGetRQFilterChildrenRequestModel)configRequestModel).setTargetId(model.getTargetIds().get(0));
    ((IConfigDetailsForGetRQFilterChildrenRequestModel)configRequestModel).setSideId(model.getSideId());
    return configRequestModel;
  }
  
  protected void prepareRuntimeRequestModel(IGetFilterChildrenForRelationshipQuicklistRequestModel model,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails)
  {
    super.prepareRuntimeRequestModel(model, configDetails);
    model.setTargetIds(((IConfigDetailsForGetRQFilterChildrenResponseModel)configDetails).getTargetIds());
    model.setIsTargetTaxonomy(((IConfigDetailsForGetRQFilterChildrenResponseModel)configDetails).getIsTargetTaxonomy());
  }
  
  @Override
  protected IConfigDetailsForGetFilterChildrenResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForGetFilterChildrenRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetRQFilterChildrenStrategy.execute((IConfigDetailsForGetRQFilterChildrenRequestModel)configRequestModel);
  }

  @Override
  protected IConfigDetailsForGetFilterChildrenRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForGetRQFilterChildrenRequestModel();
  }

  @Override
  protected List<IGetFilterChildrenModel> executeRuntimeStrategy(
      IGetFilterChildrenForRelationshipQuicklistRequestModel model,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails) throws Exception
  {
    List<String> targetIds = ((IConfigDetailsForGetRQFilterChildrenResponseModel) configDetails)
        .getTargetIds();
    if (model.getSelectedTypes().isEmpty() && model.getSelectedTaxonomyIds().isEmpty()) {
      if (model.getIsTargetTaxonomy()) {
        model.setSelectedTaxonomyIds(targetIds);
      }
      else {
        model.setSelectedTypes(targetIds);
      }
    }
    
    return getFilterChildrenValues(model, configDetails);
  }
  
  @Override
  protected void fillUsecaseSpecificFilters(
      IGetFilterChildrenForRelationshipQuicklistRequestModel dataModel,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails,
      ISearchDTOBuilder searchBuilder) throws Exception
  {
    IRelationship relationship = ((ConfigDetailsForGetRQFilterChildrenResponseModel) configDetails)
        .getRelationshipConfig();
    String side = RelationshipRecordBuilder.getRelationshipSide(dataModel.getSideId(), relationship)
        .equals(RelationSide.SIDE_1) ? CommonConstants.RELATIONSHIP_SIDE_1
            : CommonConstants.RELATIONSHIP_SIDE_2;
    
    String baseEntityIID = dataModel.getInstanceId();
    Collection<Long> idsToExclude = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getOtherSideInstanceIIds(side, baseEntityIID, relationship.getPropertyIID());
    idsToExclude.add(Long.parseLong(baseEntityIID));
    searchBuilder.setIdsToExclude(idsToExclude);
  }
  
}
