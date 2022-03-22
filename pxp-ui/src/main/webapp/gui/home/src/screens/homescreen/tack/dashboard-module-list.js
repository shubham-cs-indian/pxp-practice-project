import MenuDictionary from './../../../screens/homescreen/tack/menu-dictionary';
import CommonAlienViewRequestUrl from '../../../commonmodule/tack/common-alien-view-request-urls';

export default [
  {
    id: MenuDictionary[1],
    label: 'PIM',
    className: 'moduleItem contentMenuItem',
    title: 'PIM',
    isSelected: true,
    isVisible: true,
    url: CommonAlienViewRequestUrl.RUNTIME_DASHBOARD_PIM_URL
  },
  {
    id: MenuDictionary[2],
    label: 'MAM',
    className: 'moduleItem mamMenuItem',
    title: 'MAM',
    isSelected: false,
    isVisible: true,
    url: CommonAlienViewRequestUrl.RUNTIME_DASHBOARD_MAM_URL
  }
];