const updateMyPageForm = document.getElementById('update-password-form');
updateMyPageForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const formData = new FormData(updateMyPageForm);
    const requestUrl = "/api/members/student/my-page/password"
    const response = await fetch(requestUrl, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(Object.fromEntries(formData.entries()))
    }).then(res => {
        const target = document.getElementById('changePasswordModal');
        const modal = bootstrap.Modal.getInstance(target);
        modal.hide();

        if (res.status === 204) {
            window.alert("비밀번호가 성공적으로 변경됐습니다.")
            const redirectUrl = "/student/my-page";
            location.assign(encodeURI(redirectUrl));
        } else {
            return res.json();
        }
    }).then(res => {
        let noErrorFields = ['password', 'checkPassword'];
        if (res.errors) {
            Object.keys(res.errors).forEach((value, key, array) => {
                const errorField = document.getElementById(`${value}-error`);
                errorField.innerText = `${res.errors[value]}`;
                const index = noErrorFields.indexOf(value);
                if (index !== -1) {
                    noErrorFields.splice(index, 1);
                }});
            removeErrorMessage(noErrorFields);
        } else {
            removeErrorMessage(noErrorFields);
            if (res.message) {
                window.alert(res.message);
            }
        }
    });
});

function removeErrorMessage(noErrorFields) {
    noErrorFields.forEach((value, index, array) => {
        const errorField = document.getElementById(`${value}-error`);
        errorField.innerText = null;
    });
}