package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKlassInstanceTreeInformationModel extends IModel {
  
  public static final String NAME               = "name";
  public static final String OWNER              = "owner";
  public static final String TYPE               = "type";
  public static final String ID                 = "id";
  public static final String VERSION_ID         = "versionId";
  public static final String PARENT_ID          = "parentId";
  public static final String KLASS_INSTANCE_IDS = "klassInstanceIds";
  public static final String SET_INSTANCE_IDS   = "setInstanceIds";
  public static final String IS_FOLDER          = "isFolder";
  
  public String getName();
  
  public void setName(String name);
  
  public String getOwner();
  
  public void setOwner(String owner);
  
  public String getType();
  
  public void setType(String type);
  
  public String getId();
  
  public void setId(String id);
  
  public Integer getVersionId();
  
  public void setVersionId(Integer versionId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public List<String> getSetInstanceIds();
  
  public void setSetInstanceIds(List<String> setInstanceIds);
  
  public Boolean getIsFolder();
  
  public void setIsFolder(Boolean isFolder);
}
