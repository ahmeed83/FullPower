import {useState} from "react";

export const DropDown = ({setAuthorizationType}) => {
    const [showDropDownContent, setShowDropDownContent] = useState(false);

    const showDropDownInfo = () => {
        setShowDropDownContent(!showDropDownContent);
    }

    const hideDropDownInfo = (authorizationType) => {
        setAuthorizationType(authorizationType)
        setShowDropDownContent(!showDropDownContent);
    }


    return (
        <div className="relative mb-32">
            <button onClick={showDropDownInfo}
                    className="inline-flex items-center h-10 px-5 text-indigo-100 transition-colors duration-150 bg-indigo-700 rounded-lg focus:shadow-outline hover:bg-indigo-800">
                <span>Authorization Type</span>
                <svg className="w-4 h-4 ml-3 fill-current" viewBox="0 0 20 20">
                    <path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"/>
                </svg>
            </button>
            {showDropDownContent &&
            <div className="absolute w-48 flex flex-col py-2 mt-1 text-gray-700 bg-white border border-black rounded-lg">
                <div className="px-3 py-1 cursor-pointer hover:bg-blue-500"
                   onClick={() => hideDropDownInfo("READ")}>READ</div>
                <div className="px-3 py-1 cursor-pointer hover:bg-blue-500"
                   onClick={() => hideDropDownInfo("WRITE")}>WRITE</div>
            </div>
            }
        </div>
    )
}
