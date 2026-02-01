import NoWalletsFound from "../Components/NoWalletsFound.jsx";
import AvilableWallets from "../Components/AvilableWallets.jsx";

export default function Wallets() {

    const wallets = [
        {
            walletId: '1',
            walletCurrency:'Usd',
            walletBalance:'100000',
            walletStatus:'Active',
            walletType:'Checking',
            walletCreatedAt:'2024-01-01',
            walletUpdatedAt:'2024-01-01'
        },
        {
            walletId: '2',
            walletCurrency:'Usd',
            walletBalance:'100000',
            walletStatus:'Active',
            walletType:'Checking',
            walletCreatedAt:'2024-01-01',
            walletUpdatedAt:'2024-01-01'
        },
        {
            walletId: '3',
            walletCurrency:'Usd',
            walletBalance:'100000',
            walletStatus:'Active',
            walletType:'Checking',
            walletCreatedAt:'2024-01-01',
            walletUpdatedAt:'2024-01-01'
        },
        {
            walletId: '4',
            walletCurrency:'Usd',
            walletBalance:'100000',
            walletStatus:'Active',
            walletType:'Checking',
            walletCreatedAt:'2024-01-01',
            walletUpdatedAt:'2024-01-01'
        },
    ]



    return (
        <>
            {wallets.length===0 && <NoWalletsFound/>}
            {wallets.length!==0 && <AvilableWallets Wallets={wallets}/>}

        </>
    )
}