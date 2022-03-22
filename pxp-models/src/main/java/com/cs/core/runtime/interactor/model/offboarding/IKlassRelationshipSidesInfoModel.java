package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKlassRelationshipSidesInfoModel extends IModel {
  
  public static final String SIDE1 = "side1";
  public static final String SIDE2 = "side2";
  
  public IKlassRelationshipSide getSide1();
  
  public void setSide1(IKlassRelationshipSide side1);
  
  public IKlassRelationshipSide getSide2();
  
  public void setSide2(IKlassRelationshipSide side2);
}
