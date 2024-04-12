const baseUrl = "/api/members/instructor/lectures";
const createLectureForm = document.getElementById('create-lecture-form');
createLectureForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const formData = new FormData(createLectureForm);
    const response = await fetch(baseUrl, {
        method: 'POST',
        body: formData
    })
        .then(res => {
            const target = document.getElementById('addLectureModal');
            const modal = bootstrap.Modal.getInstance(target);
            modal.hide();

            if (res.status === 303) {
                const redirectURI = "/instructor/lectures";
                window.location.assign(encodeURI(redirectURI));
            } else {
                return res.json();
            }
        }).then(res => {
            let noErrorFields = ['name', 'personnel', 'lectureStatus', 'imageFile'];
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
                if (res.message) {
                    window.alert(res.message);
                }
                removeErrorMessage(noErrorFields);
            }
        });
});

function removeErrorMessage(noErrorFields) {
    noErrorFields.forEach((value, index, array) => {
        const errorField = document.getElementById(`${value}-error`);
        errorField.innerText = null;
    });
}