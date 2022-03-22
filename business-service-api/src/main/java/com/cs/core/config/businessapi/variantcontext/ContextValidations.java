package com.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.exception.InvalidCodeException;

import javax.naming.InvalidNameException;

public class ContextValidations extends Validations {

  public static void validateContext(ISaveVariantContextModel saveVariantContextModel) throws Exception
  {
    if (isEmpty(saveVariantContextModel.getLabel())) {
      throw new InvalidNameException();
    }
  }

  public static void validateContext(ICreateVariantContextModel createVariantContextModel) throws Exception
  {
    if (!isCodeValid(createVariantContextModel.getCode())) {
      throw new InvalidCodeException();
    }
    if (isEmpty(createVariantContextModel.getLabel())) {
      throw new InvalidNameException();
    }
  }

  public static void validateContext(IGridEditVariantContextInformationModel saveVariantContextModel) throws Exception
  {
    if (isEmpty(saveVariantContextModel.getLabel())) {
      throw new InvalidNameException();
    }
  }
}
