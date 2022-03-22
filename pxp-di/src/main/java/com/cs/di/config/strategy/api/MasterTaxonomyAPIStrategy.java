package com.cs.di.config.strategy.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.Section;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.attributiontaxonomy.AddedTagModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.AddedTaxonomyLevelModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.CreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IAddedTagModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IAddedTaxonomyLevelModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.SaveMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.ContextKlassModel;
import com.cs.core.config.interactor.model.klass.IContextKlassModel;
import com.cs.core.config.interactor.model.klass.IModifiedContextKlassModel;
import com.cs.core.config.interactor.model.taxonomy.GetTaxonomyRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.IBulkCreateMasterTaxonomy;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.ICreateMasterTaxonomy;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.IGetMasterTaxonomy;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.ISaveMasterTaxonomy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.context.ModifiedContextKlassModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MasterTaxonomyAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  IGetMasterTaxonomy                  getMasterTaxonomy;
  
  @Autowired
  protected ICreateMasterTaxonomy     createMasterTaxonomy;
  
  @Autowired
  protected IBulkCreateMasterTaxonomy bulkCreateMasterTaxonomy;
  
  @Autowired
  protected ISaveMasterTaxonomy       saveMasterTaxonomy;
  
  public static final String          COUPLING_TYPE            = "couplingType";
  public static final String          TAG_LEVELS               = "tagLevels";
  public static final String          PARENT_TAXONOMY_ID       = "parentTaxonomyId";
  public static final String          ADDED_TASKS              = "addedTasks";
  public static final String          DELETED_TASKS            = "deletedTasks";
  public static final String          CONTEXT_KLASS_ID         = "contextKlassId";
  public static final String          ADDED_CONTEXT_KLASSES    = "addedContextKlasses";
  public static final String          DELETED_CONTEXT_KLASSES  = "deletedContextKlasses";
  public static final String          MODIFIED_CONTEXT_KLASSES = "modifiedContextKlasses";
  public static final String          ADDED_ATTRIBUTES         = "addedAttributes";
  public static final String          ADDED_TAGS               = "addedTags";
  public static final String          MODIFIED_ATTRIBUTES      = "modifiedAttributes";
  public static final String          MODIFIED_TAGS            = "modifiedTags";
  public static final String          DELETED_ATTRIBUTES       = "deletedAttributes";
  public static final String          DELETED_TAGS             = "deletedTags";
  public static final String          ADDED_SECTIONS           = "addedSections";
  public static final String          DELETED_SECTIONS         = "deletedSections";
  public static final String          MODIFIED_SECTIONS        = "modifiedSections";
  public static final String          ADDED_LEVEL              = "addedLevel";
  public static final String          DELETED_LEVEL            = "deletedLevel";
  public static final String          IS_NEWLY_CREATED         = "isNewlyCreated";
  public static final String          PROPERTY_COLLECTION_ID   = "propertyCollectionId";
  public static final String          ENTITY_MAP               = "entity";
  public static final String          EMPTY                    = "";
  public static final String          ADDED_TAG                = "addedTag";
  
  private static final ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Autowired
  protected TransactionThreadData     transactionThread;
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IGetTaxonomyRequestModel parameterModel = new GetTaxonomyRequestModel();
    parameterModel.setId(code);
    return getMasterTaxonomy.execute(parameterModel);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    if (inputData.get(PARENT_TAXONOMY_ID).equals(Integer.valueOf(-1))) {
      ICreateMasterTaxonomyModel createMasterTaxonomyModel = (ICreateMasterTaxonomyModel) mapper.convertValue(inputData,
          CreateMasterTaxonomyModel.class);
      return createMasterTaxonomy.execute(createMasterTaxonomyModel);
    }
    else {
      List<ICreateMasterTaxonomyModel> listOfChildren = new ArrayList<>();
      listOfChildren.add(mapper.convertValue(inputData, CreateMasterTaxonomyModel.class));
      IListModel<ICreateMasterTaxonomyModel> listModel = new ListModel<>(listOfChildren);
      return bulkCreateMasterTaxonomy.execute(listModel);
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    Map<String, Object> entityMap = (Map<String, Object>) getData.get(ENTITY_MAP);
    SaveMasterTaxonomyModel saveTaxonomyModel = new SaveMasterTaxonomyModel();
    ITaxonomy entity = saveTaxonomyModel.getEntity();
    
    if (inputData.containsKey(CommonConstants.LABEL_PROPERTY)) {
      entity.setLabel((String) inputData.get(CommonConstants.LABEL_PROPERTY));
    }
    else {
      entity.setLabel((String) entityMap.get(CommonConstants.LABEL_PROPERTY));
    }
    if (inputData.containsKey(CommonConstants.ICON_PROPERTY)) {
      entity.setIcon((String) inputData.get(CommonConstants.ICON_PROPERTY));
    }
    else {
      entity.setIcon((String) entityMap.get(CommonConstants.ICON_PROPERTY));
    }
    if (inputData.containsKey(ADDED_TASKS)) {
      saveTaxonomyModel.setAddedTasks((List<String>) inputData.get(ADDED_TASKS));
    }
    else {
      saveTaxonomyModel.setAddedTasks(new ArrayList<String>());
    }
    if (inputData.containsKey(DELETED_TASKS)) {
      saveTaxonomyModel.setDeletedTasks((List<String>) inputData.get(DELETED_TASKS));
    }
    else {
      saveTaxonomyModel.setDeletedTasks(new ArrayList<String>());
    }
    
    if (inputData.containsKey(ADDED_CONTEXT_KLASSES)) {
      saveTaxonomyModel.setAddedContextKlasses(addEmbeddedKlassesToSave(inputData));
    }
    else {
      saveTaxonomyModel.setAddedContextKlasses(new ArrayList<>());
    }
    
    if (inputData.containsKey(MODIFIED_CONTEXT_KLASSES)) {
      saveTaxonomyModel.setModifiedContextKlasses(setModifiedDataToEmbeddedKlassesInSave(inputData));
    }
    else {
      saveTaxonomyModel.setModifiedContextKlasses(new ArrayList<>());
    }
    if (inputData.containsKey(DELETED_CONTEXT_KLASSES)) {
      saveTaxonomyModel.setDeletedContextKlasses((List<String>) inputData.get(DELETED_CONTEXT_KLASSES));
    }
    else {
      saveTaxonomyModel.setDeletedContextKlasses(new ArrayList<String>());
    }
    
    if (inputData.containsKey(ADDED_LEVEL)) {
      saveTaxonomyModel.setAddedLevel(addTagLevel(inputData));
    }
    if (inputData.containsKey(DELETED_LEVEL)) {
      List<Map<String, Object>> fetchedTagLevels = (List<Map<String, Object>>) entityMap.get(TAG_LEVELS);
      for (Map<String, Object> tagLevel : fetchedTagLevels) {
        Map<String, Object> tag = (Map<String, Object>) tagLevel.get(CommonConstants.TAG);
        if (inputData.get(DELETED_LEVEL).equals((String) tag.get(CommonConstants.ID_PROPERTY))) {
          saveTaxonomyModel.setDeletedLevel((String) tagLevel.get(CommonConstants.ID_PROPERTY));
          break;
        }
      }
    }
    if (inputData.containsKey(ADDED_SECTIONS)) {
      List<String> addedSections = (List<String>) inputData.get(ADDED_SECTIONS);
      saveTaxonomyModel.setAddedSections(addSectionsToSave(addedSections));
    }
    else {
      saveTaxonomyModel.setAddedSections(new ArrayList<>());
    }
    if (inputData.containsKey(DELETED_SECTIONS)) {
      saveTaxonomyModel.setDeletedSections(deleteSectionsInSave(inputData, entityMap));
    }
    else {
      saveTaxonomyModel.setDeletedSections(new ArrayList<String>());
    }
    saveTaxonomyModel.setModifiedSections(new ArrayList<>());
    saveTaxonomyModel.setAddedDataRules(new ArrayList<String>());
    saveTaxonomyModel.setDeletedDataRules(new ArrayList<String>());
    saveTaxonomyModel.setAddedAppliedKlasses(new ArrayList<String>());
    saveTaxonomyModel.setDeletedAppliedKlasses(new ArrayList<String>());
    saveTaxonomyModel.setModifiedElements(new ArrayList<>());
    // fields not allowed to update
    entity.setCode((String) entityMap.get(CommonConstants.CODE_PROPERTY));
    entity.setId((String) entityMap.get(CommonConstants.ID_PROPERTY));
    return saveMasterTaxonomy.execute(saveTaxonomyModel);
  }
  
  /**
   * Delete sections in Save
   * 
   * @param inputData
   * @param entityMap
   * @return
   */
  private List<String> deleteSectionsInSave(Map<String, Object> inputData, Map<String, Object> entityMap)
  {
    List<String> sectionCodesToDelete = (List<String>) inputData.get(DELETED_SECTIONS);
    List<String> idstoDelete = new ArrayList<String>();
    List<Map<String, Object>> fetchedSections = (List<Map<String, Object>>) entityMap.get(CommonConstants.SECTIONS_PROPERTY);
    for (String sectionCode : sectionCodesToDelete) {
      for (Map<String, Object> section : fetchedSections) {
        if (sectionCode.equals((String) section.get(PROPERTY_COLLECTION_ID))) {
          idstoDelete.add((String) section.get(CommonConstants.ID_PROPERTY));
          break;
        }
      }
    }
    return idstoDelete;
  }
  
  /**
   * Add a level to taxonomy
   * 
   * @param inputData
   * @return
   */
  private IAddedTaxonomyLevelModel addTagLevel(Map<String, Object> inputData)
  {
    IAddedTaxonomyLevelModel addedLevel = new AddedTaxonomyLevelModel();
    IAddedTagModel addedTag = new AddedTagModel();
    Map<String, Object> mapOfAddedTag = (Map<String, Object>) inputData.get(ADDED_LEVEL);
    
    if (mapOfAddedTag.containsKey(CommonConstants.LABEL_PROPERTY)) {
      addedTag.setLabel((String) mapOfAddedTag.get(CommonConstants.LABEL_PROPERTY));
    }
    if (mapOfAddedTag.containsKey(CommonConstants.ID_PROPERTY)) {
      addedTag.setId((String) mapOfAddedTag.get(CommonConstants.ID_PROPERTY));
    }
    if (mapOfAddedTag.containsKey(CommonConstants.CODE_PROPERTY)) {
      addedTag.setCode((String) mapOfAddedTag.get(CommonConstants.CODE_PROPERTY));
    }
    addedTag.setIsNewlyCreated((Boolean) mapOfAddedTag.get(IS_NEWLY_CREATED));
    addedTag.setTagValueIds(new ArrayList<String>());
    addedLevel.setAddedTag(addedTag);
    return addedLevel;
  }
  
  /**
   * sets modified data to existing embedded klasses in Save
   * 
   * @param inputData
   * @return
   */
  private List<IModifiedContextKlassModel> setModifiedDataToEmbeddedKlassesInSave(Map<String, Object> inputData)
  
  {
    List<IModifiedContextKlassModel> modifiedContextKlasses = new ArrayList<IModifiedContextKlassModel>();
    List<Map<String, Object>> listOfModifiedKlasses = (List<Map<String, Object>>) inputData.get(MODIFIED_CONTEXT_KLASSES);
    for (Map<String, Object> modifiedKlass : listOfModifiedKlasses) {
      
      IModifiedContextKlassModel contextKlassModel = new ModifiedContextKlassModel();
      contextKlassModel.setContextKlassId((String) modifiedKlass.get(CONTEXT_KLASS_ID));
      
      if (modifiedKlass.containsKey(ADDED_ATTRIBUTES)) {
        List<Map<String, Object>> addedAttributes = (List<Map<String, Object>>) modifiedKlass.get(ADDED_ATTRIBUTES);
        List<IIdAndCouplingTypeModel> listOfAttributes = setContextKlassAttributesAndTags(addedAttributes);
        contextKlassModel.setAddedAttributes(listOfAttributes);
      }
      else {
        contextKlassModel.setAddedAttributes(new ArrayList<>());
      }
      
      if (modifiedKlass.containsKey(ADDED_TAGS)) {
        List<Map<String, Object>> addedTags = (List<Map<String, Object>>) modifiedKlass.get(ADDED_TAGS);
        List<IIdAndCouplingTypeModel> listOfTags = setContextKlassAttributesAndTags(addedTags);
        contextKlassModel.setAddedTags(listOfTags);
      }
      else {
        contextKlassModel.setAddedTags(new ArrayList<>());
      }
      
      if (modifiedKlass.containsKey(MODIFIED_ATTRIBUTES)) {
        List<Map<String, Object>> modifiedAttributes = (List<Map<String, Object>>) modifiedKlass.get(MODIFIED_ATTRIBUTES);
        List<IIdAndCouplingTypeModel> listOfAttributes = setContextKlassAttributesAndTags(modifiedAttributes);
        contextKlassModel.setModifiedAttributes(listOfAttributes);
      }
      else {
        contextKlassModel.setModifiedAttributes(new ArrayList<>());
      }
      if (modifiedKlass.containsKey(MODIFIED_TAGS)) {
        List<Map<String, Object>> modifiedTags = (List<Map<String, Object>>) modifiedKlass.get(MODIFIED_TAGS);
        List<IIdAndCouplingTypeModel> listOfTags = setContextKlassAttributesAndTags(modifiedTags);
        contextKlassModel.setModifiedTags(listOfTags);
      }
      else {
        contextKlassModel.setModifiedTags(new ArrayList<>());
      }
      
      if (modifiedKlass.containsKey(DELETED_ATTRIBUTES)) {
        List<String> deletedAttributes = (List<String>) modifiedKlass.get(DELETED_ATTRIBUTES);
        contextKlassModel.setDeletedAttributes(deletedAttributes);
      }
      else {
        contextKlassModel.setDeletedAttributes(new ArrayList<>());
      }
      
      if (modifiedKlass.containsKey(DELETED_TAGS)) {
        List<String> deletedTags = (List<String>) modifiedKlass.get(DELETED_TAGS);
        contextKlassModel.setDeletedTags(deletedTags);
      }
      else {
        contextKlassModel.setDeletedTags(new ArrayList<>());
      }
      modifiedContextKlasses.add(contextKlassModel);
    }
    return modifiedContextKlasses;
  }
  
  /**
   * Add embedded Klasses to SAVE call
   * 
   * 
   * @param inputData
   * @return
   */
  private List<IContextKlassModel> addEmbeddedKlassesToSave(Map<String, Object> inputData)
  {
    List<IContextKlassModel> addedContextKlasses = new ArrayList<IContextKlassModel>();
    List<Map<String, Object>> listOfContextKlasses = (List<Map<String, Object>>) inputData.get(ADDED_CONTEXT_KLASSES);
    for (Map<String, Object> contextKlass : listOfContextKlasses) {
      IContextKlassModel contextKlassModel = new ContextKlassModel();
      contextKlassModel.setContextKlassId((String) contextKlass.get(CONTEXT_KLASS_ID));
      List<Map<String, Object>> attributes = (List<Map<String, Object>>) contextKlass.get(CommonConstants.ATTRIBUTES);
      if (!attributes.isEmpty()) {
        List<IIdAndCouplingTypeModel> listOfAttributes = setContextKlassAttributesAndTags(attributes);
        contextKlassModel.setAttributes(listOfAttributes);
      }
      else {
        contextKlassModel.setAttributes(new ArrayList<>());
      }
      List<Map<String, Object>> tags = (List<Map<String, Object>>) contextKlass.get(CommonConstants.TAGS);
      if (!tags.isEmpty()) {
        List<IIdAndCouplingTypeModel> listOfTags = setContextKlassAttributesAndTags(tags);
        contextKlassModel.setTags(listOfTags);
      }
      else {
        contextKlassModel.setTags(new ArrayList<>());
      }
      addedContextKlasses.add(contextKlassModel);
    }
    return addedContextKlasses;
  }
  
  /**
   * add PropertyCollections
   * 
   * 
   * @param addedSections
   * @return
   */
  private List<ISection> addSectionsToSave(List<String> addedSections)
  {
    List<ISection> listOfSections = new ArrayList<ISection>();
    
    for (int index = 0; index < addedSections.size(); index++) {
      ISection sectionObj = new Section();
      sectionObj.setPropertyCollectionId(addedSections.get(index));
      sectionObj.setSequence(index);
      sectionObj.setLabel(EMPTY);
      sectionObj.setType(CommonConstants.SECTION_TYPE);
      listOfSections.add(sectionObj);
    }
    return listOfSections;
  }
  
  /**
   * Sets data transfer properties for embedded klasses (attributes and tags
   * details)
   * 
   * 
   * @param entities
   * @return
   */
  public List<IIdAndCouplingTypeModel> setContextKlassAttributesAndTags(List<Map<String, Object>> entities)
  {
    List<IIdAndCouplingTypeModel> listOfEntities = new ArrayList<IIdAndCouplingTypeModel>();
    for (Map<String, Object> entity : entities) {
      IIdAndCouplingTypeModel idAndCouplingTypeModel = new IdAndCouplingTypeModel();
      idAndCouplingTypeModel.setId((String) entity.get(CommonConstants.ID_PROPERTY));
      idAndCouplingTypeModel.setCouplingType((String) entity.get(COUPLING_TYPE));
      listOfEntities.add(idAndCouplingTypeModel);
    }
    return listOfEntities;
  }
}
