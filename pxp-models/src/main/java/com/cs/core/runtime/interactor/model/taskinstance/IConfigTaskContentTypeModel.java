package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigTaskContentTypeModel extends IModel {
  
  public static final String REFERENCED_KLASSES = "referencedKlasses";
  public static final String REFERENCED_ROLES   = "referencedRoles";
  public static final String REFERENCED_USERS   = "referencedUsers";
  
  public Map<String, IConfigTaskReferenceResponseModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IConfigTaskReferenceResponseModel> referencedKlasses);
  
  public Map<String, IConfigEntityInformationModel> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IConfigEntityInformationModel> referencedRoles);
  
  public Map<String, IConfigEntityInformationModel> getReferencedUsers();
  
  public void setReferencedUsers(Map<String, IConfigEntityInformationModel> referencedUsers);
}
