<#assign content>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/">
        <img src="images/panda_before.jpg" style="height: 27px">
      </a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="/">Home</a></li>
        <li class="active"><a href="/workspace">Workspace</a></li>
        <li><a href="/projects">Projects</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a>${name}</a></li>
        <li><a href="/logout">Logout</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div class="error">
  ${error}
</div>

<h1><span>WorkSpace</span></h1>

<div id="work" onresize="resize_canvas()">
  <input id="audio" type="file" name="audio" accept =".mp3, .wav, .midi, .mid"/>
  <label for="audio" class="white">Audio</label>
  <input id="video" type="file" name="video" accept =".mp4, .mov"/>
  <label for="video" class="white">Video</label>
	<ul id="filters">
		<li class="filter_pair">
			<select>
        <option value="Amplitude">Amplitude</option>
        <option value="Tempo">Tempo</option>
        <option value="Frequency">Frequency</option>
      </select>
			<select>
        <option value="Tint">Tint</option>
        <option value="Push">Push</option>
        <option value="Bulge">Bulge</option>
        <option value="Emboss">Emboss</option>
      </select>
    </li>
	</ul>
  <button class="my-button" id="new_filter">Add Filter Pair</button>
  <button class="my-button red-button" id="render">Render</button>
</div>
<script src="js/three.js"></script>
<script src="js/EffectComposer.js"></script>
<script src="js/RenderPass.js"></script>
<script src="js/ShaderPass.js"></script>
<script src="js/CopyShader.js"></script>
<script src="js/DigitalGlitch.js"></script>
<script src="js/GlitchPass.js"></script>
<script src="js/BloomPass.js"></script>
<script src="js/ConvolutionShader.js"></script>
<script src="js/test2.js"></script>
<!-- <script src="js/AudioVisualizer.js"></script>
<script src="js/Lines.js"></script> -->

<audio id="myAudio" src="01 Ultralight Beam.mp3"></audio>
<canvas id="canvas">
</canvas>
<div id="frame">
  <video id="preview" autoplay>
      <source src="/users/testguy2/v_42742c1f079047e38c7bfc210e2384a6/test_video.mp4" type="video/mp4">
  </video>
</div>

<script type="text/javascript">
  function resize_canvas() {
    canvas = document.getElementById("canvas");
    canvas.width = "87%";
    canvas.height = "80%";
  };
  $(document).ready(function() {
    let max_fields = 5; //maximum input boxes allowed
    let wrapper = $("#filters"); //Fields wrapper
    let x = 1;
    $("#new_filter").on("click", function(e) {
      e.preventDefault();
      if(x < max_fields) { //max input box allowed
        x++; //text box increment
        $(wrapper).append('<li class="filter_pair"><select><option value="Amplitude">Amplitude</option><option value="Tempo">Tempo</option><option value="Frequency">Frequency</option></select><select><option value="Tint">Tint</option><option value="Push">Push</option><option value="Bulge">Bulge</option><option value="Emboss">Emboss</option></select><a href="#" class="remove">remove</a></li>'); //add new filter space
        $(".remove").on("click", function(e) { //user click on remove text
          e.preventDefault();
          $(this).parent('li').remove();
          x--;
        })
      } else {
        alert("Maximum 5 filters");
      }
    });

    // send file data using AJAX
    function sendFileWhenDone(fileData) {
      // you can access the file data from the file reader's event object as:

      console.log("File data we sent: ", fileData);

      // Send AJAX request with form data
      $.ajax({
        type: "POST",
        // specify the url we want to upload our file to
        url: '/render',
        // this is how we pass in the actual file data from the form
        data: fileData,
        processData: false,
        contentType: false,
        success: function(JSONsentFromServer) {
          // what do you do went it goes through
          let parsed = JSON.parse(JSONsentFromServer);
          $('#myAudio').attr('src', parsed.audiofp);
          document.getElementById("myAudio").play();
          $('#preview').attr('src', parsed.videofp);
        },
        error: function(errorSentFromServer) {
          // what to do if error
          console.log("[Error]", errorSentFromServer);
        }
      })
    }

    $("#render").click(function(e) {
      e.preventDefault();
      // console.log($(".filter_pair"));
      // console.log($($(".filter_pair").toArray()[0]).children().first().val());
      // console.log($($(".filter_pair").toArray()[0]).children().last().val());
      // console.log($($(".filter_pair").toArray()[1]).children().first().val());
      // console.log($($(".filter_pair").toArray()[1]).children().eq(1).val());
      let filter_choices = [];
      for(let i=0; i<x; i++) {
        filter_choices.push($($(".filter_pair").toArray()[i]).children().first().val());
        filter_choices.push($($(".filter_pair").toArray()[i]).children().eq(1).val());
      }
      // get a reference to the fileInput
      let audioInput = $("#audio");
      console.log("audioInput", audioInput);
      let videoInput = $("#video");
      console.log("videoInput", videoInput);
      // so that you can get the file you wanted to upload
      let audioFile = audioInput[0].files[0];
      let videoFile = videoInput[0].files[0];

      // create the container for our file data
      var fd = new FormData();

      // encode the file
      fd.append('audioName', audioFile);
      fd.append('videoName', videoFile);
      fd.append('filters', JSON.stringify(filter_choices));

      sendFileWhenDone(fd);
    })
  });

  // $("#change").click(function(e) {
  //   e.preventDefault();
  //   console.log("here");
  //   $('#myAudio').attr('src', "tswift.mp3");
  //   document.getElementById("myAudio").play();
  // });
</script>
</#assign>
<#include "main.ftl">
