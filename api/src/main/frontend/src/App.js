import {useState} from "react";
import './styles/output.css'
import {getCustomerAccounts} from "./services/client";
import {Spinner} from "./common/spinner/Spinner";
import {CustomerAccountList} from "./components/CustomerAccountList";

export const App = () => {

    const [loading, isLoading] = useState(false);
    const [customerAccounts, setCustomerAccounts] = useState([]);
    const [customerId, setCustomerId] = useState();

    const handleSubmit = (evt) => {
        evt.preventDefault();
        isLoading(true);
        getCustomerAccounts(customerId).then((res) => {
            setCustomerAccounts(res.data);
            isLoading(false);
        });
    }

    if (loading) {
        return <Spinner/>;
    }

    return (
        <div className="bg-gray-900 p-20 h-screen flex justify-center items-start flex-col">
            <h1 className="text-5xl text-white">Rabobank assignment Ahmed</h1>

            {customerAccounts.length > 0 ?
                <CustomerAccountList customerAccounts={customerAccounts} customerId={customerId}/>
                : <form onSubmit={handleSubmit}>
                    <div className="pt-24 pb-10">
                        <h2 className="pb-2 text-white">Please use 100 or 200.</h2>
                        <input
                            className="bg-gray-100 appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            id="title"
                            type="text"
                            onChange={e => setCustomerId(e.target.value)}
                            placeholder="Login with your userID"
                        />
                        <button className="p-4 bg-blue-600 rounded-lg font-bold text-white mt-5 hover:bg-gray-600">
                            log in
                        </button>
                    </div>
                </form>
            }
        </div>
    );
}