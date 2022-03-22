package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.IKlassViewSetting;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.klass.KlassViewSetting;
import com.cs.core.config.interactor.entity.klass.ProjectKlass;
import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.ClassFrameStructureValidator;
import com.cs.core.config.interactor.model.configdetails.AbstractAddedStructureModel;
import com.cs.core.config.interactor.model.configdetails.IAddedStructureModel;
import com.cs.core.runtime.interactor.model.context.ModifiedContextKlassModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ProjectKlassSaveModel extends AbstractKlassSaveModel
    implements IProjectKlassSaveModel {
  
  private static final long                      serialVersionUID   = 1L;
  
  protected List<? extends IAddedStructureModel> addedStructures    = new ArrayList<>();
  
  protected List<? extends IStructure>           modifiedStructures = new ArrayList<>();
  
  protected List<String>                         deletedStructures  = new ArrayList<>();
  
  protected IContextKlassModel                   addedGtinKlass;
  protected String                               deletedGtinKlass;
  protected IModifiedContextKlassModel           modifiedGtinKlass;
  protected String                               contextID;
  protected long                                 contextIID;
  
  public ProjectKlassSaveModel()
  {
    super(new ProjectKlass());
  }
  
  public ProjectKlassSaveModel(ProjectKlass project)
  {
    super(project);
  }
  
  @Override
  public List<? extends IAddedStructureModel> getAddedStructures()
  {
    return addedStructures;
  }
  
  @JsonDeserialize(contentAs = AbstractAddedStructureModel.class)
  @Override
  public void setAddedStructures(List<? extends IAddedStructureModel> addedStructures)
  {
    this.addedStructures = addedStructures;
  }
  
  @Override
  public List<? extends IStructure> getModifiedStructures()
  {
    return this.modifiedStructures;
  }
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
  @Override
  public void setModifiedStructures(List<? extends IStructure> modifiedStructures)
  {
    this.modifiedStructures = modifiedStructures;
  }
  
  @Override
  public List<String> getDeletedStructures()
  {
    return this.deletedStructures;
  }
  
  @Override
  public void setDeletedStructures(List<String> deletedStructureIds)
  {
    this.deletedStructures = deletedStructureIds;
  }
  
  @Override
  public Date getStartDate()
  {
    return ((IProjectKlass) this.entity).getStartDate();
  }
  
  @Override
  public void setStartDate(Date date)
  {
    ((IProjectKlass) this.entity).setStartDate(date);
  }
  
  @Override
  public Date getEndDate()
  {
    return ((IProjectKlass) this.entity).getEndDate();
  }
  
  @Override
  public void setEndDate(Date date)
  {
    ((IProjectKlass) this.entity).setEndDate(date);
  }
  
  @Override
  public IStructureValidator getValidator()
  {
    return ((IProjectKlass) this.entity).getValidator();
  }
  
  @JsonDeserialize(as = ClassFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    ((IProjectKlass) this.entity).setValidator(validator);
  }
  
  @JsonIgnore
  @Override
  public List<IStructure> getStructureChildren()
  {
    // TODO : Bring it back later(Neo4j rest api uses old jackson which does not
    // use @JsonIgnore)
    // throw new RuntimeException("Not meant to be called on save Model");
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setStructureChildren(List<IStructure> elements)
  {
    // TODO : Bring it back later(Neo4j rest api uses old jackson which does not
    // use @JsonIgnore)
    // throw new RuntimeException("Not meant to be called on save Model");
  }
  
  @JsonDeserialize(as = ProjectKlass.class)
  @Override
  public ITreeEntity getParent()
  {
    return this.entity.getParent();
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
  
  @Override
  public IContextKlassModel getAddedGtinKlass()
  {
    return addedGtinKlass;
  }
  
  @Override
  @JsonDeserialize(as = ContextKlassModel.class)
  public void setAddedGtinKlass(IContextKlassModel addedGtinKlass)
  {
    this.addedGtinKlass = addedGtinKlass;
  }
  
  @Override
  public String getDeletedGtinKlass()
  {
    return deletedGtinKlass;
  }
  
  @Override
  public void setDeletedGtinKlass(String deletedGtinKlass)
  {
    this.deletedGtinKlass = deletedGtinKlass;
  }
  
  @Override
  public IModifiedContextKlassModel getModifiedGtinKlass()
  {
    return modifiedGtinKlass;
  }
  
  @Override
  @JsonDeserialize(as = ModifiedContextKlassModel.class)
  public void setModifiedGtinKlass(IModifiedContextKlassModel modifiedGtinKlass)
  {
    this.modifiedGtinKlass = modifiedGtinKlass;
  }

}
