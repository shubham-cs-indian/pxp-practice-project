import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';
import TagTypeConstants from './tag-type-constants';

const tagTypes = function () {
  return {
    LOV: [
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
      }
    ],

    STATUS: [
      {
        "id": TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE,
        "label": oTranslations().TAG_TYPE_LIFECYCLE_STATUS
      },
      {
        "id": TagTypeConstants.STATUS_TAG_TYPE,
        "label": oTranslations().STATUS
      },
      {
        "id": TagTypeConstants.LISTING_STATUS_TAG_TYPE,
        "label": oTranslations().TAG_TYPE_LISTING_STATUS
      }
    ],
  };
};
export default tagTypes;