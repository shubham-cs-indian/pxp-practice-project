import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';
import TagTypeConstants from './tag-type-constants';

const tagTypes = function () {
  return [
    {
      "id": TagTypeConstants.YES_NEUTRAL_TAG_TYPE,
      "label": oTranslations().TAG_TYPE_YES_NEUTRAL
    },
    {
      "id": TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE,
      "label": oTranslations().TAG_TYPE_YES_NEUTRAL_NO
    },
    {
      "id": TagTypeConstants.RANGE_TAG_TYPE,
      "label": oTranslations().RANGE
    },
    {
      "id": TagTypeConstants.RULER_TAG_TYPE,
      "label": oTranslations().TAG_TYPE_RULER
    },
    {
      "id": TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE,
      "label": oTranslations().TAG_TYPE_LIFECYCLE_STATUS
    },
    {
      "id": TagTypeConstants.STATUS_TAG_TYPE,
      "label": oTranslations().STATUS
    },
    {
      "id": TagTypeConstants.TAG_TYPE_BOOLEAN,
      "label": oTranslations().TAG_TYPE_BOOLEAN
    },
    {
      "id": TagTypeConstants.TAG_TYPE_MASTER,
      "label": oTranslations().MASTER
    },
    {
      "id": TagTypeConstants.TAG_TYPE_LANGUAGE,
      "label": oTranslations().LANGUAGE
    },
    {
      "id": TagTypeConstants.LISTING_STATUS_TAG_TYPE,
      "label": oTranslations().TAG_TYPE_LISTING_STATUS
    }
  ];
};
export default tagTypes;