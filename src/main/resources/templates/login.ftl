<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登陆</title>
</head>
<body>
    <h2>activiti-流程管理</h2>
    <form action="/login" method="get">
        <table>
            <tr>
                <td width="80">用户名：</td>
                <td><input id="username" name="username" style="width: 100px"/></td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input id="password" name="password" type="password" style="width: 100px"/></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <button type="submit" class="btn btn-primary">登录系统</button>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>