package com.cs.core.interactor.usecase.initialize.utils;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.validationontype.InvalidTagTypeException;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabModel;
import com.cs.core.config.interactor.model.klass.IProjectKlassModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.exception.EntityIdMappingNotFoundException;
import com.cs.core.exception.InvalidAbbreviationException;
import com.cs.core.exception.InvalidCodeException;
import com.cs.core.exception.InvalidEmailException;
import com.cs.core.exception.InvalidFirstNameException;
import com.cs.core.exception.InvalidIdException;
import com.cs.core.exception.InvalidIsNatureException;
import com.cs.core.exception.InvalidLocaleIdException;
import com.cs.core.exception.InvalidParentIdException;
import com.cs.core.exception.InvalidPasswordException;
import com.cs.core.exception.InvalidRelationshipSideException;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.InvalidUserNameException;

public class ValidationUtils {
  
  public static void validateAttribute(IAttribute attribute) throws Exception
  {
    validateId(attribute.getId());
    validateCode(attribute.getCode());
    validateType(attribute.getType());
  }
  
  public static void validateTag(ITag tag, boolean isChild) throws Exception
  {
    validateId(tag.getId());
    validateCode(tag.getCode());
    validateType(tag.getType());
    if (!isChild) {
      validateTagType(tag.getTagType());
    }
  }
  
  public static void validateTagTypes(ITagTypeModel tagType) throws Exception
  {
    validateId(tagType.getId());
    validateCode(tagType.getCode());
  }
  
  public static void validateOrganization(IOrganizationModel organization) throws Exception
  {
    validateId(organization.getId());
  }
  
  public static void validatePropertyCollection(IPropertyCollection propertyCollection)
      throws Exception
  {
    validateId(propertyCollection.getId());
    validateCode(propertyCollection.getCode());
  }
  
  public static void validateUser(IUserModel user) throws Exception
  {
    validateId(user.getId());
    validateCode(user.getCode());
    validateType(user.getType());
    validateUserName(user.getUserName());
    validatePassword(user.getPassword());
    validateFirstName(user.getFirstName());
    validateEmail(user.getEmail());
  }
  
  public static void validateSystem(ICreateSystemModel system) throws Exception
  {
    validateId(system.getId());
  }
  
  public static void validateLanguage(ILanguageModel languageModel) throws Exception
  {
    validateId(languageModel.getId());
    validateCode(languageModel.getCode());
    validateLocaleId(languageModel.getLocaleId());
    validateAbbreviation(languageModel.getAbbreviation());
  }
  
  public static void validateTab(ICreateTabModel tab) throws Exception
  {
    validateId(tab.getId());
    validateCode(tab.getCode());
  }
  
  public static void validateRelationship(IRelationshipModel relationship) throws Exception
  {
    validateId(relationship.getId());
    validateCode(relationship.getCode());
    validateRelationshipSide(relationship.getSide1());
    validateRelationshipSide(relationship.getSide2());
  }
  
  public static void validateDashboardTab(IDashboardTabModel dashboardTab) throws Exception
  {
    validateId(dashboardTab.getId());
  }
  
  public static void validateRole(IRole role) throws Exception
  {
    validateId(role.getId());
    validateCode(role.getCode());
    validateCode(role.getType());
  }
  
  public static void validatePIMKlass(IProjectKlassModel klass) throws Exception
  {
    validateId(klass.getId());
    validateCode(klass.getCode());
    validateParentId(klass.getParent()
        .getId());
    validateIsNature(klass.getIsNature());
    validateType(klass.getNatureType());
  }
  
  public static void validateAssetKlass(IAssetModel klass) throws Exception
  {
    validateId(klass.getId());
    validateCode(klass.getCode());
    validateParentId(klass.getParent()
        .getId());
    validateIsNature(klass.getIsNature());
    validateType(klass.getNatureType());
  }
  
