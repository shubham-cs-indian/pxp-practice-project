package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.datarule.RuleViolation;
import com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = Id.NONE)
public class ArticleInstanceWithDiffModel extends AbstractContentInstanceModel
    implements IArticleInstanceWithDiffModel {
  
  private static final long                                                serialVersionUID = 1L;
  
  protected IArticleInstance                                               articleInstance;
  
  protected IKlassInstanceDiffModel                                        klassInstanceDiffModel;
  
  protected Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets = new HashMap<>();
  
  public ArticleInstanceWithDiffModel()
  {
    this.articleInstance = new ArticleInstance();
  }
  
  public ArticleInstanceWithDiffModel(IArticleInstance articleInstance)
  {
    this.articleInstance = articleInstance;
  }
  
  @Override
  public IEntity getEntity()
  {
    return articleInstance;
  }
  
  @Override
  public String getId()
  {
    return articleInstance.getId();
  }
  
  @Override
  public void setId(String id)
  {
    articleInstance.setId(id);
  }
  
  @Override
  public String getName()
  {
    return articleInstance.getName();
  }
  
  @Override
  public void setName(String name)
  {
    articleInstance.setName(name);
  }
  
  @Override
  public List<? extends IContentTagInstance> getTags()
  {
    return articleInstance.getTags();
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    articleInstance.setTags(tags);
  }
  
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    return articleInstance.getAttributes();
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    articleInstance.setAttributes(attributes);
  }
  
  @Override
  public String getBranchOf()
  {
    return articleInstance.getBranchOf();
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    articleInstance.setBranchOf(branchOf);
  }
  
  @Override
  public List<? extends IRoleInstance> getRoles()
  {
    return articleInstance.getRoles();
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    articleInstance.setRoles(roles);
  }
  
  @Override
  public String getCreatedBy()
  {
    return articleInstance.getCreatedBy();
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    articleInstance.setCreatedBy(createdBy);
  }
  
  @Override
  public Long getCreatedOn()
  {
    return articleInstance.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    articleInstance.setCreatedOn(createdOn);
  }
  
  /*@Override
  public String getOwner()
  {
    return articleInstance.getOwner();
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    articleInstance.setOwner(owner);
  }*/
  
  @Override
  public String getBaseType()
  {
    return articleInstance.getBaseType();
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    articleInstance.setBaseType(baseType);
  }
  
  @Override
  public Long getVersionId()
  {
    return this.articleInstance.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.articleInstance.setVersionId(versionId);
  }
  
  /*
   * @JsonDeserialize(contentAs = KlassInstanceImage.class)
   *
   * @Override public void setAddedAssets(List<? extends IKlassInstanceImage>
   * usedImageKeys) { this.addedAssets = usedImageKeys; }
   *
   * @Override public List<? extends IKlassInstanceImage> getAddedAssets() {
   * return this.addedAssets; }
   *
   * @JsonDeserialize(contentAs = KlassInstanceImage.class)
   *
   * @Override public List<? extends IKlassInstanceImage> getDeletedAssets() {
   * return deletedAssets; }
   *
   * @JsonDeserialize(contentAs = KlassInstanceImage.class)
   *
   * @Override public void setDeletedtAssets(List<? extends IKlassInstanceImage>
   * deletedImageIds) { this.deletedAssets = deletedImageIds; }
   */
  
  @Override
  public Long getVersionTimestamp()
  {
    return articleInstance.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    articleInstance.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return articleInstance.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    articleInstance.setLastModifiedBy(lastModifiedBy);
  }
  
  @JsonIgnore
  @Override
  public IKlassInstanceDiffModel getKlassInstanceDiff()
  {
    return klassInstanceDiffModel;
  }
  
  @JsonProperty("klassInstanceDiff")
  @JsonDeserialize(as = KlassInstanceDiffModel.class)
  @Override
  public void setKlassInstanceDiff(IKlassInstanceDiffModel klassInstanceDiff)
  {
    this.klassInstanceDiffModel = klassInstanceDiff;
  }
  
  @Override
  public Long getLastModified()
  {
    return articleInstance.getLastModified();
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    articleInstance.setLastModified(lastModified);
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
  public List<IRuleViolation> getRuleViolation()
  {
    return this.articleInstance.getRuleViolation();
  }
  
  @JsonDeserialize(contentAs = RuleViolation.class)
  @Override
  public void setRuleViolation(List<IRuleViolation> ruleViolation)
  {
    this.articleInstance.setRuleViolation(ruleViolation);
  }
  
  @Override
  public IMessageInformation getMessages()
  {
    return this.articleInstance.getMessages();
  }
  
  @JsonDeserialize(as = MessageInformation.class)
  @Override
  public void setMessages(IMessageInformation messages)
  {
    this.articleInstance.setMessages(messages);
  }
  
  @Override
  public List<String> getTypes()
  {
    return articleInstance.getTypes();
  }
  
  @Override
  public void setTypes(List<String> multiClassificationTypes)
  {
    articleInstance.setTypes(multiClassificationTypes);
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return articleInstance.getDefaultAssetInstanceId();
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    articleInstance.setDefaultAssetInstanceId(defaultAssetInstanceId);
  }
  
  /**
   * **********************Ignored properties*************************
   */
  @JsonIgnore
  @Override
  public List<String> getTaxonomyIds()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTaxonomyIds(List<String> taxonomyId)
  {
  }
  
  @Override
  public Boolean getIsMerged()
  {
    return articleInstance.getIsMerged();
  }
  
  @Override
  public void setIsMerged(Boolean isMerged)
  {
    articleInstance.setIsMerged(isMerged);
  }
}
