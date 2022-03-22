import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const quickLinkItemList = function () {
  return {
    linkView1: [
      {
        id: 6,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig1',
        title: oTranslations().BASIC_INFORMATION
      },
      {
        id: 1,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig2',
        title: oTranslations().SECTION
      },
      /* {
         id: 2,
         name: 'classViewEditor',
         isActive: false,
         class: 'linkIcon classConfig2',
         title: 'Content Structure'
       },*/
      {
        id: 3,
        name: 'classViewPermission',
        isActive: false,
        class: 'linkIcon classConfig3',
        title: oTranslations().PERMISSION
      },
      /*{
        id: 4,
        name: 'classViewSettings',
        isActive: false,
        class: 'linkIcon classConfig3',
        title: 'Settings'
      },*/
      /*{
        id: 5,
        name: 'classViewSettings',
        isActive: false,
        class: 'linkIcon classConfig4',
        title: oTranslations().NOTIFICATION
      },*/
      {
        id: 7,
        name: 'classViewSettings',
        isActive: false,
        class: 'linkIcon classConfig4',
        title: oTranslations().RULES
      },
      {
        id: 8,
        name: 'classViewSettings',
        isActive: false,
        class: 'linkIcon classConfig5',
        title: oTranslations().CONTEXT
      }
    ],
    linkView2: [
      {
        id: 6,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig1',
        title: oTranslations().BASIC_INFORMATION
      },
      {
        id: 1,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig2',
        title: oTranslations().SECTION
      },
      {
        id: 3,
        name: 'classViewPermission',
        isActive: false,
        class: 'linkIcon classConfig3',
        title: oTranslations().PERMISSION
      },
      {
        id: 7,
        name: 'classViewSettings',
        isActive: false,
        class: 'linkIcon classConfig4',
        title: oTranslations().RULES
      },
      {
        id: 8,
        name: 'classViewSettings',
        isActive: false,
        class: 'linkIcon classConfig5',
        title: oTranslations().CONTEXT
      }
    ],
    propertyCollectionView: [
      {
        id: 1,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig1',
        title: oTranslations().SECTION
      },
    ],
    editorialLinkView: [
      {
        id: 1,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig1',
        title: oTranslations().BASIC_INFORMATION
      },
      {
        id: 2,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig2',
        title: oTranslations().SECTION
      },
      {
        id: 3,
        name: 'classViewPermission',
        isActive: false,
        class: 'linkIcon classConfig3',
        title: oTranslations().PERMISSION
      },
      {
        id: 4,
        name: 'classViewSettings',
        isActive: false,
        class: 'linkIcon classConfig4',
        title: oTranslations().SETTINGS
      }/*,
    {
      id: 7,
      name: 'classViewSettings',
      isActive: false,
      class: 'linkIcon classConfig5',
      title: 'Rules'
    }*/
    ],
    editorialCollectionLinkView: [
      {
        id: 1,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig1',
        title: oTranslations().BASIC_INFORMATION
      },
      {
        id: 2,
        name: 'classViewSections',
        isActive: true,
        class: 'linkIcon classConfig2',
        title: oTranslations().SECTION
      },
      {
        id: 3,
        name: 'classViewPermission',
        isActive: false,
        class: 'linkIcon classConfig3',
        title: oTranslations().PERMISSION
      }/*,
    {
      id: 7,
      name: 'classViewSettings',
      isActive: false,
      class: 'linkIcon classConfig4',
      title: 'Rules'
    }*/
    ]
  };
};

export default quickLinkItemList;
