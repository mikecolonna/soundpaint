  //var ctx = new AudioContext();
  /*var audio = document.getElementById('myAudio');
  var audioSrc = ctx.createMediaElementSource(audio);
  var analyser = ctx.createAnalyser();

  audioSrc.connect(analyser);
  audioSrc.connect(ctx.destination);
  var frequencyData = new Uint8Array(analyser.frequencyBinCount);*/


  var camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, .1, 500);
  camera.position.set(0, 0, 100);
  camera.lookAt(new THREE.Vector3(0, 0, 0));
  camera.position.z = 2;

  var renderer = new THREE.WebGLRenderer();
  renderer.setSize(window.innerWidth, window.innerHeight);
  document.body.appendChild(renderer.domElement);

  var scene = new THREE.Scene();

  /*var x = 0;
  var y = 0;
  var z = 0;

  for (var i = 0; i < 1000; i++) {
    var cubeGeometry = new THREE.BoxGeometry(3, 3, 3);
    var cubeMaterial = new THREE.MeshBasicMaterial({color:frequencyData[i]*0xff3300});
    var cube = new THREE.Mesh(cubeGeometry, cubeMaterial);
    cube.castShadow = true;
    cube.receiveShadow = true;
    cube.name = frequencyData.length;
    cube.position.x = x;

    x += 10;

    if (x == 100) {
      z += 10;
      x = 0;
    } else if (z == 100) {
      x = 0;
      y += 10;
      z = 0;
    }

    cube.position.y = y;
    cube.position.z = z;
    scene.add(cube);
  }*/

  var geometry = new THREE.BoxGeometry(1, 1, 1);
  var material = new THREE.MeshBasicMaterial({color : 0x00FF00, wireframe : true});
  var cube = new THREE.Mesh(geometry, material);
  scene.add(cube);

  /*var material = new THREE.LineBasicMaterial({color : 0x0000ff});
  var geometry = new THREE.Geometry();
  geometry.vertices.push(new THREE.Vector3(-10, 0, 0));
  geometry.vertices.push(new THREE.Vector3(0, 10, 0));
  geometry.vertices.push(new THREE.Vector3(10, 0, 0));

  var line = new THREE.Line(geometry, material);

  scene.add(line);*/

  /*var MAX_POINTS = 500;

  var geometry = new THREE.BufferGeometry();

  var positions = new Float32Array(MAX_POINTS * 3);
  geometry.addAttribute('position', new THREE.BufferAttribute(positions, 3));

  var drawCount = 2;
  geometry.setDrawRange(0, drawCount);

  var material = new THREE.LineBasicMaterial({color:0xff0000, linewidth:2});

  var line = new THREE.Line(geometry, material);
  scene.add(line);

  var positions = line.geometry.attributes.position.array;

  var x = y = z = index = 0;

  line.geometry.attributes.position.needsUpdate = true;*/

  function render() {
    /*scene.traverse(function (e) {
      if (e instanceof THREE.Mesh) {
        e.rotation.x += frequencyData[50]/1000;
        e.rotation.y = frequencyData[e.id]/50;
        e.rotation.z += frequencyData[10]/1000;
        var color = new THREE.Color(1, 0, 0);
        e.material.color.setRGB(frequencyData[e.id]/255, 0, 0);
      }
    });*/
    requestAnimationFrame(render);
    /*for (var i = 0, l = MAX_POINTS; i < l; i++) {
      positions[index++] = x;
      positions[index++] = y;
      positions[index++] = z;

      x += (Math.random() - 0.5) * 30;
      y += (Math.random() - 0.5) * 30;
      z += (Math.random() - 0.5) * 30;
    }

    line.geometry.setDrawRange(0, drawCount++);*/
    cube.rotation.x += 0.01;
    cube.rotation.y += 0.01;


    renderer.render(scene, camera);
  }

  render();
  //audio.play();


//render();
