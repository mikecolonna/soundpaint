$(document).ready(() => {
  var ctx = new AudioContext();
  var audio = document.getElementById('myAudio');
  var audioSrc = ctx.createMediaElementSource(audio);
  var analyser = ctx.createAnalyser();

  var dataArray = new Uint8Array(analyser.frequencyBinCount);
  //analyser.getByteTimeDomainData(dataArray);

  audioSrc.connect(analyser);
  audioSrc.connect(ctx.destination);
  var frequencyData = new Uint8Array(analyser.frequencyBinCount);
  var scene, renderer, camera;
  var fov, zoom, inc;

  function init() {
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, .1, 500);
    camera.position.set(0, 0, 100);
    camera.lookAt(new THREE.Vector3(0, 0, 0));
    //camera.position.z = 2;

    renderer = new THREE.WebGLRenderer( { canvas : document.getElementById('canvas') } );
    renderer.setSize(window.innerWidth, window.innerHeight);
    document.body.appendChild(renderer.domElement);

    scene = new THREE.Scene();

    addCubes();

    fov = camera.fov;
    zoom = 1.0;
    inc = -0.001;
  }

  function addCubes() {
    var x = -100;
    var y = 0;
    var z = 0;

    for (var i = 0; i < 1000; i++) {
      var cubeGeometry = new THREE.BoxGeometry(3, 3, 3);
      var cubeMaterial = new THREE.MeshNormalMaterial({color:frequencyData[i]*0xff3300, wireframe : true});
      var cube = new THREE.Mesh(cubeGeometry, cubeMaterial);
      cube.castShadow = true;
      cube.receiveShadow = true;
      cube.name = frequencyData.length;
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
  }

  function render() {
    scene.traverse(function (e) {
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
    analyser.getByteFrequencyData(frequencyData);
  }

  function animate() {
    requestAnimationFrame(animate);
    render();

    renderer.render(scene, camera);
  }

  init();
  animate();
  audio.play();
});
