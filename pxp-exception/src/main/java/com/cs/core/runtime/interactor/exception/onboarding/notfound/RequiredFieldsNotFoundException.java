package com.cs.core.runtime.interactor.exception.onboarding.notfound;

import com.cs.core.runtime.interactor.exception.onboarding.component.ComponentException;

public class RequiredFieldsNotFoundException extends ComponentException {
  
  private static final long serialVersionUID = 1L;
  
  public RequiredFieldsNotFoundException()
  {
    super();
  }
  
  public RequiredFieldsNotFoundException(
      RequiredFieldsNotFoundException requiredFieldsNotFoundException)
  {
    super();
  }
  
  public RequiredFieldsNotFoundException(String message)
  {
    super(message);
  }
}
