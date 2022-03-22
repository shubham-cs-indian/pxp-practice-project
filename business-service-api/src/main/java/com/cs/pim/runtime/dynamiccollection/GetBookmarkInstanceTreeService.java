package com.cs.pim.runtime.dynamiccollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.instancetree.AbstractNewInstanceTree;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.instancetree.GetBookmarkRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.GetNewInstanceTreeForBookmarkResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetBookmarkRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetInstanceTreeForBookmarkRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeForBookmarkResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class GetBookmarkInstanceTreeService
    extends AbstractNewInstanceTree<IGetInstanceTreeRequestModel, IGetNewInstanceTreeForBookmarkResponseModel>
    implements IGetBookmarkInstanceTreeService {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  @Override
  public IGetNewInstanceTreeForBookmarkResponseModel executeInternal(IGetInstanceTreeRequestModel model) throws Exception
  {
    IGetNewInstanceTreeForBookmarkResponseModel responseModel = new GetNewInstanceTreeForBookmarkResponseModel();
    IGetInstanceTreeForBookmarkRequestModel requestModel = ((IGetInstanceTreeForBookmarkRequestModel) model);
    IGetBookmarkRequestModel getRequestModel = getInstanceTreeRequestModelForBookmark(requestModel.getBookmarkId());
    getRequestModel.setIsFilterDataRequired(model.getIsFilterDataRequired());
    responseModel.setGetRequestModel(getRequestModel);
    
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = prepareConfigRequestModel(getRequestModel, getModuleEntities(getRequestModel));
    IConfigDetailsForNewInstanceTreeModel configDetails = executeConfigDetailsStrategy(configRequestModel);
    prepareRuntimeRequestModel(getRequestModel, configDetails);
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    Map<String, List<Map<String, Object>>> idVsHighlightsMap = new HashMap<>();

    List<IBaseEntityDTO> baseEntities = getBaseEntityDTOsAsPerSearchCriteria(
        getRequestModel, configDetails, localeCatalogDao, responseModel, idVsHighlightsMap);

    if (baseEntities != null && !baseEntities.isEmpty()) {
      List<IKlassInstanceInformationModel> responseList = responseModel.getChildren();
      populateResponseList(responseList, baseEntities, ((IConfigDetailsForGetNewInstanceTreeModel) configDetails).getLinkedVariantCodes(),
          localeCatalogDao);
    }
        
    prepareResponseModel(getRequestModel, responseModel, baseEntities);
    instanceTreeUtils.addRuleViolationFilter(model.getIsFilterDataRequired(), responseModel.getFilterData());
    
    fillPostConfigDetails(configDetails, responseModel, idVsHighlightsMap);
    return responseModel;
  }

  private IGetBookmarkRequestModel getInstanceTreeRequestModelForBookmark(
      String bookmarkId) throws Exception, RDBMSException
  {
    
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    ICollectionDTO dynamicCollectionDTO = collectionDAO.getCollection(Long.parseLong(bookmarkId), CollectionType.dynamicCollection);
    
    IJSONContent searchCriteria = dynamicCollectionDTO.getSearchCriteria();
    IGetBookmarkRequestModel getRequestModel = ObjectMapperUtil.readValue(searchCriteria.toString(), GetBookmarkRequestModel.class);
    return getRequestModel;
  }
  
  @Override
  protected IGetConfigDetailsForGetNewInstanceTreeRequestModel prepareConfigRequestModel(IGetInstanceTreeRequestModel model,
      List<String> entities)
  {
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = super.prepareConfigRequestModel(model, entities);
    List<String> appliedAttributesIds = new ArrayList<>();
    model.getAttributes().forEach(attribute -> {
      appliedAttributesIds.add(attribute.getId());
    });
    List<String> appliedTagsIds = new ArrayList<>();
    model.getTags().forEach(tag -> {
      appliedTagsIds.add(tag.getId());
    });
    for (ISortModel sort : model.getSortOptions()) {
      appliedAttributesIds.add(sort.getSortField());
    }
    configRequestModel.setAttributeIds(appliedAttributesIds);
    configRequestModel.setTagIds(appliedTagsIds);
    return configRequestModel;
  }
  
  
  private void populateResponseList(List<IKlassInstanceInformationModel> responseList, List<IBaseEntityDTO> listOfDTO,
      List<String> linkedVariantCodes, ILocaleCatalogDAO localeCatlogDAO) throws Exception
  {
    Map<Long, Long> variantIdParentIdMap = getVariantIdParentIdMap(listOfDTO, localeCatlogDAO, linkedVariantCodes);
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO,
          rdbmsComponentUtils);
      responseList.add(klassInstanceInformationModel);
      Long baseEntityIID = baseEntityDTO.getBaseEntityIID();
      fillVariantOfInfo(variantIdParentIdMap, klassInstanceInformationModel, baseEntityIID);
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
  protected IGetNewInstanceTreeResponseModel executeRuntimeStrategy(IGetInstanceTreeRequestModel model,
      IConfigDetailsForNewInstanceTreeModel configDetails, Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws Exception
  {
    return null;
  }
  
  @Override
  protected List<String> getModuleEntities(IGetInstanceTreeRequestModel model) throws Exception
  {
    IModule module = moduleMappingUtil.getModule(model.getModuleId());
    List<String> entities = module.getEntities();
    return entities;
  }
  
}
