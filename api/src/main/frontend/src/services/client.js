import Axios from 'axios';
import {AUTHORIZE_ACCOUNT, CONTENT_TYPE_JSON_VALUE, GET_CUSTOMER_ACCOUNTS} from "./constants";

const checkStatus = (response) => {
    if (response.status >= 200) {
        return response;
    } else {
        let error = new Error(response.statusText);
        error.response = response;
        response.then((e) => {
            error.error = e;
        });
        return Promise.reject(error);
    }
};

export const getCustomerAccounts = (customerId) =>
    Axios.get(GET_CUSTOMER_ACCOUNTS + customerId, {
        headers: {
            'Content-Type': CONTENT_TYPE_JSON_VALUE,
        },
    }).then(checkStatus);

export const authorizeAccount = (requestAuthorizeAccount) =>
    Axios.post(AUTHORIZE_ACCOUNT, requestAuthorizeAccount, {
        headers: {
            'Content-Type': CONTENT_TYPE_JSON_VALUE,
        },
    }).then(checkStatus);