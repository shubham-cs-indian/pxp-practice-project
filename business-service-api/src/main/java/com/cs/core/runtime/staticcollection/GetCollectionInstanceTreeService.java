package com.cs.core.runtime.staticcollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.CollectionFilterDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.instancetree.AbstractNewInstanceTree;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.GetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetCollectionInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Service
public class GetCollectionInstanceTreeService
    extends AbstractNewInstanceTree<IGetCollectionInstanceTreeRequestModel, IGetNewInstanceTreeResponseModel>
    implements IGetCollectionInstanceTreeService {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  @Autowired
  protected SearchAssembler     searchAssembler;
  
  @Autowired
  protected GetAllUtils         getAllUtils;
  
  public void populateResponseList(List<IKlassInstanceInformationModel> responseList, List<IBaseEntityDTO> listOfDTO,
      IConfigDetailsForGetNewInstanceTreeModel configDetails) throws Exception
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    Set<IPropertyDTO> properties = getAllUtils.getXrayProperties(referencedAttributes, referencedTags);
    List<String> linkedVariantCodes = configDetails.getLinkedVariantCodes();
    Map<Long, Long> variantIdParentIdMap = getVariantIdParentIdMap(listOfDTO, rdbmsComponentUtils.getLocaleCatlogDAO(), linkedVariantCodes);
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
  protected IGetConfigDetailsForGetNewInstanceTreeRequestModel getConfigDetailsRequestModel()
  {
    return new GetConfigDetailsForGetNewInstanceTreeRequestModel();
  }
  
  @Override
  protected IConfigDetailsForNewInstanceTreeModel executeConfigDetailsStrategy(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetNewInstanceTreeStrategy.execute(configRequestModel);
  }
  
  @Override
  protected IGetNewInstanceTreeResponseModel executeRuntimeStrategy(IGetCollectionInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws Exception
  {
    IGetNewInstanceTreeResponseModel responseModel = new GetNewInstanceTreeResponseModel();
    
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();

    List<IBaseEntityDTO> baseEntities = getBaseEntityDTOsAsPerSearchCriteria(
        dataModel, configDetails, localeCatalogDao, responseModel, idVsHighlightsMap);

    List<IKlassInstanceInformationModel> children = responseModel.getChildren();
    if (baseEntities != null && !baseEntities.isEmpty()) {
      populateResponseList(children, baseEntities,(IConfigDetailsForGetNewInstanceTreeModel) configDetails);
    }
    prepareResponseModel(dataModel, responseModel, baseEntities);
    return responseModel;
  }
  
  @Override
  protected void fillUsecaseSpecificFilters(IGetInstanceTreeRequestModel dataModel, IConfigDetailsForNewInstanceTreeModel configDetails,
      ISearchDTOBuilder searchBuilder)
  {
    IGetCollectionInstanceTreeRequestModel collectionInstnaceTreeModel = (IGetCollectionInstanceTreeRequestModel) dataModel;
    CollectionFilterDTO collectionFilterDto = new CollectionFilterDTO(collectionInstnaceTreeModel.getIsQuicklist(),collectionInstnaceTreeModel.getCollectionId());
    searchBuilder.addCollectionFilters(collectionFilterDto);
  }
  
  /* public String generateSearchExpression(IGetCollectionInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails) throws Exception
  {
    StringBuilder searchExpression = searchAssembler.getBaseQuery(BaseType.UNDEFINED);
    
    StringBuilder generateEntityFilterExpression = searchAssembler.generateEntityFilterExpression(dataModel.getSelectedTypes(),
        dataModel.getSelectedTaxonomyIds(), false);
    
    StringBuilder permissionExpression = searchAssembler.generateEntityFilterExpression(List.copyOf(dataModel.getKlassIdsHavingRP()),
        List.copyOf(dataModel.getTaxonomyIdsHavingRP()), true);
    
    if (!generateEntityFilterExpression.toString().isEmpty() && !permissionExpression.toString().isEmpty()) {
      generateEntityFilterExpression.append(" and ");
    }
    generateEntityFilterExpression.append(permissionExpression.toString());
    
    String dataQualityExpression = searchAssembler.generateSpecialUsecaseFilterExpression(dataModel.getSpecialUsecaseFilters());
    if (!generateEntityFilterExpression.toString().isEmpty() && !dataQualityExpression.isEmpty()) {
      generateEntityFilterExpression.append(" and ");
    }
    generateEntityFilterExpression.append(dataQualityExpression);
    
    String expressionForCollectionBaseEntities = getExpressionForCollectionBaseEntities(dataModel);
    
    if (!generateEntityFilterExpression.toString().isEmpty() && !expressionForCollectionBaseEntities.isEmpty()) {
      generateEntityFilterExpression.append(" and ");
      generateEntityFilterExpression.append(expressionForCollectionBaseEntities);
    }
    else if (!expressionForCollectionBaseEntities.isEmpty()) {
      searchExpression.append(expressionForCollectionBaseEntities);
    }
    
    if (!generateEntityFilterExpression.toString().isEmpty()) {
      searchExpression.append(generateEntityFilterExpression);
    }
    
    String evaluationExpression = instanceTreeUtils.evaluationExpressionForAttributeAndTags(dataModel);
    if (!evaluationExpression.isEmpty()) {
      searchExpression.append(" where ").append(evaluationExpression);
    }
    return searchExpression.toString();
  }*/
  
  private String getExpressionForCollectionBaseEntities(IGetCollectionInstanceTreeRequestModel dataModel)
      throws NumberFormatException, Exception
  {
    if (dataModel.getIsQuicklist()) {
      return "";
    }
    else {
      String id = dataModel.getCollectionId();
      return String.format(" $entity belongsto [b>%s $iid=%d] ", "staticCollection", Long.parseLong(id));
    }
  }
  
  @Override
  protected List<String> getModuleEntities(IGetCollectionInstanceTreeRequestModel model) throws Exception
  {
    if (model.getModuleEntities() != null && !model.getModuleEntities().isEmpty()) {
      return model.getModuleEntities();
    }
    else {
      IModule module = moduleMappingUtil.getModule(model.getModuleId());
      List<String> entities = module.getEntities();
      return entities;
    }
  }
  
}
