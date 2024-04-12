const lectureList = document.getElementById('lecture-list');
const urlSearchParams = new URLSearchParams(window.location.search);
const urlSearchParamsToString = urlSearchParams.toString();
const baseUrl = "/api/members/student/lectures/list";
const requestUrl = `${baseUrl}?${urlSearchParamsToString}`

for (const param of urlSearchParams.keys()) {
    const paramValue = urlSearchParams.get(param);
    if (paramValue != null && paramValue != "") {
        const element = document.getElementById(param);
        if (param == 'status' || param == 'condition') {
            for (let i = 0; i < element.options.length; i++) {
                if (element.options[i].value == paramValue) {
                    element.options[i].selected = true;
                }
            }
        }
        else if (param == 'text'){
            element.value = paramValue;
        }

    }
}

async function getLectureList() {
    const response = await fetch(requestUrl)
        .then(res => {
            return res.json();
        }).then(res => {
            if (res.status && res.status !== 200) {
                window.alert(res.message);
            } else {
                return res;
            }
        });
    return response;
}
const addDataToHtml = getLectureList()
    .then(response => {
        const lectures = response.content;
        for (const lecture of lectures) {
            addLectureList(lecture);
        }
        addPageButton(response);
    });

function addLectureList(lecture) {
    const lectureBasicInfo = lecture.lectureBasicInfo;
    const instructorInfo = lecture.instructorInfo;
    const profileImage = lecture.profileImage;
    const isApplied = lecture.isApplied;
    const mapperId = lecture.mapperId;

    addLectureContainer(lectureBasicInfo);
    addLectureInfo(lectureBasicInfo, instructorInfo);
    addProfileImage(profileImage, lectureBasicInfo.id);
    addLectureButtons(lectureBasicInfo, isApplied, mapperId);
}

function addLectureContainer(lectureBasicInfo) {
    lectureList.innerHTML +=
        `<div class="row g-0">
                <div class="col-md-4 text-center display-vertical-center p-3">
                    <div id="lecture-profile-${lectureBasicInfo.id}" class="small-rounded-square-container"></div>
                </div>

                <div class="col-md-8">
                    <div id="lecture-info-${lectureBasicInfo.id}" class="card-body"></div>
                </div>
            </div>
         </div>`;
}
function addProfileImage(profileImage, lectureId) {
    const lectureProfile = document.getElementById(`lecture-profile-${lectureId}`);
    if (profileImage.imageData == null) {
        lectureProfile.innerHTML +=
            `<div class="profile-img-container">
                <svg xmlns="http://www.w3.org/2000/svg" class="bi bi-images default-lecture-img" viewBox="0 0 16 16">
                    <path d="M4.502 9a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z"/>
                    <path d="M14.002 13a2 2 0 0 1-2 2h-10a2 2 0 0 1-2-2V5A2 2 0 0 1 2 3a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v8a2 2 0 0 1-1.998 2zM14 2H4a1 1 0 0 0-1 1h9.002a2 2 0 0 1 2 2v7A1 1 0 0 0 15 11V3a1 1 0 0 0-1-1zM2.002 4a1 1 0 0 0-1 1v8l2.646-2.354a.5.5 0 0 1 .63-.062l2.66 1.773 3.71-3.71a.5.5 0 0 1 .577-.094l1.777 1.947V5a1 1 0 0 0-1-1h-10z"/>
                </svg>
            </div>`;
    } else {
        lectureProfile.innerHTML +=
            `<img src="data:${profileImage.dataType};base64,${profileImage.imageData}" class="img-fluid">`;
    }
}
function addLectureInfo(lectureBasicInfo, instructorInfo) {
    const lectureInfo = document.getElementById(`lecture-info-${lectureBasicInfo.id}`);
    lectureInfo.innerHTML +=
        `<h5 class="card-title gmarketSansFont">강의명: ${lectureBasicInfo.name}</h5>
        <p class="card-text gmarketSansFont">강의ID: ${lectureBasicInfo.id}</p>
        <p class="card-text gmarketSansFont">강사: ${instructorInfo.name}</p>
        <p class="card-text gmarketSansFont">남은 정원: ${lectureBasicInfo.remainingPersonnel}</p>
        <p class="card-text"><small class="text-body-secondary">${lectureBasicInfo.lectureStatus}</small></p>`
}
function addLectureButtons(lectureBasicInfo, isApplied, mapperId) {
    const lectureInfo = document.getElementById(`lecture-info-${lectureBasicInfo.id}`);

    if (lectureBasicInfo.lectureStatus === "READY" || lectureBasicInfo.lectureStatus === "FULL" && isApplied === false) {
        lectureInfo.innerHTML +=
            `<div id="disable-apply-lecture-btn-${lectureBasicInfo.id}" class="disable-apply-lecture-btn">
                <button type="button" class="btn btn-secondary" disabled>신청 불가</button>
            </div>`;
    }
    else if (isApplied) {
        lectureInfo.innerHTML +=
            `<div class="cancel-lecture-btn">
                <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#cancelLectureModal${lectureBasicInfo.id}">강의 취소</button>
                <div class="modal fade" id="cancelLectureModal${lectureBasicInfo.id}" tabindex="-1" aria-labelledby="cancelLectureModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="cancelLectureModalLabel">강의 취소</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">정말로 ${lectureBasicInfo.name} 강의를 취소하시겠습니까?</div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                                <form id="cancel-lecture-btn-${lectureBasicInfo.id}" onclick="cancelLecture(${mapperId}, ${lectureBasicInfo.id})">
                                    <input type="submit" class="btn btn-danger" value="강의 취소">
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`;
    } else if (lectureBasicInfo.lectureStatus === "OPEN" && isApplied === false) {
        lectureInfo.innerHTML +=
            `<div class="apply-lecture-btn">
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addLectureModal${lectureBasicInfo.id}">강의 신청</button>
                <div class="modal fade" id="addLectureModal${lectureBasicInfo.id}" tabindex="-1" aria-labelledby="addLectureModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="addLectureModalLabel">강의 신청</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">정말로 ${lectureBasicInfo.name} 강의를 신청하시겠습니까?</div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                                <form id="apply-lecture-btn-${lectureBasicInfo.id}" onclick="applyLecture(${lectureBasicInfo.id})">
                                    <input type="submit" class="btn btn-primary" value="강의 신청">
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`;
    }
}

