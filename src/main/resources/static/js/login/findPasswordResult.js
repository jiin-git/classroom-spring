const findPasswordResult = document.getElementById('password-result');
const jsonPassword = localStorage.getItem("password");
localStorage.removeItem("password");
const password = JSON.parse(jsonPassword);

if (password == null) {
    const supportLogin = document.getElementById('support-login');
    const errorMessage = localStorage.getItem("error");
    localStorage.removeItem("error");

    findPasswordResult.innerHTML +=
        `<h2 class="alert alert-danger login-alert-result display-vertical-center">
            ${errorMessage}
        </h2>`;
    supportLogin.innerHTML +=
        `<a href="/login/find/password" class="btn btn-secondary login-result-btn">비밀번호 찾기</a>`;
}
else {
    findPasswordResult.innerHTML +=
        `<h2 class="login-help-result">
            임시 비밀번호는 ${password} 입니다.
        </h2>`;
}