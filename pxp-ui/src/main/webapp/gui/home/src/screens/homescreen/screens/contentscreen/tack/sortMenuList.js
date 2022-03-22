import SortFieldTypeDictionary from './mock/mock-data-for-sort-field-dictionary';

export const sortMenuList = [
   {
     id: SortFieldTypeDictionary.NAME,
     className: 'sortByName',
     label: 'Name'
   },
   {
     id: SortFieldTypeDictionary.CREATED_ON,
     className: 'sortByCreatedOn',
     label: 'Created On'
   },
   {
     id: SortFieldTypeDictionary.LAST_MODIFIED,
     className: 'sortBylastModified',
     label: 'Last Modified'
   }
 /*  {
     id: 'type-asc',
     className: 'sortByTypeInAscendingOrder',
     label: 'Type : Ascending'
   },
   {
     id: 'type-desc',
     className: 'sortByTypeInDescendingOrder',
     label: 'Type : Descending'
   }*/
];

export const quickListSortMenuList = [
  {
    id: SortFieldTypeDictionary.RELEVANCE,
    className: 'sortByRelevance',
    label: 'Relevance'
  }
];
