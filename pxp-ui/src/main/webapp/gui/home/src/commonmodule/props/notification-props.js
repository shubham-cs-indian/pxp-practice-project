const NotificationProps = (function () {

  const Props = function () {
    return {
      bIsNotificationButtonClicked: false,
    }
  };

  let oProperties = new Props();

  return {
    setIsNotificationButtonSelected: function (_bIsNotificationButtonClicked) {
      oProperties.bIsNotificationButtonClicked = _bIsNotificationButtonClicked;
    },

    getIsNotificationButtonSelected: function () {
      return oProperties.bIsNotificationButtonClicked;
    },

    reset: function () {
      oProperties = new Props();
    }
  }
})();

export default NotificationProps;