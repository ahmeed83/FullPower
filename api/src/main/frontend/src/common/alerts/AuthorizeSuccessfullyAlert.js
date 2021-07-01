import Swal from 'sweetalert2';

export const AuthorizeSuccessfullyAlert = (accountNumber) => {
    Swal.fire({
        title: accountNumber + " is Successfully authorized!!",
        text: 'Thanks',
        icon: "success",
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.reload();
        }
    })
};


