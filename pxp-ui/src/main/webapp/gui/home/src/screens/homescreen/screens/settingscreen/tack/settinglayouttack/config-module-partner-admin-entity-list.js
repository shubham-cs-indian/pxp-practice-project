import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oModulesDictionary from "./config-modules-dictionary";

let aPartnerAdminEntityList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_ORGANISATION,
      label: getTranslations().PARTNERS,
      children: [/**No Children*/],
      className: "actionItemOrganisationConfig",
      parentId: oModulesDictionary.PARTNER_ADMIN
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_USER,
      label: getTranslations().USERS,
      children: [/**No Children*/],
      className: "actionItemUser",
      parentId: oModulesDictionary.PARTNER_ADMIN
    }
  ];
};

export default aPartnerAdminEntityList;
