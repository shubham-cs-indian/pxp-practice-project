package com.cs.core.config.interactor.model.klass;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.AbstractKlass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassContext;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.IKlassPermission;
import com.cs.core.config.interactor.entity.klass.KlassContext;
import com.cs.core.config.interactor.entity.klass.KlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.KlassPermission;
import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.model.asset.AssetCollectionKlassModel;
import com.cs.core.config.interactor.model.asset.AssetModel;
import com.cs.core.config.interactor.model.supplier.SupplierModel;
import com.cs.core.config.interactor.model.target.TargetCollectionKlassModel;
import com.cs.core.config.interactor.model.target.TargetModel;
import com.cs.core.config.interactor.model.textasset.TextAssetModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({
    @Type(value = ContentKlassModel.class, name = "com.cs.core.config.interactor.entity.klass.ContentKlass"),
    @Type(value = ProjectKlassModel.class, name = "com.cs.core.config.interactor.entity.klass.ProjectKlass"),
    @Type(value = AssetModel.class, name = "com.cs.core.config.interactor.entity.klass.Asset"),
    @Type(value = SetKlassModel.class, name = "com.cs.core.config.interactor.entity.klass.SetKlass"),
    @Type(value = CollectionKlassModel.class, name = "com.cs.core.config.interactor.entity.klass.CollectionKlass"),
    @Type(value = AssetCollectionKlassModel.class,
        name = "com.cs.core.config.interactor.entity.klass.AssetCollectionKlass"),
    @Type(value = TargetModel.class, name = "com.cs.core.config.interactor.entity.klass.Market"),
    @Type(value = TargetCollectionKlassModel.class,
        name = "com.cs.core.config.interactor.entity.klass.TargetCollectionKlass"),
    @Type(value = TextAssetModel.class, name = "com.cs.core.config.interactor.entity.textasset.TextAsset"),
    @Type(value = SupplierModel.class, name = "com.cs.core.config.interactor.entity.supplier.Supplier") })
