package com.cs.core.config.businessapi.tabs;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IModifiedTabPropertySequenceModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.exception.InvalidModifiedTabSequenceNumber;
import com.cs.core.exception.InvalidNewSequenceNumber;

@Component
public class TabValidations extends Validations
{
  
  public void validate(ICreateTabModel model) throws Exception
  {
    validate(model.getCode(), model.getLabel());
  }
  
  public void validate(IListModel<ISaveTabModel> model) throws Exception
  {
    for(ISaveTabModel dataModel : model.getList())
    {
      validate(dataModel.getCode(), dataModel.getLabel());
      validateModifiedTabSequence(dataModel.getModifiedTabSequence());
    }
  }
  
  public void validate(ISaveTabModel model) throws Exception
  {
    validate(model.getId(), model.getLabel());
    
    for(IModifiedTabPropertySequenceModel dataModel : model.getModifiedPropertySequence())
    {
      if(dataModel.getNewSequence() == null || dataModel.getNewSequence() < 0)
      {
        throw new InvalidNewSequenceNumber();
      }
    }
  }
  
  public void validateModifiedTabSequence(Integer modifiedTabSequence)
      throws InvalidModifiedTabSequenceNumber
  {
    if(modifiedTabSequence == null || modifiedTabSequence < 0)
    {
      throw new InvalidModifiedTabSequenceNumber();
    }
  }
}
