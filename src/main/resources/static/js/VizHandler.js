const Visualizer = function() {
  let camera, renderer, scene;
  let vizHolder;
  let fov, zoom, inc;

  // data with which to manipulate visuals
  let frequencyData;

  function init() {
    Visualizer.on("update", update);

    // CAMERA
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, .1, 500);
    camera.position.set(0, 0, 100);
    camera.lookAt(new THREE.Vector3(0, 0, 0));

    // RENDERER
    renderer = new THREE.WebGLRenderer( { canvas : document.getElementById('canvas') } );
    renderer.setSize(window.innerWidth, window.innerHeight);

    // SCENE
    scene = new THREE.Scene();
    scene.fog = new THREE.Fog(0x000000, 2000, 3000);

    // INIT VISUALS
    vizHolder = new THREE.Object3D();
    scene.add(vizHolder);
    Lines.init();

    /*addCubes();

    fov = camera.fov;
    zoom = 1.0;
    inc = -0.001;*/
  }

  function setFrequencyData(data) {
    frequencyData = data;
  }

  /*function addCubes() {
    var x = -100;
    var y = 0;
    var z = 0;

    for (var i = 0; i < 1000; i++) {
      var cubeGeometry = new THREE.BoxGeometry(3, 3, 3);
      var cubeMaterial = new THREE.MeshNormalMaterial({color:frequencyData[i]*0xff3300, wireframe : true});
      var cube = new THREE.Mesh(cubeGeometry, cubeMaterial);
      cube.castShadow = true;
      cube.receiveShadow = true;
      //cube.name = frequencyData.length;
      cube.position.x = x;

      x += 10;

      if (x == 100) {
        z += 10;
        x = -100;
      } else if (z == 100) {
        x = 0;
        y += 10;
        z = 0;
      }

      cube.position.y = y;
      cube.position.z = z;
      scene.add(cube);
    }
  }*/

  function render() {
    /*scene.traverse(function (e) {
      if (e instanceof THREE.Mesh) {
        //e.rotation.x += frequencyData[e.id]/5000;
        e.rotation.y += frequencyData[e.id]/1000;
        //e.rotation.z += frequencyData[e.id]/10000;
        var color = new THREE.Color(1, 0, 0);
        var r = 0;
        var b = 0;
        var g = frequencyData[e.id]/255;

        //e.material.color.setRGB(1, 0, 0);
      }
    });

    camera.fov = fov * zoom;
    camera.updateProjectionMatrix();
    zoom += inc;
    if ( zoom <= 0.1*(frequencyData[20]/100) || zoom >= 1*(frequencyData[20]/100) ){
      inc = -inc;
    }

    analyser.getByteTimeDomainData(dataArray);
    analyser.getByteFrequencyData(frequencyData);*/
  }

  function animate() {
    requestAnimationFrame(animate);
    render();

    renderer.render(scene, camera);
  }

  return {
    init: init,
    update: update,
    getVizHolder: function() { return vizHolder;},
  };

};
