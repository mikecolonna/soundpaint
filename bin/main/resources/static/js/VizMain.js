const VizMain = function() {

  let canvas;

  function init() {
    canvas = document.getElementById('canvas');

    canvas.addEventListener('drop', onDocumentDrop, false);
    //canvas.addEventListener('dragover', onDocumentDragOver, false);

    AudioHandler.init();

  }

  function onDocumentDrop(evt) {
    evt.stopPropagation();
    evt.preventDefault();
    AudioHandler.onAudioDrop(evt);
  }
};
