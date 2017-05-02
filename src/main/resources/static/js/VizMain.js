//const events = new Events();

const VizMain = function() {

  let canvas;

  function init() {
    canvas = document.getElementById('canvas');

    //canvas.addEventListener('drop', onDocumentDrop, false);
    //canvas.addEventListener('dragover', onDocumentDragOver, false);

    AudioHandler.init();
    VizHandler.init();
  }

  function update() {
		requestAnimationFrame(update);
		VizMain.emit("update");
	}

  return {
    init : init
  };

}();

$(document).ready(function() {
	VizMain.init();
});
