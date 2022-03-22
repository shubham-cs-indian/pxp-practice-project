package com.cs.core.config.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagLevelEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.attributiontaxonomy.AddedTagModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.AddedTaxonomyLevelModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.CreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IAddedTagModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IAddedTaxonomyLevelModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.SaveMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.taxonomy.GetTaxonomyRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IJMSImportTaxanomyStatusModel;
import com.cs.core.config.interactor.model.taxonomy.IJMSImportTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.JMSImportTaxanomyStatusModel;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.IGetMasterTaxonomy;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IBulkCreateMasterTaxonomyStrategy;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetMasterTaxonomyStrategy;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.ISaveMasterTaxonomyStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.DiConstants;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.process.DiCreateException;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.dataintegration.EntityLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.dataintegration.IEntityLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.dataintegration.ITaxonomyCodeLevelIdMapModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.IJMSImportTaxonomy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.dataintegration.IGetIdsByCodesStrategy;
import com.cs.core.runtime.strategy.usecase.dataintegration.IGetLevelIdsByTaxonomyCodesStrategy;

@SuppressWarnings("unchecked")
@Component
public class JMSImportTaxonomy
    extends AbstractGetConfigInteractor<IJMSImportTaxonomyModel, IJMSImportTaxanomyStatusModel>
    implements IJMSImportTaxonomy {
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  /*@Autowired
  protected IAddOrDeleteTaxonomyLevelsStrategy  addOrDeleteTaxonomyLevelsStrategy;*/
  
  @Autowired
  protected IGetIdsByCodesStrategy              getIdsByCodeStrategy;
  
  @Autowired
  protected IBulkCreateMasterTaxonomyStrategy   bulkCreateMasterTaxonomyStrategy;
  
  @Autowired
  protected ISaveMasterTaxonomyStrategy         saveMasterTaxonomyStrategy;
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  /*@Autowired
  protected ISaveHierarchyTaxonomyStrategy      saveHierarchyTaxonomyStrategy;
  
  @Autowired
  protected ICreateHierarchyTaxonomyStrategy    createHierarchyTaxonomyStrategy;*/
  
  @Autowired
  protected IGetLevelIdsByTaxonomyCodesStrategy getLevelIdsByTaxonomyCodesStrategy;
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  /*@Autowired
  protected IGetHierarchyTaxonomyStrategy       getHierarchyTaxonomyStrategy;*/
  
  @Autowired
  protected IGetMasterTaxonomyStrategy          getMasterTaxonomyStrategy;
  
  @Autowired
  protected IGetMasterTaxonomy                  getMasterTaxonomy;
  protected IIdsListParameterModel              idsListParameterModel   = new IdsListParameterModel();
  protected ArrayList<String>                   linkedLevelSequenceList = new ArrayList<>();
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  /*@Autowired
  IGetHierarchyTaxonomy                         getHierarchyTaxonomy;*/

  @Override
  public IJMSImportTaxanomyStatusModel executeInternal(IJMSImportTaxonomyModel dataModel) throws Exception
  {
    
    List<String> successIDs = new ArrayList<>();
    List<String> failedIDs = new ArrayList<>();
    List<Map<String, String>> failureList = new ArrayList<>();
    
    String taxonomyType = dataModel.getType();
    // Get Ids of existing codes
    Map<String, Object> vertexlabelCodeIdMap = getVertexLabelCodeIdMap(dataModel, taxonomyType);
    Map<String, String> taxonomyCodeIdMap = (Map<String, String>) vertexlabelCodeIdMap
        .get(VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    Map<String, String> levelCodeIdMap = (Map<String, String>) vertexlabelCodeIdMap
        .get(VertexLabelConstants.ENTITY_TAG);
    Map<String, List<String>> parentChildMap = new HashMap<>();
    Map<String, String> taxonomyCodeLevelIdMap = null;
    String parentId = null;
    try {
      parentId = createOrUpdateTaxonomy(dataModel, taxonomyCodeIdMap, taxonomyType,
          taxonomyCodeLevelIdMap, dataModel.getId(), parentChildMap);
      successIDs.add(dataModel.getId());
    }
    catch (Exception e) {
      failedIDs.add(dataModel.getId());
      Map<String, String> failureMap = new HashMap<>();
      failureMap.put(ProcessConstants.TAXONOMY_ID, dataModel.getId());
      failureMap.put(DiConstants.ERROR_MESSAGE,
          "Error creating/updating parent Taxanomy with ID = " + dataModel.getId());
      failureList.add(failureMap);
    }
    
    // Create/Update levels
    List<String> levelTagIdToIgnore = new ArrayList<>();
    getLevelTagIdToIgnore(levelTagIdToIgnore, parentId, taxonomyType);
    Map<String, String> linkedLevels = dataModel.getLinkedLevels();
    Set<String> linkedLevelCodes = linkedLevels.keySet();
    for (String linkedlevelCode : linkedLevelCodes) {
      try {
        createOrUpdateLevel(dataModel, linkedlevelCode, linkedLevels.get(linkedlevelCode),
            taxonomyCodeIdMap, levelCodeIdMap, parentId, taxonomyType, levelTagIdToIgnore);
      }
      catch (Exception e) {
        failedIDs.add(dataModel.getId());
        Map<String, String> failureMap = new HashMap<>();
        failureMap.put(ProcessConstants.TAXONOMY_ID, dataModel.getId());
        failureMap.put(DiConstants.ERROR_MESSAGE,
            "Error creating/updating levels for parent Taxanomy with ID = " + dataModel.getId());
        failureList.add(failureMap);
      }
    }
    linkedLevelSequenceList = getLinkedLevelSequence(parentId, taxonomyType);
    taxonomyCodeLevelIdMap = getTaxonomyCodeLevelIdMap(dataModel, taxonomyCodeIdMap.keySet(),
        taxonomyType);
    // Children time
    List<IJMSImportTaxonomyModel> children = (List<IJMSImportTaxonomyModel>) dataModel
        .getChildren();
    for (IJMSImportTaxonomyModel child : children) {
      try {
        String parnetId = taxonomyCodeIdMap.get(child.getParentId());
        if (parnetId == null || parnetId.isEmpty()) {
          throw new DiCreateException("Invalid parent Id given for child ID = " + child.getId());
        }
        if (parnetId != null && parentChildMap.get(parnetId) == null
            && taxonomyType.equals(ProcessConstants.MASTER_TAXONOMY)) {
          List<String> childrenList = new ArrayList<>();
          getChildernsList(parnetId, childrenList);
          parentChildMap.put(parnetId, childrenList);
        }
        createOrUpdateTaxonomy(child, taxonomyCodeIdMap, taxonomyType, taxonomyCodeLevelIdMap,
            dataModel.getId(), parentChildMap);
        successIDs.add(child.getId());
      }
      catch (Exception e) {
        failedIDs.add(child.getId());
        Map<String, String> failureMap = new HashMap<>();
        failureMap.put(ProcessConstants.TAXONOMY_ID, child.getId());
        failureMap.put(DiConstants.ERROR_MESSAGE, e.getMessage() != null ? e.getMessage()
            : "Error creating/updating child Taxanomy with ID = " + child.getId());
        failureList.add(failureMap);
      }
    }
    
    IJMSImportTaxanomyStatusModel statusmodel = new JMSImportTaxanomyStatusModel();
    statusmodel.setSuccessIds(successIDs);
    statusmodel.setFailedIds(failedIDs);
    statusmodel.setFailureList(failureList);
    
    return statusmodel;
  }
  
  private void getChildernsList(String parnetId, List<String> childrenIdList)
  {
    IGetMasterTaxonomyWithoutKPModel attributionTaxonomyNode = getAttributionTaxonomyNode(parnetId);
    List<ITag> childrenMap = (List<ITag>) attributionTaxonomyNode.getEntity()
        .getChildren();
    for (ITag child : childrenMap) {
      childrenIdList.add(child.getId());
    }
  }
  
  private void getLevelTagIdToIgnore(List<String> levelTagIdToIgnore, String parentId,
      String taxonomyType)
  {
    if (taxonomyType.equals(ProcessConstants.MASTER_TAXONOMY)) {
      IGetMasterTaxonomyWithoutKPModel attributionTaxonomyNode = getAttributionTaxonomyNode(
          parentId);
      if (attributionTaxonomyNode != null) {
        List<ITagLevelEntity> existingTagLevels = attributionTaxonomyNode.getEntity()
            .getTagLevels();
        for (ITagLevelEntity tagLevels : existingTagLevels) {
          levelTagIdToIgnore.add(tagLevels.getTag()
              .getId());
        }
      }
    }
    
    //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
    /*else if (taxonomyType.equals(ProcessConstants.HIERARCHY_TAXONOMY)) {
      IGetHierarchyTaxonomyWithoutKPModel articleTaxonomyNode = getArticleTaxonomyNode(parentId);
      if (articleTaxonomyNode != null) {
        Map<String, String> existingTagLevels = articleTaxonomyNode.getEntity()
            .getLinkedLevels();
        for (Map.Entry<String, String> entry : existingTagLevels.entrySet()) {
          levelTagIdToIgnore.add(entry.getValue());
        }
      }
    }*/
  }
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  /*private IGetHierarchyTaxonomyWithoutKPModel getArticleTaxonomyNode(String parentId)
  {
    IGetTaxonomyRequestModel parameterModel = new GetTaxonomyRequestModel();
    parameterModel.setId(parentId);
    IGetHierarchyTaxonomyWithoutKPModel articleTaxonomyNode = null;
    try {
      articleTaxonomyNode = getHierarchyTaxonomy.execute(parameterModel);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    return articleTaxonomyNode;
  }*/
  
  private IGetMasterTaxonomyWithoutKPModel getAttributionTaxonomyNode(String parentId)
  {
    IGetTaxonomyRequestModel parameterModel = new GetTaxonomyRequestModel();
    parameterModel.setId(parentId);
    IGetMasterTaxonomyWithoutKPModel attributionTaxonomyNode = null;
    try {
      attributionTaxonomyNode = getMasterTaxonomy.execute(parameterModel);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    return attributionTaxonomyNode;
  }
  
  // taxonomy code:level_id(Tag_Id) Map
  private Map<String, String> getTaxonomyCodeLevelIdMap(IJMSImportTaxonomyModel dataModel,
      Set<String> taxonomyList, String taxonomyType) throws Exception
  {
    IEntityLabelCodeMapModel entityLabelCodeMapModel = new EntityLabelCodeMapModel();
    Map<String, Object> entityLabelCodeMap = new HashMap<>();
    ArrayList<String> codeList = new ArrayList<>(taxonomyList);
    codeList.remove(dataModel.getId());
    entityLabelCodeMap.put(taxonomyType, codeList);
    entityLabelCodeMapModel.setEntityLabelCodeMap(entityLabelCodeMap);
    ITaxonomyCodeLevelIdMapModel taxonomyCodeLevelIdMapModel = getLevelIdsByTaxonomyCodesStrategy
        .execute(entityLabelCodeMapModel);
    return taxonomyCodeLevelIdMapModel.getTaxonomyCodeLevelIdMap();
  }
  
  // Get level Sequence
  private ArrayList<String> getLinkedLevelSequence(String id, String taxonomyType) throws Exception
  {
    Map<String, String> linkedLevelSequence = new HashMap<>();
    IGetTaxonomyRequestModel model = new GetTaxonomyRequestModel();
    model.setId(id);
    switch (taxonomyType) {
      case ProcessConstants.MASTER_TAXONOMY:
      {
        List<ITagLevelEntity> tagLevels = getMasterTaxonomyStrategy.execute(model)
            .getEntity()
            .getTagLevels();
        int count = 1;
        for (ITagLevelEntity entity : tagLevels) {
          String tagId = entity.getTag()
              .getId();
          linkedLevelSequence.put(Integer.toString(count), tagId);
          count++;
        }
        break;
      }
      //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
      /*case ProcessConstants.HIERARCHY_TAXONOMY:
      {
        linkedLevelSequence = getHierarchyTaxonomyStrategy.execute(model)
            .getEntity()
            .getLinkedLevels();
        break;
      }*/
      default:
        break;
    }
    ArrayList<String> linkedLevelSequenceList = new ArrayList<>();
    if (linkedLevelSequence != null) {
      Set<String> keySet = linkedLevelSequence.keySet();
      for (String key : keySet) {
        linkedLevelSequenceList.add(linkedLevelSequence.get(key));
      }
    }
    return linkedLevelSequenceList;
  }
  
  // Create or update Taxonomy
  private String createOrUpdateTaxonomy(IJMSImportTaxonomyModel dataModel,
      Map<String, String> taxonomyCodeIdMap, String taxonomyType,
      Map<String, String> taxonomyCodeLevelIdMap, String parentCode,
      Map<String, List<String>> parentChildMap) throws Exception
  {
    String parentId = null;
    switch (taxonomyType) {
      case ProcessConstants.MASTER_TAXONOMY:
      {
        List<String> childrens = null;
        if (dataModel.getParentId() != null) {
          childrens = parentChildMap.get(taxonomyCodeIdMap.get(dataModel.getParentId()));
        }
        if (childrens == null) {
          childrens = new ArrayList<>();
        }
        String tagId = taxonomyCodeIdMap.get(dataModel.getId());
        if (tagId != null && childrens.contains(tagId)) {
          parentId = updateMasterTaxonomy(dataModel, taxonomyCodeIdMap);
          return parentId;
        }
        parentId = createMasterTaxonomy(dataModel, taxonomyCodeIdMap, taxonomyCodeLevelIdMap,
            parentCode, childrens);
        return parentId;
      }
      //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
      /*case ProcessConstants.HIERARCHY_TAXONOMY:
      {
        if (taxonomyCodeIdMap.containsKey(dataModel.getId())) {
          parentId = updateHierarchyTaxonomy(dataModel, taxonomyCodeIdMap);
          return parentId;
        }
        parentId = createHierarchyTaxonomy(dataModel, taxonomyCodeIdMap, taxonomyCodeLevelIdMap,
            parentCode);
        return parentId;
      }*/
      default:
        return parentId;
    }
  }
  
  // Create Master Taxonomy
  private String createMasterTaxonomy(IJMSImportTaxonomyModel dataModel,
      Map<String, String> taxonomyCodeIdMap, Map<String, String> taxonomyCodeLevelIdMap,
      String parentCode, List<String> childrens) throws Exception
  {
    ICreateMasterTaxonomyModel createAttributionTaxonomyModel = new CreateMasterTaxonomyModel();
    String tagId = taxonomyCodeIdMap.get(dataModel.getId());
    if (tagId != null && !childrens.contains(tagId)) {
      createAttributionTaxonomyModel.setTagValueId(tagId);
      createAttributionTaxonomyModel.setIsNewlyCreated(false);
    }
    else {
      tagId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAXONOMY.getPrefix());
      createAttributionTaxonomyModel.setIsNewlyCreated(true);
      createAttributionTaxonomyModel.setTaxonomyId(tagId);
      createAttributionTaxonomyModel.setCode(dataModel.getId());
    }
    createAttributionTaxonomyModel.setLabel(dataModel.getLabel());
    String taxonomyType = dataModel.getTaxonomyType();
    if (taxonomyType != null) {
      createAttributionTaxonomyModel.setTaxonomyType(dataModel.getTaxonomyType()
          .equals("minor") ? DiConstants.MINOR_TAXONOMY : DiConstants.MAJOR_TAXONOMY);
    }
    String childLevelId = setLevelId(dataModel, taxonomyCodeLevelIdMap, parentCode);
    createAttributionTaxonomyModel.setParentTagId(childLevelId);
    String parent = dataModel.getParentId();
    createAttributionTaxonomyModel
        .setParentTaxonomyId(parent == null ? "-1" : taxonomyCodeIdMap.get(parent));
    
    IListModel<ICreateMasterTaxonomyModel> createAttributionTaxonomyModelList = new ListModel<>();
    List<ICreateMasterTaxonomyModel> requestNodeList = new ArrayList<>();
    requestNodeList.add(createAttributionTaxonomyModel);
    createAttributionTaxonomyModelList.setList(requestNodeList);
    bulkCreateMasterTaxonomyStrategy.execute(createAttributionTaxonomyModelList);
    taxonomyCodeIdMap.put(dataModel.getId(), tagId);
    return tagId;
  }
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  // createHierarchyTaxonomy
  /*private String createHierarchyTaxonomy(IJMSImportTaxonomyModel dataModel,
      Map<String, String> taxonomyCodeIdMap, Map<String, String> taxonomyCodeLevelIdMap,
      String parentCode) throws Exception
  {
    IHierarchyTaxonomyCreateModel model = new HierarchyTaxonomyCreateModel();
    model.setCode(dataModel.getId());
    model.setLabel(dataModel.getLabel());
    String taxonomyType = dataModel.getTaxonomyType();
    if (taxonomyType != null) {
      model.setTaxonomyType(dataModel.getTaxonomyType()
          .equals("minor") ? DiConstants.MINOR_TAXONOMY : DiConstants.MAJOR_TAXONOMY);
    }
    ITaxonomy parentModel = new HierarchyTaxonomy();
    String parent = dataModel.getParentId();
    parentModel.setId(parent == null ? "-1" : taxonomyCodeIdMap.get(parent));
    model.setParent(parentModel);
    String childLevelId = setLevelId(dataModel, taxonomyCodeLevelIdMap, parentCode);
    model.setLinkedMasterTagParentId(childLevelId);
    IGetHierarchyTaxonomyWithoutKPModel createdTaxonomy = createHierarchyTaxonomyStrategy
        .execute(model);
    String id = createdTaxonomy.getEntity()
        .getId();
    taxonomyCodeIdMap.put(dataModel.getId(), id);
    return id;
  }*/
  
  // set level Id
  private String setLevelId(IJMSImportTaxonomyModel dataModel,
      Map<String, String> taxonomyCodeLevelIdMap, String parentCode)
  {
    String parent = dataModel.getParentId();
    String code = dataModel.getId();
    String childLevelId = null;
    if (taxonomyCodeLevelIdMap != null && taxonomyCodeLevelIdMap.containsKey(code)) {
      return taxonomyCodeLevelIdMap.get(code);
    }
    if (parent != null) {
      if (parent.equals(parentCode)) {
        childLevelId = linkedLevelSequenceList.get(0);
        taxonomyCodeLevelIdMap.put(code, childLevelId);
      }
      else {
        String parentLevelId = taxonomyCodeLevelIdMap.get(parent);
        
        int parentIndex = linkedLevelSequenceList.indexOf(parentLevelId);
        parentIndex++;
        childLevelId = linkedLevelSequenceList.get(parentIndex);
        taxonomyCodeLevelIdMap.put(code, childLevelId);
      }
    }
    return childLevelId;
  }
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  // updateHierarchyTaxonomy
  /*private String updateHierarchyTaxonomy(IJMSImportTaxonomyModel dataModel,
      Map<String, String> taxonomyCodeIdMap) throws Exception
  {
    ISaveHierarchyTaxonomyModel model = new SaveHierarchyTaxonomyModel();
    model.setId(taxonomyCodeIdMap.get(dataModel.getId()));
    model.setCode(dataModel.getId());
    model.setLabel(dataModel.getLabel());
    IGetHierarchyTaxonomyWithoutKPStrategyResponseModel updatedTaxonomyModel = saveHierarchyTaxonomyStrategy
        .execute(model);
    String id = updatedTaxonomyModel.getEntity()
        .getId();
    return id;
  }*/
  
  // Update Master Taxonomy
  private String updateMasterTaxonomy(IJMSImportTaxonomyModel dataModel,
      Map<String, String> taxonomyCodeIdMap) throws Exception
  {
    ISaveMasterTaxonomyModel model = new SaveMasterTaxonomyModel();
    model.setId(taxonomyCodeIdMap.get(dataModel.getId()));
    model.setCode(dataModel.getId());
    model.setLabel(dataModel.getLabel());
    IMasterTaxonomy updatedTaxonomyModel = saveMasterTaxonomyStrategy.execute(model)
        .getEntity();
    String id = updatedTaxonomyModel.getId();
    return id;
  }
  
  // Create or Update level
  private void createOrUpdateLevel(IJMSImportTaxonomyModel dataModel, String linkedlevelCode,
      String linkedLevelLabel, Map<String, String> taxonomyCodeIdMap,
      Map<String, String> levelCodeIdMap, String parentId, String taxonomyType,
      List<String> levelTagIdToIgnore) throws Exception
  {
    // if (!levelCodeIdMap.containsKey(linkedlevelCode)) {
    switch (taxonomyType) {
      case ProcessConstants.MASTER_TAXONOMY:
        createLevelForMaster(dataModel, linkedlevelCode, linkedLevelLabel, taxonomyCodeIdMap,
            levelCodeIdMap, parentId, levelTagIdToIgnore);
        break;
        //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
      /*case ProcessConstants.HIERARCHY_TAXONOMY:
        createLevelForHierarchy(dataModel, linkedlevelCode, linkedLevelLabel, taxonomyCodeIdMap,
            levelCodeIdMap, parentId, levelTagIdToIgnore);
        break;*/
      default:
        break;
    }
    // }
  }
  
  // Create Level for Master Taxonomy
  private void createLevelForMaster(IJMSImportTaxonomyModel dataModel, String linkedLevelCode,
      String linkedLevelLabel, Map<String, String> taxonomyCodeIdMap,
      Map<String, String> levelCodeIdMap, String parentId, List<String> levelTagIdToIgnore)
      throws Exception
  {
    String tagId = levelCodeIdMap.get(linkedLevelCode);
    if (!levelTagIdToIgnore.contains(tagId)) {
      ISaveMasterTaxonomyModel model = new SaveMasterTaxonomyModel();
      IAddedTagModel addedTag = new AddedTagModel();
      IAddedTaxonomyLevelModel addedTaxonomyLevelModel = new AddedTaxonomyLevelModel();
      addedTaxonomyLevelModel.setAddedTag(addedTag);
      model.setAddedLevel(addedTaxonomyLevelModel);
      if (tagId == null) {
        addedTag.setIsNewlyCreated(true);
        tagId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAXONOMY.getPrefix());
        addedTag.setCode(linkedLevelCode);
      }
      addedTag.setId(tagId);
      addedTag.setLabel(linkedLevelLabel);
      model.setId(parentId);
      model.setLabel(dataModel.getLabel());
      model.setCode(dataModel.getId());
      
      IGetMasterTaxonomyWithoutKPStrategyResponseModel levelModel = saveMasterTaxonomyStrategy
          .execute(model);
      levelCodeIdMap.put(levelModel.getEntity()
          .getCode(),
          levelModel.getEntity()
              .getId());
    }
  }
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  // createLevelForHierarchy
  /*private void createLevelForHierarchy(IJMSImportTaxonomyModel dataModel, String linkedlevelCode,
      String linkedLevelLabel, Map<String, String> taxonomyCodeIdMap,
      Map<String, String> levelCodeIdMap, String parentId, List<String> levelTagIdToIgnore)
      throws Exception
  {
    String tagId = levelCodeIdMap.get(linkedlevelCode);
    if (!levelTagIdToIgnore.contains(tagId)) {
      IAddedDeletedLevelTaxonomyModel addedDeletedTaxonomyLevelModel = new AddedDeletedLevelTaxonomyModel();
      addedDeletedTaxonomyLevelModel.setTaxonomyId(parentId);
      if (tagId != null) {
        addedDeletedTaxonomyLevelModel.setAddedLevel(tagId);
      }
      else {
        tagId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAXONOMY.getPrefix());
        addedDeletedTaxonomyLevelModel.setAddedLevel(tagId);
        levelCodeIdMap.put(linkedlevelCode, tagId);
        addedDeletedTaxonomyLevelModel.setCode(linkedlevelCode);
        addedDeletedTaxonomyLevelModel.setLabel(linkedLevelLabel);
        addedDeletedTaxonomyLevelModel.setIsNewlyCreated("true");
      }
      addedDeletedTaxonomyLevelModel.setDeletedLevel("");
      addOrDeleteTaxonomyLevelsStrategy.execute(addedDeletedTaxonomyLevelModel);
    }
  }*/
  
  // Get VertexlabelCodeMap
  private Map<String, Object> getVertexLabelCodeIdMap(IJMSImportTaxonomyModel dataModel,
      String taxonomyType) throws Exception
  {
    // Initialization
    IEntityLabelCodeMapModel entityLabelCodeMapModel = new EntityLabelCodeMapModel();
    Set<String> taxonomyList = new HashSet<String>();
    Set<String> levelList = new HashSet<String>();
    Map<String, Object> entityLabelCodeMap = new HashMap<String, Object>();
    // Prepare List for input model
    taxonomyList.add(dataModel.getId());
    Set<String> keySet = dataModel.getLinkedLevels()
        .keySet();
    for (String key : keySet) {
      levelList.add(key);
    }
    List<IJMSImportTaxonomyModel> children = (List<IJMSImportTaxonomyModel>) dataModel
        .getChildren();
    for (IJMSImportTaxonomyModel child : children) {
      taxonomyList.add(child.getId());
      String parent = child.getParentId();
      taxonomyList.add(parent);
    }
    // Prepare Map for input model
    entityLabelCodeMap.put(VertexLabelConstants.ROOT_KLASS_TAXONOMY,
        new ArrayList<String>(taxonomyList));
    entityLabelCodeMap.put(VertexLabelConstants.ENTITY_TAG, new ArrayList<String>(levelList));
    entityLabelCodeMapModel.setEntityLabelCodeMap(entityLabelCodeMap);
    Map<String, Object> vertexLabelCodeIdMap = getIdsByCodeStrategy.execute(entityLabelCodeMapModel)
        .getEntityLabelCodeMap();
    return vertexLabelCodeIdMap;
  }
}
