import { use, useState } from "react";
import Modal from "../Common/Modal";
import { useNavigate } from "react-router-dom";
import { transferMoney } from "../../services/WalletService";
import { v4 as uuidv4 } from 'uuid';
import {useEffect} from "react";
export default function CreateTransaction() {
    const navigate = useNavigate();
    const [error,setError] = useState(''); 
    const [submit,setSubmit] = useState(false);
    const [idempotencyKey,setIdempotencyKey] = useState('');

    useEffect(() => {
        const idempotencyKey = uuidv4();
        setIdempotencyKey(idempotencyKey);
        console.log(idempotencyKey);
    }, []);


    function handleClose() {
        navigate('../');
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setSubmit(true);
        const formData = new FormData(e.target);
        const receiverUserName = formData.get('receiverUsername');
        const amount = formData.get('amount'); // Keep as string for BigDecimal
        const currency = formData.get('currency');
        const senderWalletId = Number(formData.get('senderWalletId'));
        const data={
            senderWalletId,
            receiverUserName,
            amount,
            currency
        }
        console.log('Data being sent:', JSON.stringify(data, null, 2));
        console.log('Idempotency Key:', idempotencyKey);
        try {
            const response = await transferMoney(data,idempotencyKey);
            console.log('Transfer successful:', response);
            navigate('../');
        } catch (error) {
            console.error('Transfer failed:', error);
            console.error('Error message:', error.message);
            setError(error.message);
            setSubmit(false);
        }finally{
            setSubmit(false);
        }
    }

    

    return (
        <Modal open={true} onClose={handleClose}>
            {/* Header */}
            <div className="p-6 border-b border-amber-900/20">
                <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                        <div className="w-10 h-10 bg-linear-to-br from-amber-500 to-yellow-600 rounded-lg flex items-center justify-center">
                            <svg className="w-5 h-5 text-black" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                            </svg>
                        </div>
                        <div>
                            <h2 className="text-xl font-bold text-white">New Transfer</h2>
                            <p className="text-gray-400 text-sm">Send money to another wallet</p>
                        </div>
                    </div>
                    <button 
                        onClick={handleClose}
                        className="w-8 h-8 rounded-lg bg-[#242424] hover:bg-[#2a2a2a] flex items-center justify-center text-gray-400 hover:text-white transition-colors"
                    >
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>
            </div>

            {/* Form */}
            <form onSubmit={handleSubmit}  className="p-6 space-y-5">
                <div>
                    <label className="block text-sm font-medium text-gray-300 mb-2">
                        Sender wallet id
                    </label>
                    <input
                        type="text"
                        name="senderWalletId"
                        placeholder="Enter sender wallet id"
                        className="w-full bg-[#242424] border border-amber-900/20 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-amber-500/50 transition-colors"
                        required
                    />
                </div>
                {/* Receiver Wallet */}
                <div>
                    <label className="block text-sm font-medium text-gray-300 mb-2">
                        Receiver Username
                    </label>
                    <input
                        type="text"
                        name="receiverUsername"
                        placeholder="Enter receiver username"
                        className="w-full bg-[#242424] border border-amber-900/20 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-amber-500/50 transition-colors"
                        required
                    />
                </div>


                {/* Amount */}
                <div>
                    <label className="block text-sm font-medium text-gray-300 mb-2">
                        Amount
                    </label>
                    <div className="relative">
                        <span className="absolute left-4 top-1/2 -translate-y-1/2 text-amber-500 font-semibold">$</span>
                        <input
                            type="number"
                            name="amount"
                            placeholder="0.00"
                            min="0.01"
                            step="0.01"
                            className="w-full bg-[#242424] border border-amber-900/20 rounded-lg pl-8 pr-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-amber-500/50 transition-colors"
                            required
                        />
                    </div>
                </div>

                {/* Currency */}
                <div>
                    <label className="block text-sm font-medium text-gray-300 mb-2">
                        Currency
                    </label>
                    <select
                        name="currency"
                        className="w-full bg-[#242424] border border-amber-900/20 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-amber-500/50 transition-colors cursor-pointer"
                    >
                        <option value="USD">USD - US Dollar</option>
                        <option value="EUR">EUR - Euro</option>
                        <option value="GBP">GBP - British Pound</option>
                        <option value="JPY">JPY - Japanese Yen</option>
                        <option value="AUD">AUD - Australian Dollar</option>
                        <option value="CAD">CAD - Canadian Dollar</option>
                        <option value="CHF">CHF - Swiss Franc</option>
                        <option value="CNY">CNY - Chinese Yuan</option>
                        <option value="SEK">SEK - Swedish Krona</option>
                        <option value="NZD">NZD - New Zealand Dollar</option>
                        <option value="EGP">EGP - Egyptian Pound</option>
                    </select>
                </div>
                {error && (
                    <div className="p-3 bg-red-500/20 border border-red-500/50 rounded-lg">
                        <p className="text-red-400 text-sm">{error}</p>
                    </div>
                )}

              
                {/* Buttons */}
                <div className="flex gap-3 pt-4">
                    <button
                        type="button"
                        onClick={handleClose}
                        className="flex-1 px-4 py-3 bg-[#242424] border border-amber-900/20 text-gray-300 font-medium rounded-lg hover:bg-[#2a2a2a] hover:text-white transition-all duration-200"
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        className="flex-1 px-4 py-3 bg-linear-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 transition-all duration-200 shadow-lg shadow-amber-500/20"
                    >
                        Send Money
                    </button>
                </div>
            </form>
        </Modal>
    );
}