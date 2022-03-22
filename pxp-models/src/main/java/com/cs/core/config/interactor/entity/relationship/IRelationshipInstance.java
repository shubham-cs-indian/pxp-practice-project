package com.cs.core.config.interactor.entity.relationship;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;

import java.util.List;

public interface IRelationshipInstance extends IRuntimeEntity {
  
  public static final String RELATIONSHIP_ID                 = "relationshipId";
  public static final String COMMON_RELATIONSHIP_INSTANCE_ID = "commonRelationshipInstanceId";
  public static final String SIDE1_INSTANCE_ID               = "side1InstanceId";
  public static final String SIDE2_INSTANCE_ID               = "side2InstanceId";
  public static final String SIDE1_INSTANCE_VERSION_ID       = "side1InstanceVersionId";
  public static final String SIDE2_INSTANCE_VERSION_ID       = "side2InstanceVersionId";
  public static final String VARIANT1_INSTANCE_ID            = "variant1InstanceId";
  public static final String VARIANT2_INSTANCE_ID            = "variant2InstanceId";
  public static final String COUNT                           = "count";
  public static final String RELATIONSHIP_OBJECT_ID          = "relationshipObjectId";
  public static final String SIDE1_BASE_TYPE                 = "side1BaseType";
  public static final String SIDE2_BASE_TYPE                 = "side2BaseType";
  public static final String CONTEXT                         = "context";
  public static final String TAGS                            = "tags";
  public static final String ORIGINAL_INSTANCE_ID            = "originalInstanceId";
  public static final String SIDE_ID                         = "sideId";
  
  public String getCommonRelationshipInstanceId();
  
  public void setCommonRelationshipInstanceId(String commonRelationshipInstanceId);
  
  public String getSide1InstanceId();
  
  public void setSide1InstanceId(String side1Id);
  
  public String getSide2InstanceId();
  
  public void setSide2InstanceId(String side2Id);
  
  public String getVariant1InstanceId();
  
  public void setVariant1InstanceId(String variant1InstanceId);
  
  public String getVariant2InstanceId();
  
  public void setVariant2InstanceId(String variant2InstanceId);
  
  public Integer getCount();
  
  public void setCount(Integer count);
  
  public String getRelationshipObjectId();
  
  public void setRelationshipObjectId(String relationshipObjectId);
  
  public String getSide1BaseType();
  
  public void setSide1BaseType(String side1BaseType);
  
  public String getSide2BaseType();
  
  public void setSide2BaseType(String side2BaseType);
  
  public IContextInstance getContext();
  
  public void setContext(IContextInstance context);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getSide1InstanceVersionId();
  
  public void setSide1InstanceVersionId(String side1InstanceVersionId);
  
  public String getSide2InstanceVersionId();
  
  public void setSide2InstanceVersionId(String side2InstanceVersionId);
  
  public List<? extends IContentTagInstance> getTags();
  
  public void setTags(List<? extends IContentTagInstance> tags);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public String getOtherSideId();
  
  public void setOtherSideId(String otherSideId);
  
  public String getSide1EntityType();
  
  public void setSide1EntityType(String sideEntityType);
  
  public String getSide2EntityType();
  
  public void setSide2EntityType(String otherSideEntityType);
}
