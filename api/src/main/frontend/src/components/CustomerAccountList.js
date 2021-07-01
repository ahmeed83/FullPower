import {useState} from "react";
import {AssignAccountModal} from "./AssignAccountModal";

export const CustomerAccountList = ({customerAccounts, customerId}) => {

    const [showModal, setShowModal] = useState({show: false, account: null});

    return (
        <div>
            {showModal.show ?
                <div className="pt-4">
                    <AssignAccountModal selectedAccount={showModal.account} customerId={customerId}/>
                </div>
                :
                <div>
                    <h3 className="font-bold py-5 text-white">You can click on the accounts that belongs to you if you
                        want to
                        authorize them to other users</h3>
                    <div className="h-72 grid grid-rows-2 grid-flow-col gap-4">
                        {customerAccounts.map((customer, index) => {
                            return (
                                <div key={index}>
                                    <div onClick={() => setShowModal({
                                        show: true,
                                        account: customer.account.accountNumber
                                    })}
                                         className={`${
                                             customer.accountAuthorizationType === "OWNER" ? "bg-blue-300 cursor-pointer hover:bg-blue-500" : ""
                                         } p-5 m-2 rounded-md bg-gray-400`}>
                                        <div className="flex justify-between">
                                            <p className="px-3">{customer.account.accountNumber}</p>
                                            <p className="px-3">Balance: <b>{customer.account.balance}</b></p>
                                        </div>
                                        <p className="px-3">{customer.account.accountHolderName}</p>
                                        <p className="px-3 pt-5"><b>{customer.accountAuthorizationType}</b></p>
                                    </div>
                                </div>
                            )
                        })}
                    </div>
                </div>
            }
        </div>
    );
}