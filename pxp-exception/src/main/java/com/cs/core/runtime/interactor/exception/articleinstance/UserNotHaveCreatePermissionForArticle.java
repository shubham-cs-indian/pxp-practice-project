package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;

public class UserNotHaveCreatePermissionForArticle extends UserNotHaveCreatePermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveCreatePermissionForArticle()
  {
    super();
  }
  
  public UserNotHaveCreatePermissionForArticle(UserNotHaveCreatePermission e)
  {
    super(e);
  }
}
