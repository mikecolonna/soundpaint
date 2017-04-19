<#assign content>

<h1><span>Login</span></h1>

<div id="loginWrap">
  <form class="form-horizontal" id="loginForm">
  <div class="imgcontainer col-sm-offset-2 col-sm-10">
    <img src="../../../static/images/panda_before.jpg" style="width:50%; margin-bottom: 50px">
  </div>
  <div class="form-group">
    <label for="inputEmail" class="col-sm-2 control-label">Email</label>
    <div class="col-sm-10">
      <input type="email" class="form-control" id="inputEmail" placeholder="Email">
    </div>
  </div>
  <div class="form-group">
    <label for="inputPassword" class="col-sm-2 control-label">Password</label>
    <div class="col-sm-10">
      <input type="password" class="form-control" id="inputPassword" placeholder="Password">
    </div>
  </div>
    <div class="col-sm-offset-2 col-sm-10 signInButton">
      <button type="submit" class="btn btn-default" style="width: 44%; float:left; margin-left: 3%; border-color: red">Sign in</button>

      <a class="btn btn-default" style="width: 44%; float:right; margin-right: 3%; border-color: gray">Register</a>
    </div>
  </div>
  </form>
</div>


</#assign>
<#include "main.ftl">