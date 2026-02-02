import NoWalletsFound from "../Components/NoWalletsFound.jsx";
import AvailableWallets from "../Components/AvailableWallets.jsx";
import { useLoaderData } from "react-router-dom";

export default function Wallets() {
    const wallets = useLoaderData();

    return (
        <>
            {wallets.length===0 && <NoWalletsFound/>}
            {wallets.length!==0 && <AvailableWallets Wallets={wallets}/>}

        </>
    )
}