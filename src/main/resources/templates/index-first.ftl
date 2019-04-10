<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>index-first</title>
</head>
<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a href="#" class="brand">Activiti Explorer</a>
            <div class="nav-collapse">
                <ul class="nav">
                    <li class="active"><a href="#" rel="main/welcome.action"><i class="icon-home icon-black"></i>首页</a></li>
                    <li><a href="#" rel="chapter6/task/list"><i class="icon-th-list icon-black"></i>任务列表</a></li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-th-large icon-black"></i>管理<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="/deployactiviti">流程定义</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav pull-right">
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#" title="角色">
                            <i class="icon-user icon-black" style="margin-right: .3em"></i>用户信息<b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a id="changePwd" href="#"><i class="icon-wrench icon-black"></i>修改密码</a></li>
                            <li><a href="/logout"><i class="icon-eject icon-black"></i>安全退出</a></li>
                        </ul>
                    </li>
                </ul>

            </div>
        </div>
    </div>
    <#--<div class="container">-->
        <#--<iframe id="mainIframe" name="mainIframe" src="welcome" class="module-iframe" scrolling="auto" frameborder="0" style="width:100%;"></iframe>-->
    <#--</div>-->
</div>
</body>
</html>