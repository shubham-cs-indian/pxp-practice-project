package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassRoleSetting;
import com.cs.core.config.interactor.entity.klass.KlassRoleSetting;
import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.GetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.klass.GetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.xray.XRayConfigDetailsModel;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.customdeserializer.customDeserializer;
import com.cs.core.runtime.interactor.model.eventinstance.EventInstanceListModel;
import com.cs.core.runtime.interactor.model.eventinstance.IEventInstanceListModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceListModel;
import com.cs.core.runtime.interactor.model.taskinstance.TaskInstanceListModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassInstanceTreeModel implements IGetKlassInstanceTreeModel {
  
  private static final long                                                serialVersionUID                      = 1L;
  
  protected IContentInstance                                               klassInstance;
  protected IKlass                                                         typeKlass;
  protected List<String>                                                   allowedTypes;
  protected Map<String, IReferencedKlassDetailStrategyModel>               referencedKlasses                     = new HashMap<>();
  protected Map<String, ? extends IStructure>                              referencedStructures                  = new HashMap<>();
  protected IGlobalPermission                                              globalPermission;
  protected KlassStructureDiffModel                                        structureDiff;
  protected KlassRoleSetting                                               roleSetting;
  protected List<IKlassInstanceTreeInformationModel>                       treeElements;
  protected List<IKlassInstanceInformationModel>                           children;
  protected IGetDefaultKlassesModel                                        defaultTypes;
  protected Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets                      = new HashMap<>();
  protected Map<String, List<IKlassInstanceInformationModel>>              referenceRelationshipInstances        = new HashMap<>();
  protected IGetFilterInfoModel                                            filterInfo;
  protected int                                                            from                                  = 0;
  protected long                                                           totalContents                         = 0l;
  protected List<ITaxonomyInformationModel>                                taxonomies;
  protected IGetMultiClassificationKlassDetailsModel                       klassDetails;
  protected List<IKlassInstance>                                           variants;
  protected String                                                         variantInstanceId;
  protected Map<String, List<IKlassInstanceInformationModel>>              referencedNatureRelationshipInstances = new HashMap<>();
  protected Map<String, IEventInstanceListModel>                           events                                = new HashMap<>();
  protected Long                                                           eventsCount                           = 0L;
  protected Map<String, Integer>                                           variantsCount                         = new HashMap<>();
  protected String                                                         branchOfLabel;
  protected List<IConfigEntityInformationModel>                            hierarchies;
  protected Map<String, ITaskInstanceListModel>                            tasks                                 = new HashMap<>();
  protected Long                                                           tasksCount                            = 0L;
  protected IXRayConfigDetailsModel                                        xRayConfigDetails;
  
  @Override
  public IContentInstance getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(IContentInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    return typeKlass;
  }
  
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
  
  @Override
  public List<String> getAllowedTypes()
  {
    if (allowedTypes == null) {
      allowedTypes = new ArrayList<String>();
    }
    return allowedTypes;
  }
  
  @Override
  public void setAllowedTypes(List<String> allowedTypes)
  {
    this.allowedTypes = allowedTypes;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = (Map<String, IReferencedKlassDetailStrategyModel>) referencedKlasses;
  }
  
  @Override
  public Map<String, ? extends IStructure> getReferencedStructures()
  {
    return referencedStructures;
  }
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
  @Override
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures)
  {
    this.referencedStructures = referencedStructures;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public IKlassStructureDiffModel getStructureDiff()
  {
    return this.structureDiff;
  }
  
  @Override
  public void setStructureDiff(IKlassStructureDiffModel diff)
  {
    this.structureDiff = (KlassStructureDiffModel) diff;
  }
  
  @Override
  public IKlassRoleSetting getKlassViewSetting()
  {
    return this.roleSetting;
  }
  
  @Override
  public void setKlassViewSetting(IKlassRoleSetting roleSetting)
  {
    this.roleSetting = (KlassRoleSetting) roleSetting;
  }
  
  @Override
  public List<IKlassInstanceInformationModel> getChildren()
  {
    if (children == null) {
      children = new ArrayList<>();
    }
    return this.children;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceInformationModel.class)
  @Override
  public void setChildren(List<IKlassInstanceInformationModel> children)
  {
    this.children = children;
  }
  
  @Override
  public List<IKlassInstanceTreeInformationModel> getTreeElements()
  {
    return this.treeElements;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceTreeInformationModel.class)
  @Override
  public void setTreeElements(List<IKlassInstanceTreeInformationModel> treeElements)
  {
    this.treeElements = treeElements;
  }
  
  @Override
  public IGetDefaultKlassesModel getDefaultTypes()
  {
    return defaultTypes;
  }
  
  @Override
  @JsonDeserialize(as = GetDefaultKlassesModel.class)
  public void setDefaultTypes(IGetDefaultKlassesModel defaultTypes)
  {
    this.defaultTypes = defaultTypes;
  }
  
  @Override
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets()
  {
    return referencedAssets;
  }
  
  @JsonDeserialize(contentAs = AssetAttributeInstanceInformationModel.class)
  @Override
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
  
  @Override
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements()
  {
    return referenceRelationshipInstances;
  }
  
  @JsonDeserialize(contentUsing = customDeserializer.class)
  @Override
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances)
  {
    this.referenceRelationshipInstances = referenceRelationshipInstances;
  }
  
  @Override
  public IGetFilterInfoModel getFilterInfo()
  {
    if(filterInfo == null) {
      filterInfo = new GetFilterInfoModel();
    }
    return filterInfo;
  }
  
  @JsonDeserialize(as = GetFilterInfoModel.class)
  @Override
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  /*@Override
  public List<ICategoryInformationModel> getCategoryInfo()
  {
    return categoryInfo;
  }
  
  @JsonDeserialize(contentAs = CategoryInformationModel.class)
  @Override
  public void setCategoryInfo(List<ICategoryInformationModel> categoryInfo)
  {
    this.categoryInfo = categoryInfo;
  }*/
  
  @Override
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public long getTotalContents()
  {
    return totalContents;
  }
  
  @Override
  public void setTotalContents(long totalContents)
  {
    this.totalContents = totalContents;
  }
  
  @Override
  public List<ITaxonomyInformationModel> getTaxonomies()
  {
    if (taxonomies == null) {
      taxonomies = new ArrayList<>();
    }
    return taxonomies;
  }
  
  @Override
  public void setTaxonomies(List<ITaxonomyInformationModel> taxonomyIds)
  {
    this.taxonomies = taxonomyIds;
  }
  
  @Override
  public IGetMultiClassificationKlassDetailsModel getKlassDetails()
  {
    return klassDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetMultiClassificationKlassDetailsModel.class)
  public void setKlassDetails(IGetMultiClassificationKlassDetailsModel klassDetails)
  {
    this.klassDetails = klassDetails;
  }
  
  @Override
  public List<IKlassInstance> getVariants()
  {
    if (variants == null) {
      variants = new ArrayList<>();
    }
    return variants;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractKlassInstance.class)
  public void setVariants(List<IKlassInstance> variants)
  {
    this.variants = variants;
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return variantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    this.variantInstanceId = variantInstanceId;
  }
  
  @Override
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements()
  {
    
    return referencedNatureRelationshipInstances;
  }
  
  @Override
  @JsonDeserialize(contentUsing = customDeserializer.class)
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstances)
  {
    this.referencedNatureRelationshipInstances = referencedNatureRelationshipInstances;
  }
  
  @Override
  public Map<String, IEventInstanceListModel> getEvents()
  {
    return events;
  }
  
  @JsonDeserialize(contentAs = EventInstanceListModel.class)
  @Override
  public void setEvents(Map<String, IEventInstanceListModel> events)
  {
    this.events = events;
  }
  
  @Override
  public Long getEventsCount()
  {
    return eventsCount;
  }
  
  @Override
  public void setEventsCount(Long eventsCount)
  {
    this.eventsCount = eventsCount;
  }
  
  @Override
  public Map<String, Integer> getVariantsCount()
  {
    return variantsCount;
  }
  
  @Override
  public void setVariantsCount(Map<String, Integer> variantsCount)
  {
    this.variantsCount = variantsCount;
  }
  
  @Override
  public String getBranchOfLabel()
  {
    return branchOfLabel;
  }
  
  @Override
  public void setBranchOfLabel(String branchOfLabel)
  {
    this.branchOfLabel = branchOfLabel;
  }
  
  @Override
  public List<IConfigEntityInformationModel> getHierarchies()
  {
    if (hierarchies == null) {
      hierarchies = new ArrayList<>();
    }
    return hierarchies;
  }
  
  @Override
  public void setHierarchies(List<IConfigEntityInformationModel> hierarchies)
  {
    this.hierarchies = hierarchies;
  }
  
  @Override
  public Map<String, ITaskInstanceListModel> getTasks()
  {
    if(tasks == null) {
      tasks = new HashMap<>();
    }
    return tasks;
  }
  
  @JsonDeserialize(contentAs = TaskInstanceListModel.class)
  @Override
  public void setTasks(Map<String, ITaskInstanceListModel> tasks)
  {
    this.tasks = tasks;
  }
  
  @Override
  public Long getTasksCount()
  {
    return eventsCount;
  }
  
  @Override
  public void setTasksCount(Long tasksCount)
  {
    this.tasksCount = tasksCount;
  }
  
  @Override
  public IXRayConfigDetailsModel getXRayConfigDetails()
  {
    return xRayConfigDetails;
  }
  
  @JsonDeserialize(as = XRayConfigDetailsModel.class)
  @Override
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails)
  {
    this.xRayConfigDetails = xRayConfigDetails;
  }
}
