const findIdsForm = document.getElementById('find-ids-form');
findIdsForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const formData = new FormData(findIdsForm);
    const queryString = new URLSearchParams(formData).toString();
    const baseUrl = "/api/login/help/ids";

    const response = await fetch(`${baseUrl}?${queryString}`)
        .then(res => {
            return res.json();
        }).then(res => {
            const resultURI = "/login/find/ids/result";
            if (res.status == 200 || res.status == 302) {
                const loginIds = JSON.stringify(res.loginIds);
                localStorage.setItem("loginIds", loginIds);
                window.location.assign(encodeURI(resultURI));
            } else if (res.status == 404) {
                const errorMessage = res.message;
                localStorage.setItem("loginIds", null);
                localStorage.setItem("error", errorMessage);
                window.location.assign(encodeURI(resultURI));
            } else {
                let noErrorFields = ['name', 'email', 'memberStatus'];
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

