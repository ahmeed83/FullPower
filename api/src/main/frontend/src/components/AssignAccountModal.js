import {useState} from "react";
import {authorizeAccount} from "../services/client";
import {AuthorizeSuccessfullyAlert} from "../common/alerts/AuthorizeSuccessfullyAlert";
import {ErrorAlert} from "../common/alerts/ErrorAlert";
import {DropDown} from "./DropDown";

export const AssignAccountModal = ({selectedAccount, customerId}) => {

    const [granteeId, setSetGranteeId] = useState(null);
    const [authorizationType, setAuthorizationType] = useState(null);

    return (
        <div
            className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden">
            <div className="bg-white px-10 py-20">
                <div className="sm:flex sm:items-start">
                    <div
                        className="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
                    </div>
                    <div
                        className="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                        <h3 className="mt-4 pb-3 text-2xl leading-6 font-medium text-gray-900">{selectedAccount}</h3>
                        <h3 className="mt-4 text-lg leading-6 font-medium text-gray-900"
                            id="modal-title">
                            Please insert the customer that you want to
                            authorize this account for:
                        </h3>
                        <div className="mt-4">
                            <input
                                className="bg-gray-100 border-black appearance-none border rounded py-2 px-3 text-gray-700"
                                id="customerid"
                                type="text"
                                onChange={e => setSetGranteeId(e.target.value)}
                                placeholder="customer id"
                            />
                        </div>
                        <div className="mt-4">
                            <DropDown
                                setAuthorizationType={setAuthorizationType}/>
                        </div>
                    </div>
                </div>
            </div>
            <div
                className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
                <button type="button"
                        onClick={() => sendRequest(customerId, granteeId, selectedAccount, authorizationType)}
                        className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-blue-600 text-base font-medium text-white hover:bg-blue-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm">
                    Assign Account
                </button>
                <button type="button"
                        onClick={() => window.location.reload()}
                        className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                    Cancel
                </button>
            </div>
        </div>
    );
}

function sendRequest(customerId, granteeId, selectedAccount, authorizationType) {
    const requestAuthorizeAccount = {
        "grantorId": customerId,
        "granteeId": granteeId,
        "accountNumber": selectedAccount,
        "authorizationType": authorizationType,
    };

    if (granteeId === null || authorizationType === null) {
        const error = {
            response: {
                data: {
                    httpStatus: 403,
                    errorMessage: "All fields are required!"
                }
            }
        }
        return ErrorAlert(error);
    }

    authorizeAccount(requestAuthorizeAccount)
        .then(() => {
            AuthorizeSuccessfullyAlert(selectedAccount);
        })
        .catch((err) => {
            ErrorAlert(err);
        });

}
