package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;

public class Section extends AbstractSection {
  
  private static final long serialVersionUID = 1L;
  
  public Section(String id)
  {
    this.id = id;
  }
  
  public Section(String id, Integer sequence)
  {
    this.id = id;
    this.sequence = sequence;
  }
  
  public Section()
  {
  }
}
