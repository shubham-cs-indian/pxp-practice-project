package com.cs.core.runtime.instancetree;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.GetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetLinkedInstanceQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;

@Service
public class GetLinkedInstanceQuicklistService
    extends AbstractNewInstanceTree<IGetLinkedInstanceQuicklistRequestModel, IGetNewInstanceTreeResponseModel>
    implements IGetLinkedInstanceQuicklistService {
  
  public ServiceType getServiceType()
  {
    return ServiceType.GET;
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
  protected IGetNewInstanceTreeResponseModel executeRuntimeStrategy(
      IGetLinkedInstanceQuicklistRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails,
      Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws Exception
  {
    IGetNewInstanceTreeResponseModel responseModel = new GetNewInstanceTreeResponseModel();
    
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    List<IBaseEntityDTO> baseEntities = getBaseEntityDTOsAsPerSearchCriteria(
        dataModel, configDetails, localeCatalogDao, responseModel, idVsHighlightsMap);
    
    List<IKlassInstanceInformationModel> children = responseModel.getChildren();
    if (baseEntities != null && !baseEntities.isEmpty()) {
      populateResponseList(children, baseEntities, configDetails, localeCatalogDao);
    }
    prepareResponseModel(dataModel, responseModel, baseEntities);
    
    return responseModel;
  }
  
  protected void populateResponseList(List<IKlassInstanceInformationModel> responseList, List<IBaseEntityDTO> listOfDTO,
      IConfigDetailsForNewInstanceTreeModel configDetails, ILocaleCatalogDAO localeCatalogDao) throws Exception
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    Set<IPropertyDTO> properties = getAllUtils.getXrayProperties(referencedAttributes, referencedTags);
    List<String> linkedVariantCodes = ((IConfigDetailsForGetNewInstanceTreeModel) configDetails).getLinkedVariantCodes();
    
    Map<Long, Long> variantIdParentIdMap = getVariantIdParentIdMap(listOfDTO, localeCatalogDao, linkedVariantCodes);
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
    if(dataModel.getIdsToExclude() == null || dataModel.getIdsToExclude().isEmpty())
    {
      return;
    }
    
    IGetLinkedInstanceQuicklistRequestModel linkedInstanceDataModel = (IGetLinkedInstanceQuicklistRequestModel)dataModel;
    String instanceId = linkedInstanceDataModel.getInstanceId();
    List<String> idsToInclude = linkedInstanceDataModel.getIdsToInclude();
    
    Set<Long> idsToExclude = new HashSet<>();
    
    if(instanceId != null)
    {
      String moduleId = InstanceTreeUtils.getModuleIdByEntityId(dataModel.getModuleEntities().get(0));
      BaseType baseType = InstanceTreeUtils.getBaseTypeByModultId(moduleId);
      
      ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
      Set<Long> linkedInstanceIIDs = localeCatalogDAO.getLinkedInstancesByBaseType(Long.valueOf(instanceId), baseType);
      idsToExclude.addAll(linkedInstanceIIDs);
    }
    
    idsToExclude.addAll(dataModel.getIdsToExclude().stream().map(Long::valueOf).collect(Collectors.toSet()));
      
    if(idsToInclude != null)
    {
     Set<Long> ids = idsToInclude.stream().map(Long::valueOf).collect(Collectors.toSet());
      idsToExclude.removeAll(ids);
    }
    
    searchBuilder.setIdsToExclude(idsToExclude);
  }
  
  @Override
  protected List<String> getModuleEntities(IGetLinkedInstanceQuicklistRequestModel model) throws Exception
  {
    return model.getModuleEntities();
  }
  
}