/* 강의 취소 API 호출 */
function cancelLecture(mapperId, lectureId) {
    const cancelLectureButton = document.getElementById(`cancel-lecture-btn-${lectureId}`);
    cancelLectureButton.addEventListener("submit", async function (e) {
        e.preventDefault();

        const requestUrl = `/api/members/student/lectures/${mapperId}`
        const response = await fetch(requestUrl, {
            method: 'DELETE'
        }).then(res => {
            if (res.status === 204) {
                window.alert("강의 취소가 정상적으로 완료되었습니다.");
                const redirectURI = location.pathname;
                location.assign(encodeURI(redirectURI));
            }
            else {
                return res.json();
            }
        }).then(res => {
            if (res.status && res.status === 400) {
                window.alert(res.message);
            }
        })
    })
}

/* 강의 신청 API 호출 */
function applyLecture(lectureId) {
    const applyLectureButton = document.getElementById(`apply-lecture-btn-${lectureId}`);
    applyLectureButton.addEventListener("submit", async function (e) {
        e.preventDefault();

        const requestUrl = `/api/members/student/lectures/${lectureId}`;
        const response = await fetch(requestUrl, {
            method: 'POST'
        }).then(res => {
            if (res.status === 200) {
                window.alert("강의 신청이 완료되었습니다.");
                const redirectURI = location.pathname;
                location.assign(encodeURI(redirectURI));
            } else {
                return res.json();
            }
        }).then(res => {
            if (res.status && res.status === 400) {
                window.alert(res.message);
            }
        })
    });
}

function addPageButton(response) {
    const showPages = getShowPages(response);
    const pagination = document.getElementById('pagination');
    const totalPages = ((response.totalPages > 0) ? response.totalPages : 1);

    const checkStartPage = showPages.some((page) => page === 1);
    const checkEndPage = showPages.some((page) => page === totalPages);

    pagination.innerHTML +=
        `<li id="first-page" class="page-item">
            <button form="searchCondition" type="submit" class="page-link" name="page" value="1" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </button>
        </li>`;

    if (!checkStartPage) {
        pagination.innerHTML +=
            `<li class="page-item">
                <a class="page-link" href="#" aria-label="morePrevious">...</a>
            </li>`;
    }

    for (let p = showPages[0]; p <= showPages[showPages.length - 1]; p++) {
        pagination.innerHTML +=
            `<li class="page-item">
                <button form="searchCondition" type="submit" class="page-link" name="page" value="${p}">${p}</button>
            </li>`
    }

    if (!checkEndPage) {
        pagination.innerHTML +=
            `<li class="page-item">
               <a class="page-link" href="#" aria-label="moreNext">...</a>
            </li>`;
    }

    pagination.innerHTML +=
        `<li class="page-item">
            <button form="searchCondition" class="page-link" name="page" value="${showPages.pop()}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </button>
        </li>`
}
function getShowPages(response) {
    const startPage = 1;
    const showPageUnit = 3;

    const page = response.pageable.pageNumber + 1;
    const endPage = ((response.totalPages > 0) ? response.totalPages : 1);

    let pages = [];
    for (let i = 1; i <= endPage; i++) {
        pages.push(i);
    }

    if (endPage > showPageUnit) {
        if (page == startPage) {
            return pages.slice(startPage - 1, showPageUnit);
        } else if (page == endPage) {
            return pages.slice(endPage - showPageUnit, endPage);
        } else {
            return pages.slice(page - 2, page + 1);
        }
    } else {
        return pages;
    }
}
