import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ContextViewDictionary from '../../../../../../commonmodule/tack/context-view-dictionary.js';

const contextViews = function () {
  return [
    {
      id: ContextViewDictionary.THUMBNAIL_VIEW,
      label: oTranslations().THUMBNAIL_VIEW
    },
    {
      id: ContextViewDictionary.TABULAR_VIEW,
      label: oTranslations().TABULAR_VIEW
    }
  ];
};

export default contextViews;