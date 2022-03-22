import oExceptionDictionary from './exception-dictionary';

var Exception = (function () {
  return {
    getMessage: function (oResponse, oTranslations, sAlreadyDeleted) {

      switch(oResponse.errorCode) {

      /**************** USECASE: MultiUser Bulk Delete ********************/
        case 'Todo':
          return oResponse.errorCode+": "+sAlreadyDeleted;

      /*************** attribute get: attribute is deleted already ************************/
        case 'com.cs.config.interactor.incoming.InteractorException-com.cs.config.interactor.outgoing.database.AttributeNotFoundException':
          return "[" + sAlreadyDeleted +"] "+ oTranslations.ERROR_ATTRIBUTE_IS_DELETED;

        case 'com.cs.config.interactor.incoming.InteractorException-com.cs.config.interactor.outgoing.database.RelationshipNotFoundException':
          return "[" + sAlreadyDeleted +"] "+ oTranslations.ERROR_RELATIONSHIP_NOT_FOUND;

        case 'com.cs.config.interactor.incoming.InteractorException-com.cs.config.interactor.outgoing.database.NoRelationshipDeletedException':
          //return "[" + sAlreadyDeleted +"] "+ oTranslations.ERROR_RELATIONSHIP_NOT_FOUND;
          break;

        case 'com.cs.config.interactor.incoming.InteractorException-com.cs.config.interactor.outgoing.database.TagNotFoundException':
          return "[" + sAlreadyDeleted +"] " + oTranslations.ERROR_TAG_IS_ALREADY_DELETED;

        case 'com.cs.config.interactor.incoming.InteractorException-com.cs.config.interactor.outgoing.database.ParentTagNotFoundException':
          return "[" + sAlreadyDeleted +"] "+ oTranslations.ERROR_TAG_PARENT_TAG_NOT_FOUND;

      /*************** Content create: user does not have permission to create ************************/
        case 'com.cs.runtime.interactor.exception.UserNotHaveCreatePermission':
          return oTranslations.USER_DOES_NOT_HAVE_CREATE_PERMISSION;

      /*************** Get KlassInstance: klass already deleted from Config************************/
        case 'com.cs.runtime.interactor.exception.ConfigKlassAlreadyDeleted':
          return oTranslations.ERROR_CONFIG_KLASS_NOT_FOUND;

        case 'com.cs.exception.elastic.permission.UserNotHavePermissionToAddTaxonomy':
          return oTranslations.UserNotHavePermissionToAddTaxonomy;

        case 'com.cs.exception.elastic.permission.UserNotHavePermissionToDeleteTaxonomy':
          return oTranslations.UserNotHavePermissionToDeleteTaxonomy;
        
        default ://Unhandled scenarios.
          console.error(oResponse);
          return oTranslations.ERROR_CONTACT_ADMINISTRATOR;
          // return "Error message not found";
      }
    },

    getCustomMessage: function (sCase, oTranslations, sData) {
      switch (sCase) {
        case "Attribute_already_deleted":
          return "[" + sData +"] "+ oTranslations.ERROR_ATTRIBUTE_IS_DELETED ;

        case "InstanceExistsForAttributeException":
          return "[" + sData + "] " + oTranslations[sCase];

        case "PriceContextExistsInAttributeException":
          return "[" + sData + "] " + oTranslations[sCase];

        case "Unhandled_Attribute":
          return "[" + sData +"] "+ oTranslations.ERROR_ATTRIBUTE_IS_UNHANDLED ;

        case "Tag_already_deleted":
          return "[" + sData +"] "+ oTranslations.ERROR_TAG_IS_ALREADY_DELETED ;

        case "Unhandled_Tag":
          return "[" + sData +"] "+ oTranslations.ERROR_TAG_IS_UNHANDLED ;

        case "Role_Already_Deleted":
          return "[" + sData +"] "+oTranslations.ERROR_ROLE_ALREADY_DELETED;

        case "Unhandled_Role":
          return "[" + sData +"] "+ oTranslations.ERROR_ROLE_IS_UNHANDLED;

        case "User_already_deleted":
          return "[" + sData +"] "+ oTranslations.ERROR_USER_IS_DELETED;

        case "Unhandled_User":
          return "[" + sData +"] "+ oTranslations.ERROR_USER_IS_UNHANDLED;

        case "Relationship_deleted_successfully":
          return "[" + sData +"] "+ oTranslations.RELATIONSHIP_DELETED_SUCCESSFULLY;

        case "Partial_Success_Delete_Relationship":
          return "[" + sData +"] "+ oTranslations.ERROR_RELATIONSHIP_NOT_FOUND;

        case "Partial_Failure_Delete_Relationship":
          return "[" + sData +"] "+ oTranslations.ERROR_RELATIONSHIP_CANNOT_DELETE;

        case "Partial_Failure_Linked_Relationship":
          return "[" + sData +"] "+ oTranslations.ERROR_RELATIONSHIP_LINKED;

        case "Relationship_section_not_found":
          return oTranslations.RELATIONSHIP_SECTION_NOT_FOUND;

      /*************** Content Exceptions ***********************/
        case "Already_Deleted":
          return oTranslations.ERROR_ALREADY_DELETED //+": [" + sData +"] ";
        
        case "Instance_not_found":
          return oTranslations.INSTANCE_NOT_FOUND;

        case "USER_DOES_NOT_HAVE_CREATE_PERMISSION":
          return oTranslations.USER_DOES_NOT_HAVE_CREATE_PERMISSION;

        case oExceptionDictionary.INSTANCE_NOT_FOUND:
          return oTranslations.ERROR_ALREADY_DELETED +": [" + sData +"] ";

        case "Task_not_found":
          return oTranslations.TASK_NOT_FOUND;

        case oExceptionDictionary.NOT_FOUND_EXCEPTION:
          return oTranslations.ERROR_ALREADY_DELETED //+": [" + sData +"] ";

        case oExceptionDictionary.VERSION_NOT_FOUND_EXCEPTION:
          return oTranslations.ERROR_VERSION_ALREADY_DELETED //+": [" + sData +"] ";

        case oExceptionDictionary.TYPE_CHANGED:
          return oTranslations.ERROR_TYPE_CHANGED //+": [" + sData +"] ";

        case oExceptionDictionary.ONLY_ONE_ELEMENT_ALLOWED :
          return oTranslations.MORE_THAN_ONE_ELEMENT_NOT_ALLOWED;

        case oExceptionDictionary.ARTICLE_NOT_FOUND:
          return oTranslations.ERROR_ALREADY_DELETED +": [" + sData +"] ";

        case oExceptionDictionary.EMPTY_PASSWORD:
          return oTranslations.ERROR_PASSWORD_NOT_EMPTY ;

        case oExceptionDictionary.KLASS_NOT_FOUND:
          return oTranslations.CLASS_NOT_FOUND;

        case oExceptionDictionary.ERROR_SSO_IS_DELETED:
          return oTranslations.ERROR_SSO_IS_DELETED;

        case oExceptionDictionary.ERROR_SSO_IS_UNHANDLED:
          return oTranslations.ERROR_SSO_IS_UNHANDLED;

        default ://Unhandled scenarios.
          // console.error(oResponse);
          // return oTranslations.ERROR_CONTACT_ADMINISTRATOR;
          return "Error message not found";
      }
    }
  }
})();

export default Exception;