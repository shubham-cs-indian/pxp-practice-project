
export const homeScreenRequestMapping = {
  'GetTag': 'config/tags',
  'GetAllTags': 'config/tags/<%=id%>?mode=<%=mode%>',
  'GetKlass': 'config/classes',
  'GetAttributes': 'config/attributes',
  'GetContentType': 'content/type/get',
  'GetContentStatus': 'content/status/get',
  'GetClass': 'config/classes/<%=id%>',
  'Logout': 'logout',
  'GetCurrentUser':'config/currentUser',
  'GetLanguageInfo':'config/defaultandsupportedlang',
  'checkUserAvailability': 'config/checkUsers',
  'saveUser': 'config/users',
  'ResetPassword': 'config/users/resetpassword',
  'getUserById':  'config/users/<%=id%>',
  'GetCurrencyExchangeRates': 'config/exchangerates',
  'GetAllNotifications' : 'runtime/taskinstances/gettaskcount/<%=taskId%>',
  'GetThemeConfigurations':'config/themeconfigurations',
  'GetViewConfigurations':'config/viewconfigurations',
  'GetPhysicalCatalogIds':'config/getphyicalCatalogIds',
  'GetPortals':'config/getportals'
};