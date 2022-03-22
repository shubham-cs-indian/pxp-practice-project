package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.KlassNatureRelationship;

public class ReferencedNatureRelationshipModel extends KlassNatureRelationship
implements IReferencedNatureRelationshipModel {

private static final long serialVersionUID = 1L;

protected String          natureType;

@Override
public String getNatureType()
{

return natureType;
}

@Override
public void setNatureType(String natureType)
{
this.natureType = natureType;
}
}
