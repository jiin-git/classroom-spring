const baseUrl = "/api/members/instructor/lectures";
const lectureIdApplicants = location.pathname.split('/instructor/lectures/')[1];
const lectureInfoUrl = location.pathname.split('/applicants')[0];

async function getApplicantList() {
    const response = await fetch(`${baseUrl}/${lectureIdApplicants}`)
        .then(res => res.json());
    return response;
}

const applicantList = getApplicantList()
    .then(applicants => {
        for (const applicant of applicants) {
            addApplicantInfo(applicant);
        }
        addButtons();
    });

function addApplicantInfo(applicant) {
    const applicantsContainer = document.getElementById('applicants');

    applicantsContainer.innerHTML += `
        <p class="m-0 pt-1 pb-1 sCoreDreamFont fs-list fs-id">${applicant.id}</p>
        <p class="m-0 pt-1 pb-1 sCoreDreamFont fs-list">${applicant.name}</p>`
}
function addButtons() {
    const buttonContainer = document.getElementById('buttons');
    buttonContainer.innerHTML += `
      <a class="btn btn-secondary" href="${lectureInfoUrl}">강의정보</a>
      <a class="btn btn-primary" href="/instructor/lectures">홈으로</a>`;
}