package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.model.configdetails.IRelevanceCountInformationModel;

public class RelevanceCountInformationModel implements IRelevanceCountInformationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected int             to;
  protected int             from;
  protected int             relevance;
  protected long            count;
  
  @Override
  public int getTo()
  {
    return to;
  }
  
  @Override
  public void setTo(int to)
  {
    this.to = to;
  }
  
  @Override
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public int getRelevance()
  {
    return relevance;
  }
  
  @Override
  public void setRelevance(int relevance)
  {
    this.relevance = relevance;
  }
  
  @Override
  public long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(long count)
  {
    this.count = count;
  }
}
