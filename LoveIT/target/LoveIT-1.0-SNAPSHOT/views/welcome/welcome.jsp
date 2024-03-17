
<%@taglib prefix="DN" tagdir="/WEB-INF/tags/" %>

<DN:GenericPage pageStyleUrl="${pageContext.request.contextPath}/css/welcome/welcome.css">

    <main>
        <h1>Welcome to the App!</h1>

        <div>
            <button onclick="chooseGender('man')">Choose Man</button>
            <button onclick="chooseGender('woman')">Choose Woman</button>
            <button onclick="chooseOtherGenders()">Choose Others</button>
        </div>

        <div>

        <a href="${pageContext.request.contextPath}/login">Login</a>
        <a href="${pageContext.request.contextPath}/register">Register</a>

        </div>


    </main>

    <script>
        function chooseGender(gender) {
            window.location.href = "/welcome?gender=" + gender;
        }

        function chooseOtherGenders() {
            // Implement logic to show a popup or navigate to a page with 15 gender options
            alert("Implement logic to choose from 15 genders");
        }
    </script>

</DN:GenericPage>
