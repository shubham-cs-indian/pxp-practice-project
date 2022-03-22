package com.cs.core.runtime.klassinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplatePermissionModel;
import com.cs.core.runtime.interactor.model.configdetails.ITemplatePermissionForGetVariantInstancesModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.variants.*;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetVariantInstancesInTableViewStrategy;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractGetVariantInstancesInTableViewService<P extends IGetVariantInstanceInTableViewRequestModel, R extends IGetVariantInstancesInTableViewModel>
    extends AbstractRuntimeService<P, R> {
  
  protected abstract String getBaseType();
  
  protected abstract String getModuleId();
  
  @Autowired
  protected IGetConfigDetailsForGetVariantInstancesInTableViewStrategy getconfigDetailsForGetVariantInstancesInTableViewStrategy;
  
  @Autowired
  protected ISessionContext                                            context;
  
  @Autowired
  protected PermissionUtils                                            permissionUtils;
  
  @Autowired
  protected RDBMSComponentUtils                                        rdbmsComponentUtils;
  
  @Autowired
  protected VariantInstanceUtils                                       variantInstanceUtils;
  
  @Autowired
  protected GetAllUtils                                                getAllUtils;
  
  @Override
  protected R executeInternal(P requestModel) throws Exception
  {
    IConfigDetailsForGetVariantInstancesInTableViewRequestModel configRequestModel = new ConfigDetailsForGetVariantInstancesInTableViewRequestModel();

    String parentId = requestModel.getParentId();
    String klassInstanceId = requestModel.getKlassInstanceId();
    IKlassInstanceTypeModel klassInstanceTypeModel = getKlassInstanceType(parentId);
    String contextId = requestModel.getContextId();
    
    Map<Long, Map<String, List<String>>> childrensVSotherClassifiers = getChildrenVSotherClassifiers(parentId, contextId);
    
    configRequestModel.setInstanceIdVsOtherClassifiers(childrensVSotherClassifiers);
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setContextId(contextId);
    List<String> columnIds = requestModel.getColumnIds();
    configRequestModel.setPropertyIds(columnIds);
    configRequestModel.setParentKlassIds(new ArrayList<>(klassInstanceTypeModel.getTypes()));
    configRequestModel.setParentTaxonomyIds(klassInstanceTypeModel.getTaxonomyIds());
    configRequestModel.setBaseType(getBaseType());
    configRequestModel.setModuleId(getModuleId());
    
    IConfigDetailsForGetVariantInstancesInTableViewModel configDetails = getconfigDetailsForGetVariantInstancesInTableViewStrategy
        .execute(configRequestModel);
    
    IBaseKlassTemplatePermissionModel permission = configDetails.getReferencedPermissions();
    
    // remove all column having no permission (multi-user scenario)
    if (columnIds != null) {
      Set<String> visiblePropertyIds = permission.getVisiblePropertyIds();
      List<String> columnIdsToKeep = new ArrayList<>(CommonConstants.KLASS_TYPES);
      columnIdsToKeep.add(IInstanceTimeRange.FROM);
      columnIdsToKeep.add(IInstanceTimeRange.TO);
      columnIdsToKeep.addAll(visiblePropertyIds);
      columnIds.retainAll(columnIdsToKeep);
    }
    
    IGetVariantInstancesInTableViewRequestStrategyModel elasticRequestModel = new GetVariantInstancesInTableViewStrategyModel();
    ITemplatePermissionForGetVariantInstancesModel referencedPermissions = (ITemplatePermissionForGetVariantInstancesModel) configDetails
        .getReferencedPermissions();
    
    elasticRequestModel.setAttributes(requestModel.getAttributes());
    elasticRequestModel.setKlassInstanceId(klassInstanceId);
    elasticRequestModel.setContextId(requestModel.getContextId());
    if(requestModel.getFrom() == null) {
      elasticRequestModel.setFrom(0);
    } else {
      elasticRequestModel.setFrom(requestModel.getFrom());
    }
    elasticRequestModel.setParentId(parentId);
    Map<String, IReferencedSectionElementModel> referencedElements = getReferencedElements(configDetails.getInstanceIdVsReferencedElements());
    configDetails.setReferencedElements(referencedElements);
    elasticRequestModel.setReferencedElements(referencedElements);
    elasticRequestModel.setReferencedTags(configDetails.getReferencedTags());
    elasticRequestModel.setReferencedVariantContexts(configDetails.getReferencedVariantContexts()
        .getEmbeddedVariantContexts());
    elasticRequestModel.setSize(requestModel.getSize());
    elasticRequestModel.setSortOptions(requestModel.getSortOptions());
    elasticRequestModel.setTags(requestModel.getTags());
    elasticRequestModel.setColumnIds(columnIds);
    elasticRequestModel.setCurrentUserId(context.getUserId());
    elasticRequestModel.setFilterInfo(configDetails.getFilterInfo());
    elasticRequestModel.setTimeRange(requestModel.getTimeRange());
    elasticRequestModel.setEntities(new ArrayList<>(referencedPermissions.getEntitiesHavingRP()));
    elasticRequestModel.setAllSearch(requestModel.getAllSearch());
    elasticRequestModel.setKlassIdsHavingRP(referencedPermissions.getKlassIdsHavingRP());
    elasticRequestModel
        .setContextKlassIdsHavingRP(referencedPermissions.getContextKlassIdsHavingRP());
    elasticRequestModel
        .setReferencedPropertyCollections(configDetails.getReferencedPropertyCollections());
    elasticRequestModel.setTaxonomyIdsHavingRP(referencedPermissions.getTaxonomyIdsHavingRP());
    
    IGetVariantInstancesInTableViewModel returnModel = variantInstanceUtils
        .executeGetVariantInstances(elasticRequestModel, configDetails);
    returnModel.setConfigDetails(configDetails);
    permissionUtils.manageVariantInstancePermissions(configDetails);
    return (R) returnModel;
  }
  
  private Map<String, IReferencedSectionElementModel> getReferencedElements(
      Map<String, Map<String, IReferencedSectionElementModel>> idVsReferencedElements)
  {
    Map<String, IReferencedSectionElementModel> referencedElements = new HashMap<>();
    
    idVsReferencedElements.values().forEach(map -> referencedElements.putAll(map));
    return referencedElements;
  }

  private Map<Long, Map<String, List<String>>> getChildrenVSotherClassifiers(String parentId, String contextId) throws NumberFormatException, RDBMSException
  {
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    List<Long> childrenIIDs = localeCatalogDAO.loadContextualEntityIIDs(Collections.singletonList(contextId), Long.valueOf(parentId));
    
    if(!childrenIIDs.isEmpty())
      return localeCatalogDAO.getEntityVSotherClassifierCodes(childrenIIDs.toArray(new Long[0]));
    
    return new HashMap<>();
  }

  private IKlassInstanceTypeModel getKlassInstanceType(String parentId)
      throws Exception
  {
    IKlassInstanceTypeModel typeModel = new KlassInstanceTypeModel();
    //remove get baseEntityIId
    List<IBaseEntityDTO> baseEntitiesByIIDs = rdbmsComponentUtils.getLocaleCatlogDAO().getBaseEntitiesByIIDs(List.of(parentId));

    IBaseEntityDAO baseEntityDAOByIID = rdbmsComponentUtils.getBaseEntityDAO(baseEntitiesByIIDs.get(0));
    IBaseEntityDTO baseEntityDTO = baseEntityDAOByIID.getBaseEntityDTO();
    typeModel.getParentKlassIds()
        .add(baseEntityDTO.getNatureClassifier()
            .getClassifierCode());
    
    for (IClassifierDTO classifierDTO : baseEntityDTO.getOtherClassifiers()) {
      if (classifierDTO.getClassifierType()
          .equals(ClassifierType.CLASS)) {
        typeModel.getParentKlassIds()
            .add(classifierDTO.getClassifierCode());
      }
      else {
        typeModel.getParentTaxonomyIds()
            .add(classifierDTO.getClassifierCode());
      }
    }
    return typeModel;
  }
}

