const findPasswordForm = document.getElementById('find-password-form');
findPasswordForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const formData = new FormData(findPasswordForm);
    const url = "/api/login/help/password";
    const response = await fetch(url, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(Object.fromEntries(formData))
    }).then(res => {
        return res.json();
    }).then(res => {
        const resultURI = "/login/find/password/result";
        if (res.status == 200) {
            const password = JSON.stringify(res.password);
            localStorage.setItem("password", password);
            window.location.assign(encodeURI(resultURI));
        } else if (res.status == 404) {
            const errorMessage = res.message;
            localStorage.setItem("password", null);
            localStorage.setItem("error", errorMessage);
            window.location.assign(encodeURI(resultURI));
        } else {
            let noErrorFields = ['loginId', 'email', 'memberStatus'];
            if (res.errors) {
                Object.keys(res.errors).forEach((value, key, array) => {
                    const errorField = document.getElementById(`${value}-error`);
                    errorField.innerText = `${res.errors[value]}`;
                    const index = noErrorFields.indexOf(value);
                    if (index !== -1) {
                        noErrorFields.splice(index, 1);
                    }
                });
                removeErrorMessage(noErrorFields);
            } else {
                removeErrorMessage(noErrorFields);
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