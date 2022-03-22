import './../libraries/saferenderer/safe-react-renderer';

import React from 'react';
import ReactDOM from 'react-dom';
// var Loader = require('halogen/PulseLoader');
import { view as PocScreenController } from './../screens/POCscreen/controller/poc-screen-controller';

import PocScreenStore from './../screens/POCscreen/store/poc-screen-store';
import PocScreenAction from './../screens/POCscreen/action/poc-screen-action';
// var ThemeLoader = require('./../libraries/themeloader/theme-loader.js');

// require("react-tap-event-plugin")();

ReactDOM.render(<PocScreenController
                        store={PocScreenStore}
                        action={PocScreenAction}/>,
              document.getElementById('mainContainer'));

/*ReactDOM.render(<Loader color="#26A65B"/>,
              document.getElementById('loaderContainer'));*/

// ThemeLoader.loadTheme();


