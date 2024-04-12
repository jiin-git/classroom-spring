const findIdsResult = document.getElementById('find-ids-result');
const jsonLoginIds = localStorage.getItem("loginIds");
localStorage.removeItem("loginIds");
const loginIds = JSON.parse(jsonLoginIds);

if (loginIds == null) {
    const supportLogin = document.getElementById('support-login');
    const errorMessage = localStorage.getItem("error");
    localStorage.removeItem("error");

    findIdsResult.innerHTML +=
        `<h2 class="alert alert-danger login-alert-result display-vertical-center" role="alert">
            ${errorMessage}
        </h2>`;
    supportLogin.innerHTML +=
        `<a href="/login/find/ids" class="btn btn-secondary login-result-btn">아이디 찾기</a>`;
}
else {
    findIdsResult.innerHTML +=
        `<div class="mb-3">
            <h2 class="login-help-result">조회한 회원 아이디 결과입니다.</h2>
            <div class="ids-result-container">
                <ul id="ids-list"></ul>
            </div>
        </div>`

    const idsList = document.getElementById('ids-list');
    Array.from(loginIds).forEach((value, index, array) => {
        idsList.innerHTML +=
            `<li class="sCoreDreamFont" value="${value}">${value}</li>`
    });
}