  public static void validateSupplierKlass(ISupplierModel klass) throws Exception
  {
    validateId(klass.getId());
    validateCode(klass.getCode());
    validateParentId(klass.getParent()
        .getId());
    validateIsNature(klass.getIsNature());
    validateType(klass.getNatureType());
  }
  
  public static void validateTargetKlass(ITargetModel klass) throws Exception
  {
    validateId(klass.getId());
    validateCode(klass.getCode());
    validateParentId(klass.getParent()
        .getId());
    validateIsNature(klass.getIsNature());
  }
  
  public static void validateTextAssetKlass(ITextAssetModel klass) throws Exception
  {
    validateId(klass.getId());
    validateCode(klass.getCode());
    validateParentId(klass.getParent()
        .getId());
  }
  
  public static void validateSmartDocument(ISmartDocumentModel smartDocument) throws Exception
  {
    validateId(smartDocument.getId());
    validateCode(smartDocument.getCode());
  }
  
  public static void validateVariantContext(IVariantContext context) throws Exception
  {
    validateId(context.getId());
    validateCode(context.getCode());
    validateType(context.getType());
  }
  
  public static void validateId(String id) throws InvalidIdException
  {
    if (id == null || id.isEmpty()) {
      throw new InvalidIdException();
    }
  }
  
  public static void validateCode(String code) throws InvalidCodeException
  {
    if (code == null || code.isEmpty()) {
      throw new InvalidCodeException();
    }
  }
  
  public static void validateType(String type) throws InvalidTypeException
  {
    if (type == null) {
      throw new InvalidTypeException();
    }
  }
  
  public static void validateParentId(String parentId) throws InvalidParentIdException
  {
    if (parentId == null || parentId.isEmpty()) {
      throw new InvalidParentIdException();
    }
  }
  
  public static void validateIsNature(Boolean isNature) throws InvalidIsNatureException
  {
    if (isNature == null) {
      throw new InvalidIsNatureException();
    }
  }
  
  public static void validateTagType(String tagType) throws InvalidTagTypeException
  {
    if (tagType == null || tagType.isEmpty()) {
      throw new InvalidTagTypeException();
    }
  }
  
  public static void validateUserName(String username) throws InvalidUserNameException
  {
    if (username == null || username.isEmpty()) {
      throw new InvalidUserNameException();
    }
  }
  
  public static void validatePassword(String password) throws InvalidPasswordException
  {
    if (password == null || password.isEmpty()) {
      throw new InvalidPasswordException();
    }
  }
  
  public static void validateFirstName(String firstName) throws InvalidFirstNameException
  {
    if (firstName == null || firstName.isEmpty()) {
      throw new InvalidFirstNameException();
    }
  }
  
  public static void validateEmail(String email) throws InvalidEmailException
  {
    if (email == null || email.isEmpty()) {
      throw new InvalidEmailException();
    }
  }
  
  public static void validateLocaleId(String localeId) throws InvalidLocaleIdException
  {
    if (localeId == null || localeId.isEmpty()) {
      throw new InvalidLocaleIdException();
    }
  }
  
  public static void validateAbbreviation(String abbreviation) throws InvalidAbbreviationException
  {
    if (abbreviation == null || abbreviation.isEmpty()) {
      throw new InvalidAbbreviationException();
    }
  }
  
  public static void validateRelationshipSide(IRelationshipSide side)
      throws InvalidRelationshipSideException
  {
    if (side == null || side.getCardinality() == null || side.getCardinality()
        .isEmpty() || side.getKlassId() == null || side.getKlassId()
            .isEmpty()) {
      throw new InvalidRelationshipSideException();
    }
  }
  
  public static void validateEntityIdMapping(String side1Entity, String side2Entity)
      throws EntityIdMappingNotFoundException
  {
    if (side1Entity == null || side2Entity == null) {
      throw new EntityIdMappingNotFoundException();
    }
  }
}
