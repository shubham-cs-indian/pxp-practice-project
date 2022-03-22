package com.cs.core.runtime.interactor.entity.searchable;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.entity.propertyinstance.Statistics;
import com.cs.core.runtime.interactor.entity.tag.ISearchableTagInstance;
import com.cs.core.runtime.interactor.entity.tag.SearchableTagInstance;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSearchableInstance extends AbstractRuntimeEntity
    implements ISearchableInstance {
  
  private static final long                    serialVersionUID    = 1L;
  protected String                             klassInstanceId;
  protected IMessageInformation                messages;
  protected String                             baseType            = this.getClass()
      .getName();
  protected List<ISearchableAttributeInstance> attributes          = new ArrayList<>();
  protected List<ISearchableTagInstance>       tags                = new ArrayList<>();
  protected List<String>                       types               = new ArrayList<>();
  protected List<String>                       taxonomyIds         = new ArrayList<>();
  protected List<String>                       selectedTaxonomyIds = new ArrayList<>();
  protected String                             organizationId;
  protected String                             physicalCatalogId;
  protected String                             logicalCatalogId;
  protected String                             systemId;
  protected String                             endpointId;
  protected String                             originalInstanceId;
  protected List<IStatistics>                  dataGovernance;
  protected List<String>                       languageCodes;
  protected String                             parentId;
  protected Boolean                            isEmbedded;
  protected IContextInstance                   context;
  
  @Override
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public List<ISearchableAttributeInstance> getAttributes()
  {
    return this.attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = SearchableAttributeInstance.class)
  public void setAttributes(List<ISearchableAttributeInstance> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<ISearchableTagInstance> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = SearchableTagInstance.class)
  public void setTags(List<ISearchableTagInstance> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getTypes()
  {
    if (types == null) {
      types = new ArrayList<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(List<String> multiClassificationTypes)
  {
    this.types = multiClassificationTypes;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    this.logicalCatalogId = logicalCatalogId;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String masterContentId)
  {
    this.klassInstanceId = masterContentId;
  }
  
  @Override
  public IMessageInformation getMessages()
  {
    if (messages == null) {
      messages = new MessageInformation();
    }
    return messages;
  }
  
  @JsonDeserialize(as = MessageInformation.class)
  @Override
  public void setMessages(IMessageInformation messages)
  {
    this.messages = messages;
  }
  
  @Override
  public List<IStatistics> getDataGovernance()
  {
    return dataGovernance;
  }
  
  @JsonDeserialize(contentAs = Statistics.class)
  @Override
  public void setDataGovernance(List<IStatistics> dataGovernance)
  {
    this.dataGovernance = dataGovernance;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public Boolean getIsEmbedded()
  {
    return isEmbedded;
  }
  
  @Override
  public void setIsEmbedded(Boolean isEmbedded)
  {
    this.isEmbedded = isEmbedded;
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
}
