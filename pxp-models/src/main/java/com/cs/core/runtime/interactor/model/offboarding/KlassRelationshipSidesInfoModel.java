package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.klass.KlassRelationshipSide;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KlassRelationshipSidesInfoModel implements IKlassRelationshipSidesInfoModel {
  
  private static final long        serialVersionUID = 1L;
  protected IKlassRelationshipSide side1;
  protected IKlassRelationshipSide side2;
  
  @Override
  public IKlassRelationshipSide getSide1()
  {
    return side1;
  }
  
  @Override
  @JsonDeserialize(as = KlassRelationshipSide.class)
  public void setSide1(IKlassRelationshipSide side1)
  {
    this.side1 = side1;
  }
  
  @Override
  public IKlassRelationshipSide getSide2()
  {
    return side2;
  }
  
  @Override
  @JsonDeserialize(as = KlassRelationshipSide.class)
  public void setSide2(IKlassRelationshipSide side2)
  {
    this.side2 = side2;
  }
}