public abstract class AbstractKlassModel implements IKlassModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IKlass          entity;
  
  public AbstractKlassModel(IKlass klass)
  {
    this.entity = klass;
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public List<String> getLifeCycleStatusTags()
  {
    return entity.getLifeCycleStatusTags();
  }
  
  @Override
  public void setLifeCycleStatusTags(List<String> lifeCycleStatusTags)
  {
    entity.setLifeCycleStatusTags(lifeCycleStatusTags);
  }
  
  @Override
  public List<? extends ISection> getSections()
  {
    return entity.getSections();
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    entity.setSections(sections);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }

  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public ITreeEntity getParent()
  {
    return this.entity.getParent();
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
      visible = true)
  @JsonDeserialize(as = AbstractKlass.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.entity.setParent(parent);
  }
  
  @JsonDeserialize(contentAs = AbstractKlass.class)
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return this.entity.getChildren();
  }
  
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    this.entity.setChildren(children);
  }
  
  @Override
  public String getType()
  {
    return this.entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    this.entity.setType(type);
  }
  
  @Override
  public IKlassPermission getPermission()
  {
    return entity.getPermission();
  }
  
  @JsonDeserialize(as = KlassPermission.class)
  @Override
  public void setPermission(IKlassPermission permission)
  {
    entity.setPermission(permission);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return entity.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    entity.setIsStandard(isStandard);
  }
  
  @Override
  public Boolean getIsEnforcedTaxonomy()
  {
    return entity.getIsEnforcedTaxonomy();
  }
  
  @Override
  public void setIsEnforcedTaxonomy(Boolean isEnforcedTaxanomy)
  {
    entity.setIsEnforcedTaxonomy(isEnforcedTaxanomy);
  }
  
  @Override
  public Long getNumberOfVersionsToMaintain()
  {
    return entity.getNumberOfVersionsToMaintain();
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Long numberOfVersions)
  {
    entity.setNumberOfVersionsToMaintain(numberOfVersions);
  }
  
  @Override
  public Map<String, Map<String, Boolean>> getNotificationSettings()
  {
    return entity.getNotificationSettings();
  }
  
  @Override
  public void setNotificationSettings(Map<String, Map<String, Boolean>> notificationSettings)
  {
    entity.setNotificationSettings(notificationSettings);
  }
  
  @Override
  public String getTreeTypeOption()
  {
    return entity.getTreeTypeOption();
  }
  
  @Override
  public void setTreeTypeOption(String numberOfVersions)
  {
    entity.setTreeTypeOption(numberOfVersions);
  }
  
  @Override
  public Boolean getIsDefaultChild()
  {
    return entity.getIsDefaultChild();
  }
  
  @Override
  public void setIsDefaultChild(Boolean isDefaultChild)
  {
    entity.setIsDefaultChild(isDefaultChild);
  }
  
  @Override
  public Boolean getIsDefaultFolder()
  {
    return entity.getIsDefaultFolder();
  }
  
  @Override
  public void setIsDefaultFolder(Boolean isDefaultFolder)
  {
    entity.setIsDefaultFolder(isDefaultFolder);
  }
  
  @Override
  public Boolean getIsAllowedAtTopLevel()
  {
    return entity.getIsAllowedAtTopLevel();
  }
  
  @Override
  public void setIsAllowedAtTopLevel(Boolean isAllowedAtTopLevel)
  {
    entity.setIsAllowedAtTopLevel(isAllowedAtTopLevel);
  }
  
  @Override
  public Boolean getIsAbstract()
  {
    return entity.getIsAbstract();
  }
  
  @Override
  public void setIsAbstract(Boolean isAbstract)
  {
    entity.setIsAbstract(isAbstract);
  }
  
  @Override
  public List<String> getAllowedTypes()
  {
    return entity.getAllowedTypes();
  }
  
  @Override
  public void setAllowedTypes(List<String> allowedTypes)
  {
    entity.setAllowedTypes(allowedTypes);
  }
  
  @Override
  public List<String> getDataRules()
  {
    return entity.getDataRules();
  }
  
  @Override
  public void setDataRules(List<String> dataRules)
  {
    entity.setDataRules(dataRules);
  }
  
  @Override
  public IKlassContext getContexts()
  {
    return entity.getContexts();
  }
  
  @JsonDeserialize(as = KlassContext.class)
  @Override
  public void setContexts(IKlassContext variantContexts)
  {
    entity.setContexts(variantContexts);
  }
  
  @Override
  public String getNatureType()
  {
    
    return entity.getNatureType();
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    entity.setNatureType(natureType);
  }
  
  @Override
  public List<IKlassNatureRelationship> getRelationships()
  {
    
    return entity.getRelationships();
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassNatureRelationship.class)
  public void setRelationships(List<IKlassNatureRelationship> relationships)
  {
    entity.setRelationships(relationships);
  }
  
  @Override
  public String getPreviewImage()
  {
    return entity.getPreviewImage();
  }
  
  @Override
  public void setPreviewImage(String previewImage)
  {
    entity.setPreviewImage(previewImage);
  }
  
  @Override
  public Boolean getIsNature()
  {
    return entity.getIsNature();
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    entity.setIsNature(isNature);
  }
  
  @Override
  public List<String> getTasks()
  {
    return entity.getTasks();
  }
  
  @Override
  public void setTasks(List<String> tasks)
  {
    entity.setTasks(tasks);
  }
  
  @Override
  public List<String> getEmbeddedKlassIds()
  {
    return entity.getEmbeddedKlassIds();
  }
  
  @Override
  public void setEmbeddedKlassIds(List<String> embeddedKlassIds)
  {
    entity.setEmbeddedKlassIds(embeddedKlassIds);
  }
  
  @Override
  public String getLanguageKlassId()
  {
    return entity.getLanguageKlassId();
  }
  
  @Override
  public void setLanguageKlassId(String languageKlassId)
  {
    entity.setLanguageKlassId(languageKlassId);
  }
  
  @Override
  public Long getClassifierIID()
  {
    return entity.getClassifierIID();
  }
  
  @Override
  public void setClassifierIID(Long classifierIID)
  {
    entity.setClassifierIID(classifierIID);
  }
  
  @Override
  public String getContextID()
  {
    return this.entity.getContextID();
  }
  
  @Override
  public void setContextID(String contextID)
  {
    this.entity.setContextID(contextID);
  }
  
  @Override
  public Long getContextIID()
  {
    return this.entity.getContextIID();
  }
  
  @Override
  public void setContextIID(Long contextIID)
  {
    this.entity.setContextIID(contextIID);
  }
}
