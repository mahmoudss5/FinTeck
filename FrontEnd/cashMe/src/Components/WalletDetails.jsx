import {useParams} from "react-router-dom";

export default function WalletDetails() {
    const WalleId=useParams().id;

    return (
        <div className="text-2xl flex justify-center items-center text-white">
                the detials of wallet with id {WalleId}
        </div>
    )
}