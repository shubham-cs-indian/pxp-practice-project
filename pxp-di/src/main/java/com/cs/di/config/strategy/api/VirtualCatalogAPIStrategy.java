package com.cs.di.config.strategy.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IModifiedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IProjectKlassSaveModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("virtualCatalogAPIStrategy")
@SuppressWarnings("unchecked")
public class VirtualCatalogAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  //TODO: PXPFDEV-21451: Deprecate Virtual Catalog  
  /*@Autowired
  protected IGetVirtualCatalogWithoutKP getVirtualCatalogWithoutKP;
  
  @Autowired
  protected ICreateVirtualCatalog       createVirtualCatalog;
  
  @Autowired
  protected ISaveVirtualCatalog         saveVirtualCatalog;*/
  
  private static final String           EMBEDDED                     = "embedded";
  private static final String           CODE                         = "code";
  private static final String           ENTITY                       = "entity";
  private static final String           ENABLE_AFTER_SAVE            = "enableAfterSave";
  private static final String           TAXONOMY_INHERITANCE_SETTING = "off";
  private static final ObjectMapper     objectMapper                 = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    // TODO: PXPFDEV-21451: Deprecate Virtual Catalog  
    return null;//getVirtualCatalogWithoutKP.execute(new IdParameterModel(code));  
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    AbstractKlassModel createVirtualCatalogModel = objectMapper.readValue(configModel.getData(), AbstractKlassModel.class);
    if (createVirtualCatalogModel.getIsNature().equals(false)) {
      throw new Exception("Can not create non nature Virtual Catalog ");
    }
    // set only one value at a time to true for IsAbstract and IsDefaultChild
    if (createVirtualCatalogModel.getIsAbstract() && createVirtualCatalogModel.getIsDefaultChild()) {
      createVirtualCatalogModel.setIsAbstract(false);
      
    }
    createVirtualCatalogModel.setNumberOfVersionsToMaintain(10L);
    // restrict setting IsDefaultChild value to true for embedded text assets
    if (createVirtualCatalogModel.getNatureType().equals(EMBEDDED)) {
      createVirtualCatalogModel.setIsDefaultChild(false);
      createVirtualCatalogModel.setIsAbstract(false);
    }
    if (!(createVirtualCatalogModel.getNatureType().equals(EMBEDDED))) {
      IKlassNatureRelationship relationshipList = createVirtualCatalogModel.getRelationships().get(0);
      relationshipList.setMaxNoOfItems(10);
      List<IKlassNatureRelationship> NewRelationsiplist = new ArrayList<>();
      NewRelationsiplist.add(relationshipList);
      createVirtualCatalogModel.setRelationships(NewRelationsiplist);
    }
    // TODO: PXPFDEV-21451: Deprecate Virtual Catalog  
    return null;//createVirtualCatalog.execute((IVirtualCatalogModel) createVirtualCatalogModel);  
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    inputData.put(IProjectKlassSaveModel.ID, inputData.get(CODE));
    Map<String, Object> entity = (Map<String, Object>) getData.get(ENTITY);
    inputData.put(IProjectKlassSaveModel.PARENT, (Map<String, Object>) entity.get(IProjectKlassSaveModel.PARENT));
    AbstractKlassSaveModel model = objectMapper.convertValue(inputData, AbstractKlassSaveModel.class);
    List<IModifiedNatureRelationshipModel> modifiedRelationships = model.getModifiedRelationships();
    if (!CollectionUtils.isEmpty(modifiedRelationships)) {
      prepareModifiedRelationship(modifiedRelationships, model, entity, inputData);
    }
    // TODO: PXPFDEV-21451: Deprecate Virtual Catalog  
    /*model.setType((String) entity.get(IVirtualCatalogSaveModel.TYPE));
    model.setClassifierIID((Long) entity.get(IVirtualCatalogSaveModel.CLASSIFIER_IID));
    model.setIsAllowedAtTopLevel((Boolean) entity.get(IVirtualCatalogSaveModel.IS_ALLOWED_AT_TOP_LEVEL));
    model.setIsDefaultFolder((Boolean) entity.get(IVirtualCatalogSaveModel.IS_DEFAULT_FOLDER));
    model.setIsNature((Boolean) entity.get(IVirtualCatalogSaveModel.IS_NATURE));
    model.setIsStandard((Boolean) entity.get(IVirtualCatalogSaveModel.IS_STANDARD));
    model.setNatureType((String) entity.get(IVirtualCatalogSaveModel.NATURE_TYPE));
    model.setContextID((String) entity.get(IVirtualCatalogSaveModel.CONTEXT_ID));
    model.setContextIID((Long) entity.get(IVirtualCatalogSaveModel.CONTEXT_INTERNAL_ID));*/
    if (DiValidationUtil.isBlank(model.getLabel())) {
      model.setLabel((String) entity.get(IProjectKlassSaveModel.LABEL));
    }
    // restrict setting IsDefaultChild value to true for embedded text assets
    if (model.getNatureType().equals(EMBEDDED)) {
      model.setIsDefaultChild(false);
      model.setIsAbstract(false);
    }
    // restrict setting IsAbstract& IsDefaultChild to true.
    // only one value can be true
    if (model.getIsAbstract() && model.getIsDefaultChild()) {
      model.setIsAbstract(false);
    }
    
    if (model.getNumberOfVersionsToMaintain() == null) {
      model.setNumberOfVersionsToMaintain((Long) entity.get(IProjectKlassSaveModel.NUMBER_OF_VERSIONS_TO_MAINTAIN));
    }
    // TODO: PXPFDEV-21451: Deprecate Virtual Catalog  
    return null; //saveVirtualCatalog.execute((IVirtualCatalogSaveModel) model);
  }
  
  /**
   * @param modifiedRelationships
   * @param saveArticleModel
   * @param entity
   * @param inputData
   */
  private void prepareModifiedRelationship(List<IModifiedNatureRelationshipModel> modifiedRelationships,
      AbstractKlassSaveModel saveVirtualCatalogModel, Map<String, Object> entity, Map<String, Object> inputData)
  {
    List<Map<String, Object>> relationshipToSave = (List<Map<String, Object>>) inputData.get(IProjectKlassSaveModel.MODIFIED_RELATIONSHIPS);
    IModifiedNatureRelationshipModel modifiedRelationship = modifiedRelationships.get(0);
    
    if (relationshipToSave.get(0).get(ENABLE_AFTER_SAVE) == null) {
      modifiedRelationship.setEnableAfterSave(
          (Boolean) getDataFromRelationship(entity, modifiedRelationship, IModifiedNatureRelationshipModel.ENABLE_AFTER_SAVE));
    }
    Object relationshipLabel = getDataFromRelationship(entity, modifiedRelationship, IModifiedNatureRelationshipModel.LABEL);
    modifiedRelationship.setTaxonomyInheritanceSetting(TAXONOMY_INHERITANCE_SETTING);
  }
  
  /**
   * @param getData
   * @param modifiedRelationship
   * @param key
   * @return
   */
  private Object getDataFromRelationship(Map<String, Object> getData, IModifiedNatureRelationshipModel modifiedRelationship, String key)
  {
    List<Map<String, Object>> getRelationships = (List<Map<String, Object>>) getData.get(IProjectKlassSaveModel.RELATIONSHIPS);
    for (Map<String, Object> getRelationship : getRelationships) {
      if (getRelationship.get(CODE).equals(modifiedRelationship.getId())) {
        return getRelationship.get(key);
      }
    }
    return null;
  }
  
}
