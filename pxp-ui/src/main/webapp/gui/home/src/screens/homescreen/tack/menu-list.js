import MenuDictionary from './menu-dictionary';

const aModuleList = [
  {
    id: MenuDictionary.dashboard,
    className: 'menuItem dashboardMenuItem',
    title: 'DASHBOARD',
    type: 'alien',
    isSelected: false,
    isVisible: true
  },
  // {
  //   id: MenuDictionary.taskDashboard,
  //   className: 'menuItem taskDashboardMenuItem',
  //   title: 'DASHBOARD',
  //   type: 'taskDashboard',
  //   isSelected: false,
  //   isVisible: true,
  // },
  {
    id: MenuDictionary.dashboard2,
    className: 'menuItem dashboardMenuItem',
    title: 'DASHBOARD_TITLE',
    type: 'dashboard2',
    isSelected: false,
    isVisible: true,
  },
  {
    id: MenuDictionary.runtime,
    className: 'menuItem contentMenuItem',
    type: 'home',
    isSelected: false
  },
  /*{
    id: MenuDictionary[4],
    name: 'Editorial',
    className: 'menuItem editorialMenuItem',
    title: 'EDITORIAL_SCREEN_MENU_ITEM_TITLE',
    isSelected: false
  },
  {
    id: MenuDictionary[6],
    name: 'Export',
    className: 'menuItem exportMenuItem',
    title: 'EXPORT_SCREEN_MENU_ITEM_TITLE',
    isSelected: false,
    isVisible: true
  },*/
  /*{
    id: MenuDictionary[4],
    name: 'Editorial',
    className: 'menuItem editorialMenuItem',
    title: 'EDITORIAL_SCREEN_MENU_ITEM_TITLE',
    isSelected: false,
    isVisible: false
   },*/
  /*{
    id: 'advertisingMenuItem',
    name: 'Advertising',
    className: 'menuItem advertisingMenuItem',
    title: 'ADVERTISING_MENU_ITEM_TITLE',
    isSelected: false
    isVisible: false
   },
  {
    id: 'shareMenuItem',
    name: 'Share',
    className: 'menuItem shareMenuItem',
    title: 'SHARE_MENU_ITEM_TITLE',
    isSelected: false
    isVisible: false
   },
  {
    id: 'bookmarkMenuItem',
    name: 'Bookmark',
    className: 'menuItem bookmarkMenuItem',
    title: 'BOOKMARK_MENU_ITEM_TITLE',
    isSelected: false
    isVisible: false
   },
  {
    id: 'openInBrowserMenuItem',
    name: 'OpenInBrowser',
    className: 'menuItem openInBrowserMenuItem',
    title: 'OPEN_IN_BROWSER_MENU_ITEM_TITLE',
    isSelected: false
    isVisible: false
   },*/
  {
    id: MenuDictionary.setting,
    className: 'menuItem settingsMenuItem',
    type: 'setting',
    isSelected: false
  }
];

const aMenuList = [
  {
    id: MenuDictionary.all,
    name: 'All',
    className: 'menuItem contentMenuItem',
    title: 'ALL',
    isSelected: true,
    isVisible: true
  },
  {
    id: MenuDictionary[1],
    name: 'Content',
    className: 'menuItem contentMenuItem',
    title: 'PIM',
    isSelected: false,
    isVisible: false
  },
  {
    id: MenuDictionary[2],
    name: 'MAM',
    className: 'menuItem mamMenuItem',
    title: 'MAM',
    isSelected: false,
    isVisible: false
  },
  {
    id: MenuDictionary[3],
    name: 'Target',
    className: 'menuItem targetMenuItem',
    title: 'TARGET',
    isSelected: false,
    isVisible: false
  }
];

export default {
  moduleList: aModuleList,
  menuList: aMenuList
};