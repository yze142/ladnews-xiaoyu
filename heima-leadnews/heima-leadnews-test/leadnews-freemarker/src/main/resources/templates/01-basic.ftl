<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
<b>普通文本 String 展示：</b><br><br>
Hello ${name} <br>
<hr>
<b>对象Student中的数据展示：</b><br/>
姓名：${stu.name}<br/>
年龄：${stu.age}
<hr>

<#list list as ls>
<tr>
    <td>${ls_index}</td>
    <td>ls.name</td>
    <td>ls.age</td>

    </tr>

</#list>

</body>
</html>