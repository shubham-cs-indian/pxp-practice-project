package com.cs.core.config.interactor.model.klass;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.klass.IKlassViewSetting;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.klass.KlassViewSetting;
import com.cs.core.config.interactor.entity.klass.ProjectKlass;
import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.ClassFrameStructureValidator;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ProjectKlassModel extends AbstractKlassModel implements IProjectKlassModel {
  
  private static final long serialVersionUID = 1L;
  protected String          contextID;
  protected long            contextIID;
  
  public ProjectKlassModel()
  {
    super(new ProjectKlass());
  }
  
  public ProjectKlassModel(IProjectKlass klass)
  {
    super(klass);
  }
  
  @Override
  public IStructureValidator getValidator()
  {
    return ((ProjectKlass) this.entity).getValidator();
  }
  
  @JsonDeserialize(as = ClassFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    ((ProjectKlass) this.entity).setValidator(validator);
  }
  
  @Override
  public List<IStructure> getStructureChildren()
  {
    return ((ProjectKlass) this.entity).getStructureChildren();
  }
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
  @Override
  public void setStructureChildren(List<IStructure> elements)
  {
    ((ProjectKlass) this.entity).setStructureChildren(elements);
  }
  
  @Override
  public Date getStartDate()
  {
    return ((ProjectKlass) this.entity).getStartDate();
  }
  
  @Override
  public void setStartDate(Date date)
  {
    ((ProjectKlass) this.entity).setStartDate(date);
  }
  
  @Override
  public Date getEndDate()
  {
    return ((ProjectKlass) this.entity).getEndDate();
  }
  
  @Override
  public void setEndDate(Date date)
  {
    ((ProjectKlass) this.entity).setEndDate(date);
  }
  
  @Override
  public IKlassViewSetting getKlassViewSetting()
  {
    return ((ProjectKlass) entity).getKlassViewSetting();
  }
  
  @JsonDeserialize(as = KlassViewSetting.class)
  @Override
  public void setKlassViewSetting(IKlassViewSetting klassViewSetting)
  {
    ((ProjectKlass) entity).setKlassViewSetting(klassViewSetting);
  }
  
  @Override
  public Map<String, Integer> getReferencedClassIds()
  {
    return ((ProjectKlass) entity).getReferencedClassIds();
  }
  
  @Override
  public void setReferencedClassIds(Map<String, Integer> referencedClasIds)
  {
    ((ProjectKlass) entity).setReferencedClassIds(referencedClasIds);
  }
  
  @Override
  public Map<String, Integer> getReferencedAttributeIds()
  {
    return ((ProjectKlass) entity).getReferencedAttributeIds();
  }
  
  @Override
  public void setReferencedAttributeIds(Map<String, Integer> referencedAttributeIds)
  {
    ((ProjectKlass) entity).setReferencedAttributeIds(referencedAttributeIds);
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
  
  @JsonIgnore
  @Override
  public Boolean getIsGhost()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setIsGhost(Boolean isGhost)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getStructureId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setStructureId(String structureId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Integer getPosition()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setPosition(Integer position)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public IKlassViewSetting getViewSettings()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setViewSettings(IKlassViewSetting viewSettings)
  {
    // TODO Auto-generated method stub
  }
  
  @JsonIgnore
  @Override
  public Boolean getIsInherited()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setIsInherited(Boolean isInherited)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getLinkPath()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLinkPath(String linkPath)
  {
    // TODO Auto-generated method stub
  }
  
  @Override
  public Boolean getShouldVersion()
  {
    return ((ProjectKlass) entity).getShouldVersion();
  }
  
  @Override
  public void setShouldVersion(Boolean shouldVersion)
  {
    ((ProjectKlass) entity).setShouldVersion(shouldVersion);
  }
  
  @Override
  public Long getNumberOfVersionsToMaintain()
  {
    return ((ProjectKlass) entity).getNumberOfVersionsToMaintain();
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Long numberOfVersions)
  {
    ((ProjectKlass) entity).setNumberOfVersionsToMaintain(numberOfVersions);
  }
  
  @Override
  public Map<String, Map<String, Boolean>> getNotificationSettings()
  {
    return ((ProjectKlass) entity).getNotificationSettings();
  }
  
  @Override
  public void setNotificationSettings(Map<String, Map<String, Boolean>> notificationSettings)
  {
    ((ProjectKlass) entity).setNotificationSettings(notificationSettings);
  }
  
  @Override
  public Object clone() throws CloneNotSupportedException
  {
    // TODO Auto-generated method stub
    return super.clone();
  }
  
  @Override
  public String getGtinKlassId()
  {
    return ((ProjectKlass) entity).getGtinKlassId();
  }
  
  @Override
  public void setGtinKlassId(String gtinKlassId)
  {
    ((ProjectKlass) entity).setGtinKlassId(gtinKlassId);
  }
}
