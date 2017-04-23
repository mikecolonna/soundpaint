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
        <li><a href="/home">Home</a></li>
        <li class="active"><a href="/workspace">Workspace</a></li>
        <li><a href="/projects">Projects</a></li>
      </ul>
      <form class="navbar-form navbar-left">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="Search">
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
      </form>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/login">Login</a></li>
        <li><a href="/register">Register</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<h1><span>WorkSpace</span></h1>

<div id="work">
	<span id="choices">
		<ul>
			<li>
				filter 1
			</li>
			<li>
				filter 2
			</li>
			<li>
				filter 3
			</li>
		</ul>
	</span>
	<span id="viewing">
		<script src="js/three.js"></script>
		<script src="js/test.js"></script>

		<audio id="myAudio" src="01 Ultralight Beam.mp3"></audio>
		<canvas id="canvas">
		</canvas>
		<video width="320" height="240" controls>
  			<source src="movie.mp4" type="video/mp4">
		</video>

	</span>
</div>
</#assign>
<#include "main.ftl">
