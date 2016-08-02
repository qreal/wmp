<%--
  Created by IntelliJ IDEA.
  User: tanvd
  Date: 07.11.15
  Time: 23:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Registration</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css"/>

    <jsp:include page="../include/head.jsp"/>

</head>

<body>
<div class="container-fluid">
    <section class="container">
        <div class="container-page">
            <form class="form-signin" action="register" method="post">
                <div class="col-md-6">
                    <h3 class="dark-grey">Registration</h3>

                    <div class="form-group col-lg-12">
                        <label>Username</label>
                        <input type="text" name="login" class="form-control" placeholder="Email:" required autofocus>
                    </div>

                    <div class="form-group col-lg-6">
                        <label>Password</label>
                        <input type="password" name="pwd1" class="form-control" placeholder="Password:" required>
                    </div>
                    <div class="form-group col-lg-6">
                        <label>Repeat Password</label>
                        <input type="password" name="pwd2" class="form-control" placeholder="Password:" required>
                    </div>
                    <div class="form-group col-lg-6">
                        <button type="submit" class="btn btn-primary">Register</button>

                    </div>
                    <div class="form-group col-lg-6">
                        <c:if test="${errorPasswordsNotMatch}">
                            <h2 class="text-right login-title">Passwords not match</h2>
                        </c:if>
                        <c:if test="${errorLoginAlreadyRegistered}">
                            <h2 class="text-right login-title">Login already registered</h2>
                        </c:if>
                    </div>
                </div>

            </form>

            <div class="col-md-6">
                <h3 class="dark-grey">Terms and Conditions</h3>

                <p>
                    By clicking on "Register" you agree to The Company's' Terms and Conditions
                </p>

                <p>
                    While rare, prices are subject to change based on exchange rate fluctuations -
                    should such a fluctuation happen, we may request an additional payment. You have the option to
                    request a full refund or to pay the new price. (Paragraph 13.5.8)
                </p>

                <p>
                    Should there be an error in the description or pricing of a product, we will provide you with a full
                    refund (Paragraph 13.5.6)
                </p>

                <p>
                    Acceptance of an order by us is dependent on our suppliers ability to provide the product.
                    (Paragraph 13.5.6)
                </p>


            </div>

        </div>
    </section>
</div>
</body>
</html>