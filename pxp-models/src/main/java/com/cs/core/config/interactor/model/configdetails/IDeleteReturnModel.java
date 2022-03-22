package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;

public interface IDeleteReturnModel extends IBulkDeleteReturnModel {
  
 public static String DELETED_USER_NAMES = "deletedUserNames";
  
  public void setDeletedUserNames(List<String> deletedUserNames);
  
  public List<String> getDeletedUserNames();
}