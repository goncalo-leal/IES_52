function set(key, obj) {
  sessionStorage.setItem(key, JSON.stringify(obj));
  return true;
}

function get(key) {
  var obj = {};
  if (typeof sessionStorage.key !== "undefined") {
    obj = JSON.parse(sessionStorage.getItem(key));
  }
  return obj;
}

const SessionManager = {
  set: set,
  get: get,
}

export default SessionManager