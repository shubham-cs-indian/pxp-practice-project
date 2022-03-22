package com.cs.core.runtime.interactor.exception.articleinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveEditPermission;

public class UserNotHaveEditPermissionForArticle extends UserNotHaveEditPermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveEditPermissionForArticle()
  {
    super();
  }
  
  public UserNotHaveEditPermissionForArticle(UserNotHaveEditPermission e)
  {
    super(e);
  }
}
