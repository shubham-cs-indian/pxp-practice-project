//require('./../libraries/translationmanager/translation-manager.js').init();
import './../libraries/saferenderer/safe-react-renderer';

import React from 'react';
import ReactDOM from 'react-dom';
import Loader from 'halogen/PulseLoader';
import { view as ShopScreenController } from './../screens/shopscreen/controller/shop-screen-controller.jsx';
import ShopScreenStore from './../screens/shopscreen/store/shop-screen-store';
import ShopScreenAction from './../screens/shopscreen/action/shop-screen-action';
import ThemeLoader from './../libraries/themeloader/theme-loader.js';

//require('./../libraries/alertify/alertify-dialog');

ReactDOM.render(<ShopScreenController
                        store={ShopScreenStore}
                        action={ShopScreenAction}/>,
              document.getElementById('mainContainer'));

ReactDOM.render(<Loader color="#26A65B"/>,
              document.getElementById('loaderContainer'));

ThemeLoader.loadTheme();


