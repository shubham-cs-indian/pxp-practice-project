package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.ClassFrameStructureValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectKlass extends AbstractKlass implements IProjectKlass {
  
  private static final long              serialVersionUID       = 1L;
  
  protected ClassFrameStructureValidator validator              = new ClassFrameStructureValidator();
  
  protected List<IStructure>             structureChildren      = new ArrayList<>();
  
  protected Date                         startDate;
  
  protected Date                         endDate;
  
  protected Map<String, Integer>         referencedClassIds     = new HashMap<>();
  
  protected IKlassViewSetting            klassViewSetting       = new KlassViewSetting();
  
  protected Map<String, Integer>         referencedAttributeIds = new HashMap<>();
  
  protected Boolean                      shouldVersion;
  
  protected Boolean                      isEnforcedTaxanomy     = false;
  
  protected String                       gtinKlassId;
  
  @Override
  public IStructureValidator getValidator()
  {
    return validator;
  }
  
  @JsonDeserialize(as = ClassFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    this.validator = (ClassFrameStructureValidator) validator;
  }
  
  @Override
  public List<IStructure> getStructureChildren()
  {
    return structureChildren;
  }
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
  @Override
  public void setStructureChildren(List<IStructure> elements)
  {
    this.structureChildren = elements;
  }
  
  @Override
  public Date getStartDate()
  {
    return startDate;
  }
  
  @Override
  public void setStartDate(Date date)
  {
    this.startDate = date;
  }
  
  @Override
  public Date getEndDate()
  {
    return endDate;
  }
  
  @Override
  public void setEndDate(Date date)
  {
    this.endDate = date;
  }
  
  @Override
  public Map<String, Integer> getReferencedClassIds()
  {
    return referencedClassIds;
  }
  
  @Override
  public void setReferencedClassIds(Map<String, Integer> referencedClasIds)
  {
    this.referencedClassIds = referencedClasIds;
  }
  
  @Override
  public IKlassViewSetting getKlassViewSetting()
  {
    return this.klassViewSetting;
  }
  
  @JsonDeserialize(as = KlassViewSetting.class)
  @Override
  public void setKlassViewSetting(IKlassViewSetting klassViewSetting)
  {
    this.klassViewSetting = klassViewSetting;
  }
  
  public Map<String, Integer> getReferencedAttributeIds()
  {
    return referencedAttributeIds;
  }
  
  public void setReferencedAttributeIds(Map<String, Integer> referencedAttributeIds)
  {
    this.referencedAttributeIds = referencedAttributeIds;
  }
  
  @Override
  public IKlassViewSetting getViewSettings()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setViewSettings(IKlassViewSetting viewSettings)
  {
    // TODO Auto-generated method stub
  }
  
  @JsonIgnore
  @Override
  public Boolean getIsGhost()
  {
    // TODO Auto-generated method stub
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
  
  @Override
  public String getLinkPath()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLinkPath(String linkPath)
  {
    // TODO Auto-generated method stub
  }
  
  @Override
  public Boolean getShouldVersion()
  {
    return shouldVersion;
  }
  
  @Override
  public void setShouldVersion(Boolean shouldVersion)
  {
    this.shouldVersion = shouldVersion;
  }
  
  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  @Override
  public String getGtinKlassId()
  {
    return gtinKlassId;
  }
  
  @Override
  public void setGtinKlassId(String gtinKlassId)
  {
    this.gtinKlassId = gtinKlassId;
  }
}
