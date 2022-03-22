package com.cs.core.runtime.interactor.model.instance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflict;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndVersionId;
import com.cs.core.runtime.interactor.entity.klassinstance.AssetAttributeInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAssetAttributeInstance;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflict;
import com.cs.core.runtime.interactor.entity.relationshipinstance.RelationshipConflict;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.assetinstance.ModifiedAssetAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.AbstractKlassInstanceSaveModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public abstract class AbstractContentInstanceSaveModel extends AbstractKlassInstanceSaveModel
    implements IContentInstanceSaveModel {
  
  private static final long                                      serialVersionUID = 1L;
  
  protected List<? extends IAssetAttributeInstance>              addedAssets      = new ArrayList<>();
  protected List<? extends IModifiedAssetAttributeInstanceModel> modifiedAssets   = new ArrayList<>();
  protected List<String>                                         deletedAssets    = new ArrayList<>();
  protected List<IRuleViolation>                                 ruleViolation;
  protected IMessageInformation                                  messages;
  protected String                                               processInstanceId;
  protected List<String>                                         languagesToCompare;
  protected Long                                                 iid;
  
  @Override
  public List<? extends IAssetAttributeInstance> getAddedAssets()
  {
    return addedAssets;
  }
  
  @JsonDeserialize(contentAs = AssetAttributeInstance.class)
  @JsonIgnore
  @Override
  public void setAddedAssets(List<? extends IAssetAttributeInstance> addedAssets)
  {
    this.addedAssets = addedAssets;
  }
  
  @Override
  public List<? extends IModifiedAssetAttributeInstanceModel> getModifiedAssets()
  {
    return modifiedAssets;
  }
  
  // @JsonIgnore
  @JsonDeserialize(contentAs = ModifiedAssetAttributeInstanceModel.class)
  @Override
  public void setModifiedAssets(List<? extends IModifiedAssetAttributeInstanceModel> modifiedAssets)
  {
    this.modifiedAssets = modifiedAssets;
  }
  
  @Override
  public List<String> getDeletedAssets()
  {
    return deletedAssets;
  }
  
  @JsonDeserialize(contentAs = AssetAttributeInstance.class)
  @JsonIgnore
  @Override
  public void setDeletedAssets(List<String> deletedAssets)
  {
    this.deletedAssets = deletedAssets;
  }
  
  @Override
  public IMessageInformation getMessages()
  {
    return messages;
  }
  
  @JsonDeserialize(as = MessageInformation.class)
  @Override
  public void setMessages(IMessageInformation messages)
  {
    this.messages = messages;
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return ((IContentInstance) this.entity).getDefaultAssetInstanceId();
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    ((IContentInstance) this.entity).setDefaultAssetInstanceId(defaultAssetInstanceId);
  }
  
  @Override
  public List<String> getTypes()
  {
    return entity.getTypes();
  }
  
  @Override
  public void setTypes(List<String> multiClassificationTypes)
  {
    entity.setTypes(multiClassificationTypes);
  }
  
  @Override
  public String getProcessInstanceId()
  {
    return processInstanceId;
  }
  
  @Override
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
  
  @Override
  public List<String> getLanguageCodes()
  {
    return ((IContentInstance) this.entity).getLanguageCodes();
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    ((IContentInstance) this.entity).setLanguageCodes(languageCodes);
  }
  
  @Override
  public String getCreationLanguage()
  {
    return ((IContentInstance) this.entity).getCreationLanguage();
  }
  
  @Override
  public void setCreationLanguage(String languageCodes)
  {
    ((IContentInstance) this.entity).setCreationLanguage(languageCodes);
  }
  
  /**
   * ************ variant fields ****************
   */
  @Override
  public String getKlassInstanceId()
  {
    // TODO Auto-generated method stub
    return ((IContentInstance) this.entity).getKlassInstanceId();
  }
  
  @Override
  public void setKlassInstanceId(String masterContentId)
  {
    ((IContentInstance) this.entity).setKlassInstanceId(masterContentId);
  }
  
  @Override
  public String getParentId()
  {
    return ((IContentInstance) this.entity).getParentId();
  }
  
  @Override
  public void setParentId(String parentVariantId)
  {
    ((IContentInstance) this.entity).setParentId(parentVariantId);
  }
  
  /*@Override
    public Boolean getIsFromExternalSource()
    {
      return ((IContentInstance) this.entity).getIsFromExternalSource();
    }
  */
  /*@Override
  public void setIsFromExternalSource(Boolean isFromExternalSource)
  {
    ((IContentInstance) this.entity).setIsFromExternalSource(isFromExternalSource);
  
  }*/
  
  /*@Override
  public Boolean getIsSkipped()
  {
    return ((IContentInstance) this.entity).getIsSkipped();
  }*/
  
  /*@Override
  public void setIsSkipped(Boolean isSkipped)
  {
    ((IContentInstance) this.entity).setIsSkipped(isSkipped);
  }*/
  
  @Override
  public List<String> getPath()
  {
    return ((IContentInstance) this.entity).getPath();
  }
  
  @Override
  public void setPath(List<String> path)
  {
    ((IContentInstance) this.entity).setPath(path);
  }
  
  @Override
  public List<String> getLanguagesToCompare()
  {
    if (languagesToCompare == null) {
      languagesToCompare = new ArrayList<>();
    }
    return languagesToCompare;
  }
  
  @Override
  public void setLanguagesToCompare(List<String> languagesToCompare)
  {
    this.languagesToCompare = languagesToCompare;
  }
  
  @Override
  public List<ITaxonomyConflict> getTaxonomyConflictingValues()
  {
    return ((IContentInstance) this.entity).getTaxonomyConflictingValues();
  }
  
  @Override
  @JsonDeserialize(contentAs = TaxonomyConflict.class)
  public void setTaxonomyConflictingValues(List<ITaxonomyConflict> taxonomyConflictingValues)
  {
    ((IContentInstance) this.entity).setTaxonomyConflictingValues(taxonomyConflictingValues);
  }
  
  /**
   * ********** Ignored Fields *****************
   */
  @Override
  @JsonIgnore
  public List<IRuleViolation> getRuleViolation()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setRuleViolation(List<IRuleViolation> ruleViolation)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getBranchOf()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setBranchOf(String branchOf)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getVersionOf()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionOf(String versionOf)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public IContextInstance getContext()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setContext(IContextInstance context)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getRelationships()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setRelationships(List<String> relationships)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getNatureRelationships()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setNatureRelationships(List<String> natureRelationships)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getVariants()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVariants(List<String> variants)
  {
  }
  
  @JsonIgnore
  @Override
  public List<IIdAndVersionId> getAttributeVariants()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setAttributeVariants(List<IIdAndVersionId> attributeVariants)
  {
  }
  
  @Override
  public String getCloneOf()
  {
    return ((IContentInstance) this.entity).getCloneOf();
  }
  
  @Override
  public void setCloneOf(String cloneOf)
  {
    ((IContentInstance) this.entity).setCloneOf(cloneOf);
  }
  
  @Override
  public Long getIid()
  {
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
  
  @Override
  public List<IRelationshipConflict> getRelationshipConflictingValues()
  {
    return ((IContentInstance) this.entity).getRelationshipConflictingValues();
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipConflict.class)
  public void setRelationshipConflictingValues(List<IRelationshipConflict> relationshipConflictingValues)
  {
    ((IContentInstance) this.entity).setRelationshipConflictingValues(relationshipConflictingValues);
  }
  
}
