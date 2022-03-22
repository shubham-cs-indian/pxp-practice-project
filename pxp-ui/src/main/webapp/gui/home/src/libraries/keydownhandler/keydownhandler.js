// import jQuery from 'jquery';
import EventBus from '../eventdispatcher/EventDispatcher';

var Events = {
  KEY_DOWN : 'key_down',
  CTRL_S: 'ctrl_s',
  CTRL_C: 'ctrl_c',
  CTRL_V: 'ctrl_v',
  CTRL_D: 'ctrl_d',
  SHIFT_TAB: 'shift_tab',
  TAB: 'tab',
  ESC: 'esc',
  ENTER: 'enter'
};
/*

(function($){

  $(document).ready(function(){

    window.addEventListener('keydown', onKeyDown);

    // $('body').on('keydown', )
  });

})(jQuery);
*/

var onKeyDown = function(oEvent){

  if(oEvent.dontRaise) {return}

  if (oEvent.ctrlKey || oEvent.metaKey) {
    var loaderContainer = document.getElementById('loaderContainer');
    switch (String.fromCharCode(oEvent.which).toLowerCase()) {

      case 's':
        oEvent.preventDefault();
        //TODO: Hack for blocking multiple save
        if (loaderContainer) {
          if(loaderContainer.classList.contains("loaderInVisible")) {
            EventBus.dispatch(Events.CTRL_S, this, oEvent);
          }
        }
        else {
          EventBus.dispatch(Events.CTRL_S, this, oEvent);
        }
        break;

      case 'c':
        // oEvent.preventDefault();
        EventBus.dispatch(Events.CTRL_C, this, oEvent);
        break;

      case 'v':
        // oEvent.preventDefault();
        EventBus.dispatch(Events.CTRL_V, this, oEvent);
        break;

      case 'd':
        oEvent.preventDefault();
        //TODO: Hack for blocking multiple save
        if (loaderContainer) {
          if(loaderContainer.classList.contains("loaderInVisible")) {
            EventBus.dispatch(Events.CTRL_D, this, oEvent);
          }
        }
        else {
          EventBus.dispatch(Events.CTRL_D, this, oEvent);
        }
        break;
    }
  } else if(oEvent.which == 9){
    // oEvent.preventDefault();

    if(oEvent.shiftKey){
      EventBus.dispatch(Events.SHIFT_TAB, this, oEvent);
    } else {
      EventBus.dispatch(Events.TAB, this, oEvent);
    }
  }
  else if(oEvent.which == 27){
    EventBus.dispatch(Events.ESC, this, oEvent);
  }
  else if(oEvent.which == 13){
    EventBus.dispatch(Events.ENTER, this, oEvent);
  }
};
(function() {
  window.addEventListener('keydown', onKeyDown);
})();

export const events = Events;
