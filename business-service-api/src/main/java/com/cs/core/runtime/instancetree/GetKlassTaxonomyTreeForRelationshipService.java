package com.cs.core.runtime.instancetree;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForRelationshipKlassTaxonomyRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForRelationshipKlassTaxonomyResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipKlassTaxonomyRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.GetKlassTaxonomyTreeForRelationshipRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeForRelationshipRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipRecordBuilder;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailForRelationshipKlassTaxonomyTreeStrategy;

@Service
public class GetKlassTaxonomyTreeForRelationshipService extends AbstractKlassTaxonomyTree<IGetKlassTaxonomyTreeForRelationshipRequestModel, 
    IGetKlassTaxonomyTreeResponseModel> implements IGetKlassTaxonomyTreeForRelationshipService {
  
  @Autowired
  private IGetConfigDetailForRelationshipKlassTaxonomyTreeStrategy getConfigDetailForRelationshipKlassTaxonomyTreeStrategy;
  
  protected List<String> getModuleEntities(IGetKlassTaxonomyTreeForRelationshipRequestModel model) throws Exception
  {
    return model.getModuleEntities();
  }
  
  @Override
  protected IConfigDetailsForGetKlassTaxonomyTreeRequestModel prepareConfigRequestModel(IGetKlassTaxonomyTreeForRelationshipRequestModel dataModel, List<String> entities)
  {
    IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel = super.prepareConfigRequestModel(dataModel, entities);
    ((IConfigDetailsForRelationshipKlassTaxonomyRequestModel) configRequsetModel).setTargetId(dataModel.getTargetIds().get(0));
    ((IConfigDetailsForRelationshipKlassTaxonomyRequestModel) configRequsetModel).setType(dataModel.getType());
    ((IConfigDetailsForRelationshipKlassTaxonomyRequestModel) configRequsetModel).setRelationshipId(dataModel.getRelationshipId());
    ((IConfigDetailsForRelationshipKlassTaxonomyRequestModel) configRequsetModel).setSideId(dataModel.getSideId());
    return configRequsetModel;
  }
  
  @Override
  protected ConfigDetailsForGetKlassTaxonomyTreeRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForRelationshipKlassTaxonomyRequestModel();
  }
  
  @Override
  protected IConfigDetailsKlassTaxonomyTreeResponseModel executeConfigDetailsStrategy(IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel) throws Exception
  {
    return getConfigDetailForRelationshipKlassTaxonomyTreeStrategy.execute(configRequsetModel);
  }
  
  @Override
  protected IGetKlassTaxonomyTreeResponseModel executeRuntimeStrategy(
      IGetKlassTaxonomyTreeForRelationshipRequestModel dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData) throws Exception
  {
    return getKlassTaxonomyTreeData(dataModel, configData);
  }
  
  protected void additionalInformationForRelationshipFilter(IGetKlassTaxonomyTreeForRelationshipRequestModel model,
      IConfigDetailsKlassTaxonomyTreeResponseModel responseModel)
  {
    if (((GetKlassTaxonomyTreeForRelationshipRequestModel) model).getType()
        .equals(CommonConstants.RELATIONSHIP)) {
      model.setModuleId(InstanceTreeUtils.getModuleIdByEntityId(responseModel.getAllowedEntities()
          .get(0)));
      /*model.getKlassIdsHavingRP()
                .addAll(((ConfigDetailsForRelationshipKlassTaxonomyResponseModel) responseModel).getTargetIds());*/
    }
  }
  
  @Override
  protected void fillUsecaseSpecificFilters(IGetKlassTaxonomyTreeRequestModel dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configDetails, ISearchDTOBuilder searchBuilder)
      throws Exception
  {
    IRelationship relationship = ((ConfigDetailsForRelationshipKlassTaxonomyResponseModel) configDetails)
        .getRelationshipConfig();
    String side = RelationshipRecordBuilder.getRelationshipSide(
            ((GetKlassTaxonomyTreeForRelationshipRequestModel) dataModel).getSideId(), relationship)
        .equals(RelationSide.SIDE_1) ? CommonConstants.RELATIONSHIP_SIDE_1
            : CommonConstants.RELATIONSHIP_SIDE_2;
    
    String baseEntityIID = ((GetKlassTaxonomyTreeForRelationshipRequestModel) dataModel).getInstanceId();
    Collection<Long> idsToExclude = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getOtherSideInstanceIIds(side, baseEntityIID, relationship.getPropertyIID());
    idsToExclude.add(Long.parseLong(baseEntityIID));
    searchBuilder.setIdsToExclude(idsToExclude);
  }

}
