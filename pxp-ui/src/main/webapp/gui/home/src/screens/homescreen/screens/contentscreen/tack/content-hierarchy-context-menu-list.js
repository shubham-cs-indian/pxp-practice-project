/**
 * Created by CS49 on 11-05-2017.
 */
import ScreenModeUtils from '../store/helper/screen-mode-utils';

var getTranslation = ScreenModeUtils.getTranslationDictionary;

export default {
  CollectionHierarchyNodeContextMenuList:[
    {id: "paste", label: getTranslation().PASTE}
  ],

  CollectionThumbnailContextMenuList:[
    {id: "copy", label: getTranslation().COPY},
    {id: "cut", label: getTranslation().CUT}
  ],

  TaxonomyHierarchyNodeContextMenuList:[
    {id: "paste", label: getTranslation().PASTE}
  ],

  TaxonomyThumbnailContextMenuList:[
    {id: "copy", label: getTranslation().COPY},
    {id: "cut", label: getTranslation().CUT}
  ]
};