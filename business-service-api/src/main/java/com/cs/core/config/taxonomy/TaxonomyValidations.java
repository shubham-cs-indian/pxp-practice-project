package com.cs.core.config.taxonomy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.exception.validationontype.InvalidTaxonomyTypeException;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;
import com.cs.core.exception.InvalidCodeException;
import com.cs.core.exception.InvalidParentIdException;

public class TaxonomyValidations extends Validations {

  enum TaxonomyType {
    majorTaxonomy, minorTaxonomy;
    static List<String> types = Arrays.stream(values()).map(Enum::name).collect(Collectors.toList());
    static List<String> getTypes() {
      return types;
    }
  }

  public static void validate(ICreateMasterTaxonomyModel createModel) throws Exception
  {
    validateTaxonomy(createModel.getCode(), createModel.getLabel() , createModel.getTaxonomyType(), createModel.getParentTaxonomyId());
  }

  protected static void validateTaxonomy(String code, String label, String type, String parentId) throws Exception
  {
    if (!isCodeValid(code)) {
      throw new InvalidCodeException();
    }
    validateLabel(label);
    if (isEmpty(parentId)) {
      throw new InvalidParentIdException();
    }
    if(parentId.equals("-1")){
      if (!isTaxonomyTypeValid(type)) {
        throw new InvalidTaxonomyTypeException();
      }
    }

  }

  private static boolean isTaxonomyTypeValid(String taxonomyType)
  {
    return TaxonomyType.getTypes().contains(taxonomyType);
  }

  public static void validate(ISaveMasterTaxonomyModel createModel) throws Exception
  {
    validateLabel(createModel.getLabel());
  }
}
