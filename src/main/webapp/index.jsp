<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>


<h1>SpringMVC文件图片上传测试</h1>
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springMVC文件上传">
</form>

<h1>SpringMVC+simditor 富文本图片上传测试</h1>
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springMVC文件上传">
</form>

</body>
</html>