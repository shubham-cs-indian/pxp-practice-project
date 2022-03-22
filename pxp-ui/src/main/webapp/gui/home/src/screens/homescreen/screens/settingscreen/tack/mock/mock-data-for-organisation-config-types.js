import CS from '../../../../../../libraries/cs';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import OrganisationTypeDictionary from '../../tack/organisation-type-id-dictionary';

export default function () {
  return (CS.sortBy([
    {
      id: OrganisationTypeDictionary.INTERNAL,
      label: getTranslation().INTERNAL
    },
    {
      id: OrganisationTypeDictionary.SUPPLIERS,
      label: getTranslation().SUPPLIER
    },
    {
      id: OrganisationTypeDictionary.MARKETPLACES,
      label: getTranslation().ORGANISATION_TYPE_MARKETPLACES
    },
    {
      id: OrganisationTypeDictionary.DISTRIBUTORS,
      label: getTranslation().ORGANISATION_TYPE_DISTRIBUTORS
    },
    {
      id: OrganisationTypeDictionary.WHOLESALERS,
      label: getTranslation().ORGANISATION_TYPE_WHOLESALERS
    },
    {
      id: OrganisationTypeDictionary.TRANSLATION_AGENCY,
      label: getTranslation().ORGANISATION_TYPE_TRANSLATION_AGENCY
    },
    {
      id: OrganisationTypeDictionary.CONTENT_ENRICHMENT_AGENCY,
      label: getTranslation().ORGANISATION_TYPE_CONTENT_ENRICHMENT_AGENCY
    },
    {
      id: OrganisationTypeDictionary.DIGITAL_ASSET_AGENCY,
      label: getTranslation().ORGANISATION_TYPE_DIGITAL_ASSET_AGENCY
    },
  ], 'label'));
};
