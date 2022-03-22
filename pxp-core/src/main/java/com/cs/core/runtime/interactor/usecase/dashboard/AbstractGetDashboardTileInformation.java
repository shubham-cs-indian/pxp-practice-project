package com.cs.core.runtime.interactor.usecase.dashboard;

import java.util.*;
import java.util.stream.Collectors;

import com.cs.core.rdbms.entity.dto.SortDTO;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.dashboard.DashboardInformationResponseModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Component
public abstract class AbstractGetDashboardTileInformation<P extends IDashboardInformationRequestModel, R extends IDashboardInformationResponseModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected ISessionContext               context;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy postConfigDetailsForGetInstanceTreeStrategy;
  
  @Autowired
  protected ModuleMappingUtil             moduleMappingUtil;
  
  @Autowired
  protected RDBMSComponentUtils           rdbmsComponentUtils;
  
  @Autowired
  protected SearchAssembler               searchAssembler;
  
  @Autowired
  protected GetAllUtils                   getAllUtils;
  
  @Autowired
  protected TransactionThreadData         transactionThreadData;

  protected abstract IConfigDetailsForInstanceTreeGetModel getConfigDetails(IIdParameterModel model)
      throws Exception;
  protected abstract String generateSearchExpression(P dataModel) throws CSInitializationException;

  @SuppressWarnings({ "unchecked" })
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {
    String loginUserId = context.getUserId();
    String moduleId = dataModel.getModuleId();
    IIdParameterModel model = new IdParameterModel();
    model.setCurrentUserId(loginUserId);
    
    IConfigDetailsForInstanceTreeGetModel configDetails = getConfigDetails(model);
    
    IModule module = getModule(configDetails.getAllowedEntities(), moduleId);
    
    dataModel.setCurrentUserId(loginUserId);
    dataModel.setKlassIdsHavingRP(configDetails.getKlassIdsHavingRP());
    dataModel.setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    dataModel.setModuleEntities(module.getEntities());
    
    IDashboardInformationResponseModel responseModel = new DashboardInformationResponseModel();
    
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    List<IBaseEntityDTO> baseEntityDTOs = getAssetBaseEntityDTOs(dataModel, responseModel,
        localeCatalogDao);
    
    if (baseEntityDTOs != null && !baseEntityDTOs.isEmpty()) {
      List<IKlassInstanceInformationModel> responseList = responseModel.getChildren();
      populateResponseList(responseList, baseEntityDTOs, moduleId, dataModel, configDetails, localeCatalogDao);
    }
    
    Set<String> typeIdsSet = new HashSet<>();
    Set<Long> klassInstanceIds = new HashSet<>();
    List<IKlassInstanceInformationModel> children = responseModel.getChildren();
    for (IKlassInstanceInformationModel child : children) {
      klassInstanceIds.add(Long.parseLong(child.getId()));
      for (String type : child.getTypes()) {
        typeIdsSet.add(type);
      }
    }
    
    IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
    requestModel.getKlassIds()
        .addAll(typeIdsSet);
    IGetPostConfigDetailsResponseModel configResponseModel = postConfigDetailsForGetInstanceTreeStrategy
        .execute(requestModel);
    long starTime4 = System.currentTimeMillis();
    RDBMSLogger.instance()
        .debug("NA|OrientDB|" + this.getClass()
            .getSimpleName()
            + "|executeInternal|postConfigDetailsForGetInstanceTreeStrategy| %d ms",
            System.currentTimeMillis() - starTime4);
    responseModel.setReferencedKlasses(configResponseModel.getReferencedKlasses());
    
    return (R) responseModel;
  }
  
  protected List<IBaseEntityDTO> getAssetBaseEntityDTOs(P dataModel,
      IDashboardInformationResponseModel responseModel, ILocaleCatalogDAO localeCatalogDao)
      throws CSInitializationException, RDBMSException
  {
    String searchExpression = generateSearchExpression(dataModel);
    List<ISortModel> sortOptionsModel = dataModel.getSortOptions();
    List<ISortDTO> sortOptions = sortOptionsModel.stream().map(x -> new SortDTO(x.getSortField(),
        ISortDTO.SortOrder.valueOf(x.getSortOrder().toLowerCase()),x.getIsNumeric())).collect(Collectors.toList());
    fillAttributeAccordingToSortOptions(sortOptions);
    IRDBMSOrderedCursor<IBaseEntityDTO> cursor = getAllUtils.getCursor(localeCatalogDao,
        sortOptions, searchExpression);
    responseModel.setTotalContents(cursor.getCount());
    List<IBaseEntityDTO> baseEntityDTOs = cursor.getNext(dataModel.getFrom(), dataModel.getSize());
    return baseEntityDTOs;
  }
  
  private void populateResponseList(List<IKlassInstanceInformationModel> responseList,
	      List<IBaseEntityDTO> listOfDTO, String moduleId, IDashboardInformationRequestModel dataModel,
	      IConfigDetailsForInstanceTreeGetModel configDetails, ILocaleCatalogDAO localeCatalogDao) throws Exception
	  {
      List<String> linkedVariantRelationshipCodes = configDetails.getLinkedVariantCodes();
      Map<Long, Long> variantIdParentIdMap = linkedVariantRelationshipCodes.isEmpty() ? new HashMap<>()
          :localeCatalogDao.getLinkedVariantIds(listOfDTO, linkedVariantRelationshipCodes);
	    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
	      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO, rdbmsComponentUtils);
	      responseList.add(klassInstanceInformationModel);
	      Long baseEntityIID = baseEntityDTO.getBaseEntityIID();
	      if(variantIdParentIdMap.containsKey(baseEntityIID)) {
	        Long parentId = variantIdParentIdMap.get(baseEntityIID);
	        klassInstanceInformationModel.setVariantOf(parentId.toString());
	        String baseEntityName = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO).getBaseEntityDTO().getBaseEntityName();
	        klassInstanceInformationModel.setVariantOfLabel(baseEntityName);
	      }
	    }
	  }
  
  protected IModule getModule(Set<String> allowedEntities, String moduleId) throws Exception
  {
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    
    for (IModule iModule : modules) {
      if (iModule.getId()
          .equals(moduleId)) {
        iModule.getEntities()
            .retainAll(allowedEntities);
        return iModule;
      }
    }
    return null;
  }
  
  private void fillAttributeAccordingToSortOptions(List<ISortDTO> sortOptions)
  {
    Iterator<ISortDTO> iterator = sortOptions.iterator();
    while(iterator.hasNext()) {
      ISortDTO next = iterator.next();
      String sortField = next.getSortField();
      if(sortField.equals("expiredassets") || sortField.equals("endTime")) {
        sortOptions.remove(next);
        sortOptions.add(new SortDTO("scheduleendattribute", next.getSortOrder(), next.getIsNumeric()));
      }
    }
  }
}
