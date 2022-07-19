<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<table width="500px" align="center" border="1px">
    <thead>
    <th>id</th>
    <th>name</th>
    <th>password</th>
    <th>sex</th>
    <th>birthday</th>
    <th>registTime</th>
    </thead>
    <tbody>
 <c:forEach items="${requestScope.users}" var="user" varStatus="i">
    <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.password}</td>
        <td>${user.sex}</td>

        <td><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/></td>
        <td><fmt:formatDate value="${user.registTime}" pattern="yyyy-MM-dd"/></td>
    </tr>
 </c:forEach>


    </tbody>

</table>




</body>
</html>
