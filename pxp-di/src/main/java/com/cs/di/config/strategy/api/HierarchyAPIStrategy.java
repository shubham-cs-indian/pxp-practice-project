package com.cs.di.config.strategy.api;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.workflow.tasks.ITransformationTask;

@Service
public class HierarchyAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
  /*@Autowired
  protected IAddOrDeleteTaxonomyLevels addOrDeleteTaxonomyLevels;
  
  @Autowired
  protected ICreateHierarchyTaxonomy   createHierarchyTaxonomy;
  
  @Autowired
  protected ISaveHierarchyTaxonomy     saveHierarchyTaxonomy;*/
  
  public static final String           DEFAULT_PARENT_CODE = "-1";
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    return null;
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    String taxonomyCode = (String) inputData.get(ITransformationTask.CODE_COLUMN_CONFIG);
    String parentTaxonomyCode = (String) inputData.get(ITransformationTask.PARENT_CODE_COLUMN);
    
    //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
    //HierarchyTaxonomyCreateModel createHierarchyModel = new HierarchyTaxonomyCreateModel();
    // Set child info
    /*ITaxonomy entity = createHierarchyModel.getEntity();
    entity.setBaseType(IConfigClass.ClassifierClass.HIERARCHY_KLASS_TYPE.toString());
    entity.setCode(taxonomyCode);
    entity.setLabel((String) inputData.get(ITransformationTask.LABEL_COLUMN_CONFIG));
    entity.setTaxonomyType((String) inputData.get(ITransformationTask.TYPE_COLUMN));
    createHierarchyModel.setLinkedMasterTagParentId((String) inputData.get(ITransformationTask.MASTER_TAG_PARENT_CODE_COLUMN));
    
    // Set parent info
    HierarchyTaxonomy parent = new HierarchyTaxonomy();
    parent.setId(parentTaxonomyCode);
    parent.setBaseType(IConfigClass.ClassifierClass.HIERARCHY_KLASS_TYPE.toString());
    entity.setParent(parent);
    
    //IGetHierarchyTaxonomyWithoutKPModel responseModel = createHierarchyTaxonomy.execute(createHierarchyModel);*/
    
    // If root taxonomy then check for creation of levels.
    if (DEFAULT_PARENT_CODE.equals(parentTaxonomyCode)) {
      createTaxonomyLevels(inputData, taxonomyCode);
    }
    
    //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
    //return responseModel;
    return null;
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
    /*String taxonomyCode = (String) inputData.get(ITransformationTask.CODE_COLUMN_CONFIG);
    SaveHierarchyTaxonomyModel saveTaxonomyModel = new SaveHierarchyTaxonomyModel();
    ITaxonomy entity = saveTaxonomyModel.getEntity();
    entity.setBaseType(IConfigClass.ClassifierClass.HIERARCHY_KLASS_TYPE.toString());
    entity.setCode(taxonomyCode);
    entity.setId(taxonomyCode);
    entity.setLabel((String) inputData.get(ITransformationTask.LABEL_COLUMN_CONFIG));
    entity.setTaxonomyType((String) inputData.get(ITransformationTask.TYPE_COLUMN));
    return saveHierarchyTaxonomy.execute(saveTaxonomyModel);*/
    return null;
  }
  
  /**
   * Create or add taxonomy levels.
   * 
   * @param taxonomy
   * @param taxonomyCode
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  protected void createTaxonomyLevels(Map<String, Object> taxonomy, String taxonomyCode) throws Exception
  {
    //PXPFDEV-21214 : Deprecate - Taxonomy Hierarchies
    /*List<String> levelCodes = (List<String>) taxonomy.get(ITransformationTask.LEVEL_CODES_COLUMN);
    List<String> levellabels = (List<String>) taxonomy.get(ITransformationTask.LEVEL_LABEL_COLUMN);
    List<String> levelAcctionTypes = (List<String>) taxonomy.get(ITransformationTask.IS_NEWLY_CREATED_LEVEL_COLUMN);
    
    for (int position = 0; position < levelCodes.size(); position++) {
      IAddedDeletedLevelTaxonomyModel levelModel = new AddedDeletedLevelTaxonomyModel();
      levelModel.setCode(levelCodes.get(position));
      levelModel.setLabel(levellabels.get(position));
      levelModel.setTaxonomyId(taxonomyCode);
      if (levelAcctionTypes.get(position).equals("1")) {
        levelModel.setIsNewlyCreated(Boolean.TRUE.toString());
        levelModel.setAddedLevel(UUID.randomUUID().toString());
      }
      else {
        levelModel.setAddedLevel(levelCodes.get(position));
      }
      addOrDeleteTaxonomyLevels.execute(levelModel);
    }*/
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
