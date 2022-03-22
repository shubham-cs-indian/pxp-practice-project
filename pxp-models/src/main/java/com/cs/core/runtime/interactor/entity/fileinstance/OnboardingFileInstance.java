package com.cs.core.runtime.interactor.entity.fileinstance;

import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IOnboardingFileInstance;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class OnboardingFileInstance extends AbstractContentInstance
    implements IOnboardingFileInstance {
  
  private static final long serialVersionUID = 1L;
  
  @JsonIgnore
  @Override
  public String getBranchOf()
  {
    // TODO Auto-generated method stub
    return super.getBranchOf();
  }
  
  @JsonIgnore
  @Override
  public IMessageInformation getMessages()
  {
    // TODO Auto-generated method stub
    return super.getMessages();
  }
  
  @JsonIgnore
  @Override
  public List<IRuleViolation> getRuleViolation()
  {
    // TODO Auto-generated method stub
    return super.getRuleViolation();
  }
}
