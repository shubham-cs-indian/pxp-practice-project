package com.cs.core.runtime.klassinstance;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.naming.InvalidNameException;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.Validations;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.exception.InvalidBaseTypeException;
import com.cs.core.runtime.interactor.model.attribute.IModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;

@Component
public class SaveInstanceValidations extends Validations {
  
  public enum Type
  {
    
    Article, Asset, Target, TextAsset, Supplier;
    
    public static final List<String> validBaseTypes = Arrays.asList(
        Constants.ARTICLE_INSTANCE_BASE_TYPE, Constants.ASSET_INSTANCE_BASE_TYPE,
        Constants.MARKET_INSTANCE_BASE_TYPE, Constants.TEXTASSET_INSTANCE_BASE_TYPE,
        Constants.SUPPLIER_INSTANCE_BASE_TYPE);
  }
  
  public void validate(IKlassInstanceSaveModel model) throws Exception
  {
    validateNameAttribute(model);
    
    if (!validBaseType(model.getBaseType())) {
      throw new InvalidBaseTypeException();
    }
  }
  
  private void validateNameAttribute(IKlassInstanceSaveModel model) throws Exception
  {
    if (isEmpty(model.getName()) && model.getModifiedAttributes().isEmpty()) {
      return;
    }
    
    if (!model.getModifiedAttributes().isEmpty()) {
      Optional<IModifiedContentAttributeInstanceModel> nameAttribute = model.getModifiedAttributes()
          .stream()
          .filter(attribute -> attribute.getAttributeId().equals(SystemLevelIds.NAME_ATTRIBUTE))
          .findFirst();
      
      if (nameAttribute.isPresent()) {
        if (isEmpty(model.getName())) {
          throw new InvalidNameException("Flat level name cannot be empty");
        }
        if (!model.getName()
            .equals(((IModifiedAttributeInstanceModel) nameAttribute.get()).getValue())) {
          throw new InvalidNameException(
              "Flat level name & Modified nameattribute instance must have same value.");
        }
      }
    }
  }
  
  private static boolean validBaseType(String baseType)
  {
    return Type.validBaseTypes.contains(baseType);
  }
  
}
