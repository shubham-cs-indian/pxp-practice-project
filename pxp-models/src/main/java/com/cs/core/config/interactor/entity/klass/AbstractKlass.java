package com.cs.core.config.interactor.entity.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class AbstractKlass implements IKlass {
  
  private static final long                   serialVersionUID     = 1L;
  
  protected String                            id;
  
  protected IKlass                            parent;
  
  protected List<? extends ISection>          sections             = new ArrayList<>();
  
  protected String                            label;
  
  protected String                            type                 = this.getClass()
      .getName();
  
  protected String                            icon;
  
  protected String                            iconKey;

  
  protected String                            previewImage;
  
  protected List<IKlass>                      children             = new ArrayList<>();
  
  protected IKlassPermission                  permission           = new KlassPermission();
  
  protected Long                              versionId;
  
  protected Long                              versionTimestamp;
  
  protected Boolean                           isEnforcedTaxonomy   = false;
  
  // protected List<SectionRelationship> sectionRelationships = new
  // ArrayList<>();
  
  protected String                            lastModifiedBy;
  
  protected Boolean                           isStandard           = false;
  
  protected Long                              numberOfVersionsToMaintain;
  
  protected Map<String, Map<String, Boolean>> notificationSettings = new HashMap<>();
  
  protected String                            treeTypeOption       = "all";
  
  protected Boolean                           isDefaultChild       = false;
  
  protected Boolean                           isDefaultFolder      = false;
  
  protected Boolean                           isAllowedAtTopLevel  = false;
  
  protected Boolean                           isAbstract           = false;
  
  protected List<String>                      allowedTypes;
  
  protected List<String>                      dataRules;
  
  protected IKlassContext                     contexts;
  
  protected String                            natureType;
  
  protected List<IKlassNatureRelationship>    relationships        = new ArrayList<>();
  
  protected Boolean                           isNature;
  
  protected List<String>                      lifeCycleStatusTags  = new ArrayList<>();;
  
  protected List<String>                      tasks                = new ArrayList<>();
  
  protected List<String>                      embeddedKlassIds;
  
  protected String                            code;
  
  protected String                            languageKlassId;
  
  protected Long                              classifierIID;
  
  private String                              contextID;
  
  private Long                                contextIID;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public List<String> getLifeCycleStatusTags()
  {
    if (lifeCycleStatusTags == null) {
      lifeCycleStatusTags = new ArrayList<String>();
    }
    return lifeCycleStatusTags;
  }
  
  @Override
  public void setLifeCycleStatusTags(List<String> lifeCycleStatusTags)
  {
    this.lifeCycleStatusTags = lifeCycleStatusTags;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public List<? extends ISection> getSections()
  {
    return sections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    this.sections = sections;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return this.iconKey;
  }

  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey =iconKey;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<? extends IKlass> getChildren()
  {
    return children;
  }
  
  @JsonDeserialize(contentAs = AbstractKlass.class)
  @SuppressWarnings("unchecked")
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    this.children = (List<IKlass>) children;
  }
  
  @Override
  public ITreeEntity getParent()
  {
    return this.parent;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
      visible = true)
  @JsonDeserialize(as = AbstractKlass.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = (IKlass) parent;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public IKlassPermission getPermission()
  {
    return permission;
  }
  
  @JsonDeserialize(as = KlassPermission.class)
  @Override
  public void setPermission(IKlassPermission permission)
  {
    this.permission = permission;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public Boolean getIsEnforcedTaxonomy()
  {
    return isEnforcedTaxonomy;
  }
  
  /*
   * @JsonDeserialize(contentAs = SectionRelationship.class)
   *
   * @SuppressWarnings("unchecked")
   *
   * @Override public void setSectionRelationships(List<? extends
   * ISectionRelationship> sectionRelationships) { this.sectionRelationships =
   * (List<SectionRelationship>) sectionRelationships; }
   *
   * @Override public List<? extends ISectionRelationship>
   * getSectionRelationships() { return this.sectionRelationships; }
   */
  
  @Override
  public void setIsEnforcedTaxonomy(Boolean isEnforcedTaxonomy)
  {
    this.isEnforcedTaxonomy = isEnforcedTaxonomy;
  }
  
  @Override
  public Long getNumberOfVersionsToMaintain()
  {
    if (numberOfVersionsToMaintain == null) {
      numberOfVersionsToMaintain = 0l;
    }
    return numberOfVersionsToMaintain;
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Long numberOfVersionsToMaintain)
  {
    this.numberOfVersionsToMaintain = numberOfVersionsToMaintain;
  }
  
  @Override
  public Map<String, Map<String, Boolean>> getNotificationSettings()
  {
    return notificationSettings;
  }
  
  @Override
  public void setNotificationSettings(Map<String, Map<String, Boolean>> notificationSettings)
  {
    this.notificationSettings = notificationSettings;
  }
  
  @Override
  public String getTreeTypeOption()
  {
    return treeTypeOption;
  }
  
  @Override
  public void setTreeTypeOption(String treeTypeOption)
  {
    this.treeTypeOption = treeTypeOption;
  }
  
  @Override
  public Boolean getIsDefaultChild()
  {
    return isDefaultChild;
  }
  
  @Override
  public void setIsDefaultChild(Boolean isDefaultChild)
  {
    this.isDefaultChild = isDefaultChild;
  }
  
  @Override
  public Boolean getIsDefaultFolder()
  {
    return isDefaultFolder;
  }
  
  @Override
  public void setIsDefaultFolder(Boolean isDefaultFolder)
  {
    this.isDefaultFolder = isDefaultFolder;
  }
  
  @Override
  public Boolean getIsAllowedAtTopLevel()
  {
    return isAllowedAtTopLevel;
  }
  
  @Override
  public void setIsAllowedAtTopLevel(Boolean isAllowedAtTopLevel)
  {
    this.isAllowedAtTopLevel = isAllowedAtTopLevel;
  }
  
  @Override
  public Boolean getIsAbstract()
  {
    return isAbstract;
  }
  
  @Override
  public void setIsAbstract(Boolean isAbstract)
  {
    this.isAbstract = isAbstract;
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
  public List<String> getDataRules()
  {
    if (dataRules == null) {
      dataRules = new ArrayList<String>();
    }
    return dataRules;
  }
  
  @Override
  public void setDataRules(List<String> dataRules)
  {
    this.dataRules = dataRules;
  }
  
  @Override
  public IKlassContext getContexts()
  {
    
    return contexts;
  }
  
  @Override
  @JsonDeserialize(as = KlassContext.class)
  public void setContexts(IKlassContext variantContexts)
  {
    this.contexts = variantContexts;
  }
  
  @Override
  public String getNatureType()
  {
    
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    
    this.natureType = natureType;
  }
  
  @Override
  public List<IKlassNatureRelationship> getRelationships()
  {
    
    return relationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassNatureRelationship.class)
  public void setRelationships(List<IKlassNatureRelationship> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public String getPreviewImage()
  {
    return previewImage;
  }
  
  @Override
  public void setPreviewImage(String previewImage)
  {
    this.previewImage = previewImage;
  }
  
  @Override
  public Boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }
  
  @Override
  public List<String> getTasks()
  {
    return tasks;
  }
  
  @Override
  public void setTasks(List<String> tasks)
  {
    this.tasks = tasks;
  }
  
  @Override
  public List<String> getEmbeddedKlassIds()
  {
    if (embeddedKlassIds == null) {
      embeddedKlassIds = new ArrayList<String>();
    }
    return embeddedKlassIds;
  }
  
  @Override
  public void setEmbeddedKlassIds(List<String> embeddedKlassIds)
  {
    this.embeddedKlassIds = embeddedKlassIds;
  }
  
  @Override
  public String getLanguageKlassId()
  {
    return languageKlassId;
  }
  
  @Override
  public void setLanguageKlassId(String languageKlassId)
  {
    this.languageKlassId = languageKlassId;
  }
  
  @Override
  public Long getClassifierIID()
  {
    return classifierIID;
  }
  
  @Override
  public void setClassifierIID(Long classifierIID)
  {
    this.classifierIID = classifierIID;
  }
  
  @Override
  public String getContextID()
  {
    return this.contextID;
  }
  
  @Override
  public void setContextID(String contextID)
  {
    this.contextID = contextID;
  }
  
  @Override
  public Long getContextIID()
  {
    return this.contextIID;
  }
  
  @Override
  public void setContextIID(Long contextIID)
  {
    this.contextIID = contextIID;
  }
}
