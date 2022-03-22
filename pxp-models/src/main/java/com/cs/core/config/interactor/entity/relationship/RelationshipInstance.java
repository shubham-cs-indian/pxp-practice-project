package com.cs.core.config.interactor.entity.relationship;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class RelationshipInstance extends AbstractRuntimeEntity implements IRelationshipInstance {
  
  private static final long           serialVersionUID = 1L;
  
  protected String                    relationshipId;
  protected String                    commonRelationshipInstanceId;
  protected String                    side1InstanceId;
  protected String                    side2InstanceId;
  protected String                    side1BaseType;
  protected String                    side2BaseType;
  protected String                    variant1InstanceId;
  protected String                    variant2InstanceId;
  protected Integer                   count;
  protected String                    relationshipObjectId;
  protected String                    side1InstanceVersionId;
  protected String                    side2InstanceVersionId;
  protected IContextInstance          context;
  protected List<IContentTagInstance> tags             = new ArrayList<>();
  protected String                    originalInstanceId;
  protected String                    sideId;
  protected String                    otherSideId;
  protected String                    side1EntityType;
  protected String                    side2EntityType;
  
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
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getCommonRelationshipInstanceId()
  {
    return this.commonRelationshipInstanceId;
  }
  
  @Override
  public void setCommonRelationshipInstanceId(String commonRelationshipInstanceId)
  {
    this.commonRelationshipInstanceId = commonRelationshipInstanceId;
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
  public String getSide1InstanceId()
  {
    return side1InstanceId;
  }
  
  @Override
  public void setSide1InstanceId(String side1Id)
  {
    this.side1InstanceId = side1Id;
  }
  
  @Override
  public String getSide2InstanceId()
  {
    return side2InstanceId;
  }
  
  @Override
  public void setSide2InstanceId(String side2Id)
  {
    this.side2InstanceId = side2Id;
  }
  
  @Override
  public String getVariant1InstanceId()
  {
    return variant1InstanceId;
  }
  
  @Override
  public void setVariant1InstanceId(String variant1Id)
  {
    this.variant1InstanceId = variant1Id;
  }
  
  @Override
  public String getVariant2InstanceId()
  {
    return variant2InstanceId;
  }
  
  @Override
  public void setVariant2InstanceId(String variant2Id)
  {
    this.variant2InstanceId = variant2Id;
  }
  
  @Override
  public Integer getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Integer count)
  {
    this.count = count;
  }
  
  @Override
  public String getRelationshipObjectId()
  {
    return relationshipObjectId;
  }
  
  @Override
  public void setRelationshipObjectId(String relationshipObjectId)
  {
    this.relationshipObjectId = relationshipObjectId;
  }
  
  @Override
  public String getSide1BaseType()
  {
    return side1BaseType;
  }
  
  @Override
  public void setSide1BaseType(String side1BaseType)
  {
    this.side1BaseType = side1BaseType;
  }
  
  @Override
  public String getSide2BaseType()
  {
    return side2BaseType;
  }
  
  @Override
  public void setSide2BaseType(String side2BaseType)
  {
    this.side2BaseType = side2BaseType;
  }
  
  @Override
  public IContextInstance getContext()
  {
    return context;
  }
  
  @JsonDeserialize(as = ContextInstance.class)
  @Override
  public void setContext(IContextInstance context)
  {
    this.context = context;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getSide1InstanceVersionId()
  {
    return side1InstanceVersionId;
  }
  
  @Override
  public void setSide1InstanceVersionId(String side1InstanceVersionId)
  {
    this.side1InstanceVersionId = side1InstanceVersionId;
  }
  
  @Override
  public String getSide2InstanceVersionId()
  {
    return side2InstanceVersionId;
  }
  
  @Override
  public void setSide2InstanceVersionId(String side2InstanceVersionId)
  {
    this.side2InstanceVersionId = side2InstanceVersionId;
  }
  
  @Override
  public List<IContentTagInstance> getTags()
  {
    return tags;
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    this.tags = (List<IContentTagInstance>) tags;
  }
  
  @Override
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public String getOtherSideId()
  {
    return otherSideId;
  }
  
  @Override
  public void setOtherSideId(String otherSideId)
  {
    this.otherSideId = otherSideId;
  }
  
  @Override
  public String getSide1EntityType()
  {
    return side1EntityType;
  }
  
  @Override
  public void setSide1EntityType(String side1EntityType)
  {
    this.side1EntityType = side1EntityType;
  }
  
  @Override
  public String getSide2EntityType()
  {
    return side2EntityType;
  }
  
  @Override
  public void setSide2EntityType(String side2EntityType)
  {
    this.side2EntityType = side2EntityType;
  }
}
