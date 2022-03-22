package com.cs.di.runtime.entity.dao;

public interface IEntityViolationDAO {
  
  public boolean checkViolation( Long baseentityiid, int violationCode);

}
