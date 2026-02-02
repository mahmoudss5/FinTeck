import { Link } from 'react-router-dom'
import { useState } from 'react'
import { createWallet } from '../services/WalletService'
import { useNavigate,useRevalidator } from 'react-router-dom'
export default function CreateWallet() {
 
    const [error, setError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)
    const navigate=useNavigate()
    const revalidator = useRevalidator();

    const handleSubmit = async(e) => {
        e.preventDefault()
        setIsSubmitting(true)
        const data=new FormData(e.target)
        const currency=data.get('currency')
        const initialBalance=parseFloat(data.get('initialBalance')) || 0
        const active=true
        const wallet={
            currency:currency,
            initialBalance:initialBalance,
            active:active
        }
        console.log(wallet)
       try {
        const result=await createWallet(wallet)
        console.log('Wallet created successfully:', result)
        revalidator.revalidate();
        navigate('/home/wallets')
        } catch (error) {
            setError(error.message || 'Failed to create wallet')
            console.error('Wallet creation failed:', error)
       } finally {
        setIsSubmitting(false)
       }

    }

    return (
        <div className="max-w-2xl mx-auto space-y-6">
            {/* Header */}
            <div className="flex items-center gap-4">
                <Link to="/home/wallets" className="p-2 text-gray-400 hover:text-amber-400 hover:bg-amber-500/10 rounded-lg transition-all">
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                    </svg>
                </Link>
                <div>
                    <h1 className="text-2xl font-bold text-white">Create New Wallet</h1>
                    <p className="text-gray-400 text-sm">Set up a new wallet to manage your funds</p>
                </div>
            </div>

            {/* Form Card */}
            <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                <form className="space-y-6" onSubmit={handleSubmit}>
                    
                    {/* Currency */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Currency
                        </label>
                        <select 
                            name="currency"
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                        >
                            <option value="USD">🇺🇸 USD - US Dollar</option>
                            <option value="EUR">🇪🇺 EUR - Euro</option>
                            <option value="GBP">🇬🇧 GBP - British Pound</option>
                            <option value="JPY">🇯🇵 JPY - Japanese Yen</option>
                            <option value="CAD">🇨🇦 CAD - Canadian Dollar</option>
                            <option value="AUD">🇦🇺 AUD - Australian Dollar</option>
                            <option value="CHF">🇨🇭 CHF - Swiss Franc</option>
                        </select>
                    </div>

                    {/* Initial Deposit */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Initial Deposit
                        </label>
                        <div className="relative">
                            <span className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-500">$</span>
                            <input 
                                type="number" 
                                name="initialBalance"
                                
                                placeholder="0.00"
                                min="0"
                                step="0.01"
                                className="w-full pl-8 pr-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                            />
                        </div>
                    </div>

                    {/* Error Message */}
                    {error && (
                        <p className="text-red-500 text-sm mt-2">{error}</p>
                    )}

                    {/* Buttons */}
                    <div className="flex gap-4 pt-6">
                        <Link to="/home/wallets" className="flex-1">
                            <button 
                                type="button"
                                className="w-full py-3 px-4 border border-amber-900/30 text-gray-300 font-medium rounded-lg hover:bg-amber-500/10 hover:text-white transition-all duration-200"
                            >
                                Cancel
                            </button>
                        </Link>
                        <button 
                            type="submit" 
                            className="flex-1 py-3 px-4 bg-gradient-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:ring-offset-2 focus:ring-offset-[#1a1a1a] transform hover:scale-[1.02] transition-all duration-200 shadow-lg shadow-amber-500/20"
                        >
                            Create Wallet
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}
