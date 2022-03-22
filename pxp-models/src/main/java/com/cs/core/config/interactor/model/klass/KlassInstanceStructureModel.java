package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceStructure;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceStructure;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceStructureModel;

import java.util.List;

public class KlassInstanceStructureModel implements IKlassInstanceStructureModel {
  
  private static final long         serialVersionUID = 1L;
  
  protected IKlassInstanceStructure entity;
  
  public KlassInstanceStructureModel()
  {
    this.entity = new KlassInstanceStructure();
  }
  
  public KlassInstanceStructureModel(IKlassInstanceStructure klassInstanceStructure)
  {
    this.entity = klassInstanceStructure;
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
  public List<? extends ITagInstance> getTags()
  {
    return entity.getTags();
  }
  
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    entity.setTags(tags);
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
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    return entity.getAttributes();
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    entity.setAttributes(attributes);
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
  public List<IRoleInstance> getRoles()
  {
    return entity.getRoles();
  }
  
  @Override
  public void setRoles(List<IRoleInstance> roles)
  {
    entity.setRoles(roles);
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
  public String getData()
  {
    return entity.getData();
  }
  
  @Override
  public void setData(String data)
  {
    entity.setData(data);
  }
  
  @Override
  public String getComment()
  {
    return entity.getComment();
  }
  
  @Override
  public void setComment(String comment)
  {
    entity.setComment(comment);
  }
  
  @Override
  public String getCaption()
  {
    return entity.getCaption();
  }
  
  @Override
  public void setCaption(String caption)
  {
    entity.setCaption(caption);
  }
  
  @Override
  public String getDescription()
  {
    return entity.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    entity.setDescription(description);
  }
  
  @Override
  public Boolean getIsValueChanged()
  {
    return entity.getIsValueChanged();
  }
  
  @Override
  public void setIsValueChanged(Boolean isValueChanged)
  {
    entity.setIsValueChanged(isValueChanged);
  }
  
  @Override
  public String getStructureId()
  {
    return entity.getStructureId();
  }
  
  @Override
  public void setStructureId(String structureId)
  {
    entity.setStructureId(structureId);
  }
  
  @Override
  public String getReferenceId()
  {
    return entity.getReferenceId();
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    entity.setReferenceId(referenceId);
  }
}
