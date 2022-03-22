//require('./../libraries/translationmanager/translation-manager.js').init();
import './../libraries/saferenderer/safe-react-renderer';

import React from 'react';
import ReactDOM from 'react-dom';
import Loader from 'halogen/PulseLoader';
import { view as ValidateScreenController } from './../screens/validatescreen/controller/validate-screen-controller.jsx';
import ValidateScreenStore from './../screens/validatescreen/store/validate-screen-store';
import ValidateScreenAction from './../screens/validatescreen/action/validate-screen-action';
//var ThemeLoader = require('./../libraries/themeloader/theme-loader.js');


ReactDOM.render(<ValidateScreenController
                        store={ValidateScreenStore}
                        action={ValidateScreenAction}/>,
              document.getElementById('mainContainer'));


