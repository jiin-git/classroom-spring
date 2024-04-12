const loginForm = document.getElementById('login-form');
loginForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const formData = new FormData(loginForm);
    const url = "/api/login";
    const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(Object.fromEntries(formData.entries())),
        redirect: "follow"
    }).then(res => {
        if (res.status === 200) {
            window.location.href = res.url;
        } else {
            return res.json();
        }
    }).then(res => {
        let noErrorFields = ['loginId', 'password', 'memberStatus'];
        if (res.errors) {
            Object.keys(res.errors).forEach((value, key, array) => {
                const errorField = document.getElementById(`${value}-error`);
                errorField.innerText = `${res.errors[value]}`;
                const index = noErrorFields.indexOf(value);
                if (index !== -1) {
                    noErrorFields.splice(index, 1);
                }});
            removeErrorMessage(noErrorFields);
        }
        else {
            removeErrorMessage(noErrorFields);
            window.alert(res.message);
        }
    })
});

function removeErrorMessage(noErrorFields) {
    noErrorFields.forEach((value, index, array) => {
        const errorField = document.getElementById(`${value}-error`);
        errorField.innerText = null;
    });
}