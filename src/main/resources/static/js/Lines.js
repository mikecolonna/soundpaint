var Lines = function() {

  // template
  let groupHolder;
  const LINE_COUNT = 4;
  let vertDistance;
  const fillFactor = 0.8;
  const planeWidth = 2000;
  const segments = 10;

  function init() {

    // initialize container
    groupHolder = new THREE.Object3D();
    VizHandler.getVizHolder().add(groupHolder);
    groupHolder.position.z = 300;
    vertDistance = 1580 / LINE_COUNT;

    for (let i = 0; i < LINE_COUNT; i++) {
      let planeMaterial = new THREE.MeshBasicMaterial ({ color : 0xEBFF33});
      planeMaterial.color.setHSL(i / LINE_COUNT, 1.0, 0.5);

      const geometry = new THREE.PlaneGeometry(planeWidth, vertDistance,
        segments, segments);

      const mesh = new THREE.Mesh(geometry, planeMaterial);
      mesh.position.y = vertDistance * i - (vertDistance * LINE_COUNT) / 2;
      mesh.scale.y = (i + 1) / LINE_COUNT * fillFactor;
      groupHolder.add(mesh);
    }
  }

  function update() {
    groupHolder.position.y = AudioHandler.getBPMTime() * vertDistance;

    // scale lines on levels
    for (let i = 0; i < LINE_COUNT; i++) {
      groupHolder.children[i].scale.y =
        AudioHandler.getLevelsData()[i] * AudioHandler.getLevelsData()[i] + 0.00001;
    }
  }

  function onBeat() {}

  function onBPMBeat() {}

  return {
    init : init
  };
}();
