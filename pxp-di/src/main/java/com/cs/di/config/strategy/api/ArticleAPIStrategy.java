package com.cs.di.config.strategy.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IModifiedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IProjectKlassSaveModel;
import com.cs.core.config.interactor.usecase.klass.ICreateKlass;
import com.cs.core.config.interactor.usecase.klass.IGetKlassWithoutKP;
import com.cs.core.config.interactor.usecase.klass.ISaveKlass;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("articleAPIStrategy")
@SuppressWarnings("unchecked")
public class ArticleAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  ICreateKlass                 createKlass;
  
  @Autowired
  protected IGetKlassWithoutKP getKlassWithoutKP;
  
  @Autowired
  ISaveKlass                   saveKlass;
  
  private static final String  CODE                         = "code";
  private static final String  ENTITY                       = "entity";
  private static final String  ENABLE_AFTER_SAVE            = "enableAfterSave";
  private static final String  TAXONOMY_INHERITANCE_SETTING = "off";
  private static final String  EMBEDDED                     = "embedded";
  
  private static final ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(code);
    return getKlassWithoutKP.execute(getEntityModel);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    AbstractKlassModel createArticleModel = mapper.convertValue(inputData, AbstractKlassModel.class);
    // set NumberOfVersionsToMaintain = 0 and NaturType = "" for non nature
    // class
    if (!createArticleModel.getIsNature()) {
      createArticleModel.setNatureType("");
      createArticleModel.setNumberOfVersionsToMaintain(0L);
    }
    // restrict setting IsDefaultChild value to true for embedded
    if (createArticleModel.getNatureType().equals(EMBEDDED)) {
      createArticleModel.setIsDefaultChild(Boolean.FALSE);
      createArticleModel.setIsAbstract(Boolean.FALSE);

    }
    // set only one value at a time to true for IsAbstract and IsDefaultChild
    if (createArticleModel.getIsAbstract() && createArticleModel.getIsDefaultChild()) {
      createArticleModel.setIsAbstract(Boolean.FALSE);
    }
    return createKlass.execute(createArticleModel);
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    inputData.put(IProjectKlassSaveModel.ID, inputData.get(CODE));
    Map<String, Object> entity = (Map<String, Object>) getData.get(ENTITY);
    inputData.put(IProjectKlassSaveModel.PARENT, (Map<String, Object>) entity.get(IProjectKlassSaveModel.PARENT));
    AbstractKlassSaveModel saveArticleModel = mapper.convertValue(inputData, AbstractKlassSaveModel.class);
    List<IModifiedNatureRelationshipModel> modifiedRelationships = saveArticleModel.getModifiedRelationships();
    if (!CollectionUtils.isEmpty(modifiedRelationships)) {
      prepareModifiedRelationship(modifiedRelationships, saveArticleModel, entity, inputData);
    }
    
    saveArticleModel.setIsNature((Boolean) entity.get(IProjectKlassSaveModel.IS_NATURE));
    saveArticleModel.setNatureType((String) entity.get(IProjectKlassSaveModel.NATURE_TYPE));
    saveArticleModel.setIsStandard((Boolean) entity.get(IProjectKlassSaveModel.IS_STANDARD));
    saveArticleModel.setType((String) entity.get(IProjectKlassSaveModel.TYPE));
    saveArticleModel.setClassifierIID((Long) entity.get(IProjectKlassSaveModel.CLASSIFIER_IID));
    saveArticleModel.setIsAllowedAtTopLevel((Boolean) entity.get(IProjectKlassSaveModel.IS_ALLOWED_AT_TOP_LEVEL));
    saveArticleModel.setIsDefaultFolder((Boolean) entity.get(IProjectKlassSaveModel.IS_DEFAULT_FOLDER));
    if (DiValidationUtil.isBlank(saveArticleModel.getLabel())) {
      saveArticleModel.setLabel((String) entity.get(IProjectKlassSaveModel.LABEL));
    }
    // restrict setting IsDefaultChild value to true for embedded
    if (saveArticleModel.getNatureType().equals(EMBEDDED)) {
      saveArticleModel.setIsDefaultChild(Boolean.FALSE);
      saveArticleModel.setIsAbstract(Boolean.FALSE);
    }
    // restrict setting IsAbstract& IsDefaultChild to true.
    // only one value can be true
    if (saveArticleModel.getIsAbstract() && saveArticleModel.getIsDefaultChild()) {
      saveArticleModel.setIsAbstract(Boolean.FALSE);
    }
    // set NumberOfVersionsToMaintain = 0 for non nature textAssets
    if (!saveArticleModel.getIsNature()) {
      saveArticleModel.setNumberOfVersionsToMaintain(0L);
    }
    if (saveArticleModel.getNumberOfVersionsToMaintain() == null) {
      saveArticleModel.setNumberOfVersionsToMaintain((Long) entity.get(IProjectKlassSaveModel.NUMBER_OF_VERSIONS_TO_MAINTAIN));
    }
    
    return saveKlass.execute(saveArticleModel);
  }
  
  /**
   * @param modifiedRelationships
   * @param saveArticleModel
   * @param entity
   * @param inputData
   */
  private void prepareModifiedRelationship(List<IModifiedNatureRelationshipModel> modifiedRelationships,
      AbstractKlassSaveModel saveArticleModel, Map<String, Object> entity, Map<String, Object> inputData)
  {
    List<Map<String, Object>> relationshipToSave = (List<Map<String, Object>>) inputData.get(IProjectKlassSaveModel.MODIFIED_RELATIONSHIPS);
    IModifiedNatureRelationshipModel modifiedRelationship = modifiedRelationships.get(0);
    
    if (relationshipToSave.get(0).get(ENABLE_AFTER_SAVE) == null) {
      modifiedRelationship.setEnableAfterSave(
          (Boolean) getDataFromRelationship(entity, modifiedRelationship, IModifiedNatureRelationshipModel.ENABLE_AFTER_SAVE));
    }
    Object relationshipLabel = getDataFromRelationship(entity, modifiedRelationship, IModifiedNatureRelationshipModel.LABEL);
    modifiedRelationship.setLabel(relationshipLabel == null ? "Nature Relationship" : (String) relationshipLabel);
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
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
