package com.cs.di.config.strategy.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.asset.IAssetKlassSaveModel;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IModifiedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IProjectKlassSaveModel;
import com.cs.core.config.interactor.usecase.asset.ICreateAsset;
import com.cs.core.config.interactor.usecase.asset.IGetAsset;
import com.cs.core.config.interactor.usecase.asset.ISaveAsset;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("assetAPIStrategy")
@SuppressWarnings("unchecked")
public class AssetAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  ICreateAsset                createAsset;
  
  @Autowired
  IGetAsset                   getAsset;
  
  @Autowired
  ISaveAsset                  saveAsset;
  
  private static final String CODE              = "code";
  private static final String ENABLE_AFTER_SAVE = "enableAfterSave";
  private static final String EMBEDDED          = "embedded";
  
  private static final ObjectMapper mapper            = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
      false);
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(code);
    return getAsset.execute(getEntityModel);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    AbstractKlassModel createAsseteModel = mapper.convertValue(inputData, AbstractKlassModel.class);
    // set NumberOfVersionsToMaintain = 0 and NaturType = "" for non nature
    // class
    if (!createAsseteModel.getIsNature()) {
      createAsseteModel.setNatureType("");
      createAsseteModel.setNumberOfVersionsToMaintain(0L);
    }
    // restrict setting IsDefaultChild value to true for embedded
    if (createAsseteModel.getNatureType().equals(EMBEDDED)) {
      createAsseteModel.setIsDefaultChild(Boolean.FALSE);
      createAsseteModel.setIsAbstract(Boolean.FALSE);
    }
    // set only one value at a time to true for IsAbstract and IsDefaultChild
    if (createAsseteModel.getIsAbstract() && createAsseteModel.getIsDefaultChild()) {
      createAsseteModel.setIsAbstract(Boolean.FALSE);
    }
    return createAsset.execute((IAssetModel) createAsseteModel);
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    inputData.put(IAssetKlassSaveModel.ID, inputData.get(CODE));
    inputData.put(IAssetKlassSaveModel.PARENT, (Map<String, Object>) getData.get(IAssetKlassSaveModel.PARENT));
    AbstractKlassSaveModel saveAssetModel = mapper.convertValue(inputData, AbstractKlassSaveModel.class);
    // Handling for modified relationship
    List<IModifiedNatureRelationshipModel> modifiedRelationships = saveAssetModel.getModifiedRelationships();
    if (!CollectionUtils.isEmpty(modifiedRelationships)) {
      prepareModifiedRelationship(modifiedRelationships, saveAssetModel, getData, inputData);
    }
    
    saveAssetModel.setIsNature((Boolean) getData.get(IAssetKlassSaveModel.IS_NATURE));
    saveAssetModel.setNatureType((String) getData.get(IAssetKlassSaveModel.NATURE_TYPE));
    saveAssetModel.setIsStandard((Boolean) getData.get(IAssetKlassSaveModel.IS_STANDARD));
    saveAssetModel.setType((String) getData.get(IAssetKlassSaveModel.TYPE));
    saveAssetModel.setClassifierIID((Long) getData.get(IAssetKlassSaveModel.CLASSIFIER_IID));
    saveAssetModel.setIsAllowedAtTopLevel((Boolean) getData.get(IAssetKlassSaveModel.IS_ALLOWED_AT_TOP_LEVEL));
    saveAssetModel.setIsDefaultFolder((Boolean) getData.get(IAssetKlassSaveModel.IS_DEFAULT_FOLDER));
    if (DiValidationUtil.isBlank(saveAssetModel.getLabel())) {
      saveAssetModel.setLabel((String) getData.get(IAssetKlassSaveModel.LABEL));
    }
    // restrict setting IsDefaultChild value to true for embedded
    if (saveAssetModel.getNatureType().equals(EMBEDDED)) {
      saveAssetModel.setIsDefaultChild(Boolean.FALSE);
      saveAssetModel.setIsAbstract(Boolean.FALSE);
    }
    // restrict setting IsAbstract& IsDefaultChild to true
    // only one value can be true
    if (saveAssetModel.getIsAbstract() && saveAssetModel.getIsDefaultChild()) {
      saveAssetModel.setIsAbstract(false);
    }
    // set NumberOfVersionsToMaintain = 0 for non nature class
    if (!saveAssetModel.getIsNature()) {
      saveAssetModel.setNumberOfVersionsToMaintain(0L);
    }
    if (saveAssetModel.getNumberOfVersionsToMaintain() == null) {
      saveAssetModel.setNumberOfVersionsToMaintain((Long) getData.get(IAssetKlassSaveModel.NUMBER_OF_VERSIONS_TO_MAINTAIN));
    }
    
    return saveAsset.execute(saveAssetModel);
  }
  
  /**
   * @param modifiedRelationships
   * @param saveAssetModel
   * @param getData
   * @param inputData
   */
  private void prepareModifiedRelationship(List<IModifiedNatureRelationshipModel> modifiedRelationships,
      AbstractKlassSaveModel saveAssetModel, Map<String, Object> getData, Map<String, Object> inputData)
  {
    List<Map<String, Object>> relationshipToSave = (List<Map<String, Object>>) inputData.get(IProjectKlassSaveModel.MODIFIED_RELATIONSHIPS);
    IModifiedNatureRelationshipModel modifiedRelationship = modifiedRelationships.get(0);
    
    if (relationshipToSave.get(0).get(ENABLE_AFTER_SAVE) == null) {
      modifiedRelationship.setEnableAfterSave(
          (Boolean) getDataFromRelationship(getData, modifiedRelationship, IModifiedNatureRelationshipModel.ENABLE_AFTER_SAVE));
    }
    if (relationshipToSave.get(0).get(IModifiedNatureRelationshipModel.TAXONOMY_INHERITANCE_SETTING) == null) {
      modifiedRelationship.setTaxonomyInheritanceSetting(
          (String) getDataFromRelationship(getData, modifiedRelationship, IModifiedNatureRelationshipModel.TAXONOMY_INHERITANCE_SETTING));
    }
    Object relationshipLabel = getDataFromRelationship(getData, modifiedRelationship, IModifiedNatureRelationshipModel.LABEL);
    modifiedRelationship.setLabel(relationshipLabel == null ? "Nature Relationship" : (String) relationshipLabel);
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
