package com.cs.core.config.klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.Validations;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.validationontype.InvalidNatureTypeKlassException;
import com.cs.core.config.interactor.exception.validationontype.InvalidNumberOfVersionToMaintainException;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NatureKlassCannotBeDefaultAbstractException;

@Component
public class KlassValidations extends Validations {

  public enum Type {

    Klass, Asset, VirtualCatalog, Target, TextAsset, Supplier;

    public static final List<String> validTypes = Arrays.asList(SystemLevelIds.PROJECT_KLASS, SystemLevelIds.ASSET_KLASS,
        SystemLevelIds.MARKET_KLASS, CommonConstants.TEXT_ASSET_KLASS_TYPE,
        CommonConstants.SUPPLIER_KLASS_TYPE);
    
    public static final List<String> DEFAULT_ABSTRACT_NATURE_TYPES = Arrays.asList(
        CommonConstants.GTIN_KLASS_TYPE, CommonConstants.EMBEDDED_KLASS_TYPE,
        CommonConstants.SUPPLIER_NATURE_TYPE);
    
    public static final List<String> ROOT_KLASS_IDS                = Arrays.asList(
        SystemLevelIds.FILE, SystemLevelIds.ATTACHMENT, SystemLevelIds.SUPPLIER,
        SystemLevelIds.MARKET, SystemLevelIds.TEXT_ASSET);

    public static List<String> getNatureTypesByType(String type)
    {
      switch (type) {
        case SystemLevelIds.PROJECT_KLASS:
          return Arrays.asList("singleArticle", "pidSku", "embedded", "fixedBundle", "setOfProducts", "gtin");
        case SystemLevelIds.ASSET_KLASS:
          return Arrays.asList("imageAsset", "documentAsset", "embedded", "technicalImage", "videoAsset", "fileAsset");
        case SystemLevelIds.MARKET_KLASS:
          return Arrays.asList("market", "embedded");
        case CommonConstants.TEXT_ASSET_KLASS_TYPE:
          return Arrays.asList("textAsset", "embedded");
        case CommonConstants.SUPPLIER_KLASS_TYPE:
          return Arrays.asList("supplier");
          default:
          return new ArrayList<>();
      }
    }
  }

  public void validate(IKlassModel model) throws Exception
  {
    validate(model.getCode(), model.getLabel());
    
    if (!validType(model.getType())) {
      throw new InvalidTypeException();
    }

    if (model.getIsNature() != null && model.getIsNature()) {
      if (!validateNatureType(model.getType(), model.getNatureType())) {
        throw new InvalidNatureTypeKlassException();
      }
    }
    if (model.getNumberOfVersionsToMaintain() != null) {
      if(validateNumberOfVersionToMaintain(model)){
        throw new InvalidNumberOfVersionToMaintainException();
      }
    }
  }
  
  protected static boolean validateNumberOfVersionToMaintain(IKlassModel model)
  {
    return model.getNumberOfVersionsToMaintain() < 0;
  }

  private static boolean validType(String type)
  {
    return Type.validTypes.contains(type);
  }

  private static boolean validateNatureType(String type, String natureType)
  {
    return Type.getNatureTypesByType(type).contains(natureType);
  }

  public void validate(IKlassSaveModel model) throws Exception
  {
    validate((IKlassModel) model);
    validateDefaultAbstract(model);
  }
  
  private void validateDefaultAbstract(IKlassSaveModel model)
      throws NatureKlassCannotBeDefaultAbstractException
  {
    if (Type.DEFAULT_ABSTRACT_NATURE_TYPES.contains(model.getNatureType())) {
      if (model.getIsDefaultChild() || model.getIsAbstract()) {
        throw new NatureKlassCannotBeDefaultAbstractException(
            "Standard Identifier, Embedded And Supplier (Nature type Klasses) are neither default nor abstract.");
      }
      
      return;
    }
    
    if (Type.ROOT_KLASS_IDS.contains(model.getCode())) {
      if (!(model.getIsDefaultChild() && model.getIsAbstract())) {
        throw new NatureKlassCannotBeDefaultAbstractException(
            "File, Attachment (Nature Klasses), Market, Text Asset, Business Partner/Supplier, Virtual Catalog (Non-Nature Klasses) are default as well abstract.");
      }
      
      return;
    }
    
    if (model.getIsNature()) {
      if (model.getIsDefaultChild() && model.getIsAbstract()) {
        throw new NatureKlassCannotBeDefaultAbstractException(
            "Nature Klass can be either Abstract / Default");
      }
    }
    else {
      if (model.getIsDefaultChild()) {
        throw new NatureKlassCannotBeDefaultAbstractException(
            "Non Nature Klass has only abstract option");
      }
    }
  }
  
}
