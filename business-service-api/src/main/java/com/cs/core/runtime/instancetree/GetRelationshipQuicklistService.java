package com.cs.core.runtime.instancetree;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesTreeStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.GetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipRecordBuilder;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForRelationshipQuicklistStrategy;

@Service
public class GetRelationshipQuicklistService
    extends AbstractNewInstanceTree<IGetRelationshipQuicklistRequestModel, IGetNewInstanceTreeResponseModel>
    implements IGetRelationshipQuicklistService {
  
  @Autowired
  protected IGetConfigDetailsForRelationshipQuicklistStrategy getConfigDetailsForRelationshipQuicklistStrategy;
  
  @Autowired
  protected IGetKlassesTreeStrategy                           getKlassesTreeStrategy;
  
  @Autowired
  protected IGetFilterAndSortDataStrategy                     getFilterAndSortDataForKlassStrategy;
  
  @Override
  protected ConfigDetailsForRelationshipQuicklistRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForRelationshipQuicklistRequestModel();
  }
  
  @Override
  protected IGetConfigDetailsForGetNewInstanceTreeRequestModel prepareConfigRequestModel(IGetRelationshipQuicklistRequestModel model,
      List<String> entities)
  {
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = super.prepareConfigRequestModel(model, entities);
    ((IConfigDetailsForRelationshipQuicklistRequestModel) configRequestModel).setRelationshipId(model.getRelationshipId());
    ((IConfigDetailsForRelationshipQuicklistRequestModel) configRequestModel).setTargetId(model.getTargetIds().get(0));
    ((IConfigDetailsForRelationshipQuicklistRequestModel) configRequestModel).setSideId(model.getSideId());
    return configRequestModel;
  }
  
  @Override
  protected void prepareRuntimeRequestModel(IGetRelationshipQuicklistRequestModel model,
      IConfigDetailsForNewInstanceTreeModel configDetails)
  {
    super.prepareRuntimeRequestModel(model, configDetails);
    model.setTargetIds(((IConfigDetailsForRelationshipQuicklistResponseModel) configDetails).getTargetIds());
    model.setIsTargetTaxonomy(((IConfigDetailsForRelationshipQuicklistResponseModel) configDetails).getIsTargetTaxonomy());
    model.setSide2LinkedVariantKrIds(((IConfigDetailsForRelationshipQuicklistResponseModel) configDetails).getSide2LinkedVariantKrIds());
  }
  
  @Override
  protected IGetNewInstanceTreeResponseModel executeRuntimeStrategy(IGetRelationshipQuicklistRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws Exception
  {
    IGetNewInstanceTreeResponseModel responseModel = new GetNewInstanceTreeResponseModel();
    
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    List<String> targetIds = ((IConfigDetailsForRelationshipQuicklistResponseModel) configDetails).getTargetIds();
    if (dataModel.getSelectedTypes().isEmpty() && dataModel.getSelectedTaxonomyIds().isEmpty()) {
      if (dataModel.getIsTargetTaxonomy()) {
        dataModel.setSelectedTaxonomyIds(targetIds);
      }
      else {
        dataModel.setSelectedTypes(targetIds);
      }
    }

    List<IBaseEntityDTO> baseEntityDTOs = getBaseEntityDTOsAsPerSearchCriteria(dataModel, configDetails,
        localeCatalogDao, responseModel, idVsHighlightsMap);
    
    List<IKlassInstanceInformationModel> children = responseModel.getChildren();
    if (baseEntityDTOs != null && !baseEntityDTOs.isEmpty()) {
      populateResponseListRelationship(children, baseEntityDTOs, configDetails, localeCatalogDao);
    }
    prepareResponseModel(dataModel, responseModel, baseEntityDTOs);
    
    return responseModel;
  }

  protected void populateResponseListRelationship(List<IKlassInstanceInformationModel> responseList, List<IBaseEntityDTO> listOfDTO,
      IConfigDetailsForNewInstanceTreeModel configDetails, ILocaleCatalogDAO localeCatalogDao) throws Exception
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    Set<IPropertyDTO> properties = getAllUtils.getXrayProperties(referencedAttributes, referencedTags);
    List<String> linkedVarantCodes = ((IConfigDetailsForRelationshipQuicklistResponseModel) configDetails).getLinkedVariantCodes();
    
    Map<Long, Long> variantIdParentIdMap = getVariantIdParentIdMap(listOfDTO, localeCatalogDao, linkedVarantCodes);
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO,
          rdbmsComponentUtils);
      responseList.add(klassInstanceInformationModel);
      Long baseEntityIID = baseEntityDTO.getBaseEntityIID();
      fillVariantOfInfo(variantIdParentIdMap, klassInstanceInformationModel, baseEntityIID);
      getAllUtils.getXrayPropertyDetails(referencedAttributes, referencedTags, baseEntityDTO, properties, klassInstanceInformationModel);
    }
  }
  
  @Override
  protected void fillUsecaseSpecificFilters(IGetInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, ISearchDTOBuilder searchBuilder) throws Exception
  {
    IRelationship relationship = ((IConfigDetailsForRelationshipQuicklistResponseModel) configDetails)
        .getRelationshipConfig();
    IGetRelationshipQuicklistRequestModel model = (IGetRelationshipQuicklistRequestModel) dataModel;
    String side = RelationshipRecordBuilder.getRelationshipSide(model.getSideId(), relationship)
        .equals(RelationSide.SIDE_1) ? CommonConstants.RELATIONSHIP_SIDE_1 : CommonConstants.RELATIONSHIP_SIDE_2;
    
    String baseEntityIID = ((IGetRelationshipQuicklistRequestModel) dataModel).getInstanceId();
    Collection<Long> idsToExclude = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getOtherSideInstanceIIds(side, baseEntityIID, relationship.getPropertyIID());
    idsToExclude.add(Long.parseLong(baseEntityIID));
    searchBuilder.setIdsToExclude(idsToExclude);
  }

  
  @Override
  protected IConfigDetailsForNewInstanceTreeModel executeConfigDetailsStrategy(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForRelationshipQuicklistStrategy
        .execute((IConfigDetailsForRelationshipQuicklistRequestModel) configRequestModel);
  }
  
  @Override
  protected List<String> getModuleEntities(IGetRelationshipQuicklistRequestModel model) throws Exception
  {
    return null;
  }
  
}
