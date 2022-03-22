package com.cs.core.config.interactor.entity.datarule;

public class Rule implements IRule {
  
  protected String id;
  protected String name;
  protected String script;
  protected String bytecode;
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getScript()
  {
    return script;
  }
  
  public void setScript(String script)
  {
    this.script = script;
  }
  
  public String getBytecode()
  {
    return bytecode;
  }
  
  public void setBytecode(String bytecode)
  {
    this.bytecode = bytecode;
  }
  
  @Override
  public Long getVersionId()
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
  
  @Override
  public String getLastModifiedBy()
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
}
