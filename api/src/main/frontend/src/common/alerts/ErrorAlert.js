import Swal from 'sweetalert2';

export const ErrorAlert = (err) => {
    console.log(err.response.data.httpStatus)
    if (err.response.data.httpStatus === 403 || err.response.data.httpStatus === 409) {
        Swal.fire(err.response.data.errorMessage, "", 'warning');
    } else {
        Swal.fire('Sorry! There is something wrong with the server', 'Try one more time, or grab a coffee ☕️', 'error');
    }
};