package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassRoleSetting;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.eventinstance.IEventInstanceListModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceListModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IGetKlassInstanceTreeModel extends IModel, Serializable {
  
  public static final String KLASS_INSTANCE                                  = "klassInstance";
  public static final String TYPE_KLASS                                      = "typeKlass";
  public static final String ALLOWED_TYPES                                   = "allowedTypes";
  public static final String REFERENCED_KLASSES                              = "referencedKlasses";
  public static final String REFERENCED_STRUCTURES                           = "referencedStructures";
  public static final String GLOBAL_PERMISSION                               = "globalPermission";
  public static final String STRUCTURE_DIFF                                  = "structureDiff";
  public static final String KLASS_VIEW_SETTING                              = "klassViewSetting";
  public static final String CHILDREN                                        = "children";
  public static final String TREE_ELEMENTS                                   = "treeElements";
  public static final String DEFAULT_TYPES                                   = "defaultTypes";
  public static final String REFERENCED_ASSETS                               = "referencedAssets";
  public static final String REFERENCE_RELATIONSHIP_INSTANCE_ELEMENTS        = "referenceRelationshipInstanceElements";
  public static final String FILTER_INFO                                     = "filterInfo";
  // public static final String CATEGORY_INFO = "categoryInfo";
  public static final String FROM                                            = "from";
  public static final String TOTAL_CONTENTS                                  = "totalContents";
  public static final String TAXONOMIES                                      = "taxonomies";
  public static final String VARIANTS                                        = "variants";
  public static final String VARIANT_INSTANCE_ID                             = "variantInstanceid";
  public static final String REFERENCE_NATURE_RELATIONSHIP_INSTANCE_ELEMENTS = "referenceNatureRelationshipInstanceElements";
  public static final String KLASS_DETAILS                                   = "klassDetails";
  public static final String EVENTS                                          = "events";
  public static final String EVENTS_COUNT                                    = "eventsCount";
  public static final String VARIANTS_COUNT                                  = "variantsCount";
  public static final String BRANCH_OF_LABEL                                 = "branchOfLabel";
  public static final String HIERARCHIES                                     = "hierarchies";
  public static final String TASKS                                           = "tasks";
  public static final String TASKS_COUNT                                     = "tasksCount";
  public static final String X_RAY_CONFIG_DETAILS                            = "xrayConfigDetails";
  
  public IContentInstance getKlassInstance();
  
  public void setKlassInstance(IContentInstance klassInstance);
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass typeKlass);
  
  public List<String> getAllowedTypes();
  
  public void setAllowedTypes(List<String> allowedTypes);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  
  public Map<String, ? extends IStructure> getReferencedStructures();
  
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public IKlassStructureDiffModel getStructureDiff();
  
  public void setStructureDiff(IKlassStructureDiffModel diff);
  
  public IKlassRoleSetting getKlassViewSetting();
  
  public void setKlassViewSetting(IKlassRoleSetting roleSetting);
  
  public List<IKlassInstanceInformationModel> getChildren();
  
  public void setChildren(List<IKlassInstanceInformationModel> childrens);
  
  public List<IKlassInstanceTreeInformationModel> getTreeElements();
  
  public void setTreeElements(List<IKlassInstanceTreeInformationModel> treeElements);
  
  public IGetDefaultKlassesModel getDefaultTypes();
  
  public void setDefaultTypes(IGetDefaultKlassesModel defaultTypes);
  
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets();
  
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements();
  
  // RelationshipInstance id as a string and the list of element instances
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances);
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  /*public List<ICategoryInformationModel> getCategoryInfo();
  
  public void setCategoryInfo(List<ICategoryInformationModel> categoryInfo);*/
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public long getTotalContents();
  
  public void setTotalContents(long totalContents);
  
  public List<ITaxonomyInformationModel> getTaxonomies();
  
  public void setTaxonomies(List<ITaxonomyInformationModel> taxonomies);
  
  public IGetMultiClassificationKlassDetailsModel getKlassDetails();
  
  public void setKlassDetails(IGetMultiClassificationKlassDetailsModel klassDetails);
  
  public List<IKlassInstance> getVariants();
  
  public void setVariants(List<IKlassInstance> variants);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements();
  
  // RelationshipInstance id as a string and the list of element instances
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstances);
  
  public Map<String, IEventInstanceListModel> getEvents();
  
  public void setEvents(Map<String, IEventInstanceListModel> events);
  
  public Long getEventsCount();
  
  public void setEventsCount(Long eventsCount);
  
  public Map<String, Integer> getVariantsCount();
  
  public void setVariantsCount(Map<String, Integer> variantsCount);
  
  public String getBranchOfLabel();
  
  public void setBranchOfLabel(String branchOfLabel);
  
  public List<IConfigEntityInformationModel> getHierarchies();
  
  public void setHierarchies(List<IConfigEntityInformationModel> hierarchies);
  
  public Map<String, ITaskInstanceListModel> getTasks();
  
  public void setTasks(Map<String, ITaskInstanceListModel> tasks);
  
  public Long getTasksCount();
  
  public void setTasksCount(Long tasksCount);
  
  public IXRayConfigDetailsModel getXRayConfigDetails();
  
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails);
}
