package com.cs.core.config.klass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.interactor.model.klass.IBulkDeleteSuccessKlassResponseModel;
import com.cs.core.config.interactor.model.klass.IGetChildKlassIdsInHierarchyModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetChildKlassIdsInHierarchyStrategy;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.exception.delete.InstanceExistsForTypesException;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.utils.BaseEntityUtils;

@SuppressWarnings("unchecked")
public abstract class AbstractDeleteKlassesService<P extends IIdsListParameterModel, R extends IBulkDeleteReturnModel>
    extends AbstractDeleteConfigService<P, R> {
  
  @Autowired
  protected IGetChildKlassIdsInHierarchyStrategy getChildKlassIdsInHierarchyStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getKlassEntityConfigurationStrategy;
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;

  protected abstract String getKlassType();
  
  protected abstract IBulkDeleteKlassResponseModel executeBulkDelete(IIdsListParameterModel model)
      throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {

    IGetEntityConfigurationResponseModel getEntityResponse = getKlassEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(model.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();
    boolean hasChildDependency = getEntityResponse.isHasChildDependency();
    if (!referenceData.keySet()
        .isEmpty() || hasChildDependency) {
      throw new EntityConfigurationDependencyException();
    }

    IGetChildKlassIdsInHierarchyModel chilKlassHierarchyList = getChildKlassIdsInHierarchyStrategy.execute(model);
    Collection<String> klassTypes = chilKlassHierarchyList.getKlassTypes();
    List<Long> klassIds = new ArrayList<Long>();
    chilKlassHierarchyList.getTypeIdVsChildKlassIds().forEach((key, values) -> {
      values.forEach((value) -> {
        klassIds.add(value);
      });
    });

    if (!klassIds.isEmpty()) {
      List<String> classifierCodes= new ArrayList<>();
      List<Long> baseentityIIDs = RDBMSUtils.getBaseentityIIDsForTypes(klassIds);
      List<IClassifierDTO> classifiers = ConfigurationDAO.instance().getClassifiersByIID(klassIds);
      for(IClassifierDTO classifier : classifiers) {
        classifierCodes.add(classifier.getClassifierCode());
      }
      List<BaseType> baseTypesByKlassTypes = BaseEntityUtils.getBaseTypesByKlassTypes(klassTypes);
      ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
      ISearchDTOBuilder searchDTOBuilder = localeCatalogDao.getSearchDTOBuilder(baseTypesByKlassTypes, RootFilter.BOTH, true);
      searchDTOBuilder.addClassFilters(classifierCodes.toArray(new String[] {}));
      ISearchDTO searchDTO = searchDTOBuilder.build();
      ISearchDAO searchDAO = localeCatalogDao.openSearchDAO(searchDTO);
      Long totalCountFromArchive = searchDAO.getCountForEntityUsage();
      if (!baseentityIIDs.isEmpty() || totalCountFromArchive > 0) {
        throw new InstanceExistsForTypesException();
      }
    }
    
    IBulkDeleteKlassResponseModel responseFromStrategy = executeBulkDelete(model);
    IExceptionModel failure = responseFromStrategy.getFailure();
    
    /*typeIdsHavingInstances.forEach(typeId ->
      ExceptionUtil.addFailureDetailsToFailureObject(failure, new InstanceExistsForTypesException(), typeId, null)
    );*/
    
    IBulkDeleteSuccessKlassResponseModel success = (IBulkDeleteSuccessKlassResponseModel) responseFromStrategy
        .getSuccess();
    IBulkDeleteReturnModel modelToReturn = new BulkDeleteReturnModel();
    List<String> deletedKlassIds = success.getIds();
    if (!deletedKlassIds.isEmpty()) {
      manageValueInheritance(success);
    }

    for(String code : success.getIds()){
      ConfigurationDAO.instance().deleteClassifier(code);
    }

    List<String> successIds = new ArrayList<>(deletedKlassIds);
    successIds.retainAll(model.getIds());
    modelToReturn.setSuccess(successIds);
    modelToReturn.setFailure(failure);
    modelToReturn.setAuditLogInfo(responseFromStrategy.getAuditLogInfo());
    deletedKlassIds.retainAll(model.getIds());
    return (R) modelToReturn;
  }
  
  private void manageValueInheritance(IBulkDeleteSuccessKlassResponseModel success) throws Exception
  {
    IContentDiffModelToPrepareDataForBulkPropagation contentDiffModelToPrepareDataForBulkPropagation = new ContentDiffModelToPrepareDataForBulkPropagation();
    contentDiffModelToPrepareDataForBulkPropagation.setDeletedKlassIds(success.getIds());
    contentDiffModelToPrepareDataForBulkPropagation
        .setDeletedRelationshipIds(success.getDeletedRelationsipIds());
    contentDiffModelToPrepareDataForBulkPropagation
        .setDeletedRelationshipIds(success.getDeletedNatureRelationshipIds());
    
    //TODO: BGP
  }
}
