<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // Security Check: Redirect to login if user is not in session
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return; // Stop processing the rest of the page
    }
    String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Resume Builder</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <span class="navbar-text me-3">
                            Welcome, <%= username %>
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-outline-light" href="logout">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="p-5 mb-4 bg-light rounded-3">
            <div class="container-fluid py-5">
                <h1 class="display-5 fw-bold">Welcome to Your Dashboard</h1>
                <p class="col-md-8 fs-4">
                    You can now start building and managing your professional resumes.
                </p>
                <button class="btn btn-primary btn-lg" type="button">
                    Create a New Resume
                </button>
            </div>
        </div>
    </div>

</body>
</html>