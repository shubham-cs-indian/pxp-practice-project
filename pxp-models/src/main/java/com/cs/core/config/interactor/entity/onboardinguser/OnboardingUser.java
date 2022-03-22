package com.cs.core.config.interactor.entity.onboardinguser;

import com.cs.core.config.interactor.entity.user.User;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    defaultImpl = OnboardingUser.class, visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(name = "onboardingUser", value = OnboardingUser.class) })
public class OnboardingUser extends User implements IOnboardingUser {
  
  private static final long serialVersionUID = 1L;
}
