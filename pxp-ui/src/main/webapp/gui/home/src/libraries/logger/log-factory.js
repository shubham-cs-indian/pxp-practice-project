import Logger from './logger';

var LogFactory = (function () {

  return {
    getLogger: function (className) {
      return new Logger(className, null);
    }
  };
})();

export default LogFactory;
