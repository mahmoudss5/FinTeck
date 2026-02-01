import { useState } from 'react'

export default function Loan() {
    const [loanAmount, setLoanAmount] = useState(5000)
    const [loanTerm, setLoanTerm] = useState(12)
    
    // Simple interest calculation (for demo)
    const interestRate = 8.5
    const monthlyPayment = ((loanAmount * (1 + (interestRate / 100) * (loanTerm / 12))) / loanTerm).toFixed(2)
    const totalPayment = (monthlyPayment * loanTerm).toFixed(2)

    return (
        <div className="space-y-6">
            {/* Header */}
            <div>
                <h1 className="text-2xl font-bold text-white">Apply for a Loan</h1>
                <p className="text-gray-400">Get funds quickly with competitive rates</p>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Loan Calculator Card */}
                <div className="lg:col-span-1 bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <h2 className="text-lg font-semibold text-white mb-6">Loan Calculator</h2>
                    
                    {/* Loan Amount Slider */}
                    <div className="mb-6">
                        <div className="flex justify-between mb-2">
                            <label className="text-sm text-gray-400">Loan Amount</label>
                            <span className="text-amber-400 font-semibold">${loanAmount.toLocaleString()}</span>
                        </div>
                        <input 
                            type="range" 
                            min="1000" 
                            max="50000" 
                            step="500"
                            value={loanAmount}
                            onChange={(e) => setLoanAmount(Number(e.target.value))}
                            className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-amber-500"
                        />
                        <div className="flex justify-between text-xs text-gray-500 mt-1">
                            <span>$1,000</span>
                            <span>$50,000</span>
                        </div>
                    </div>

                    {/* Loan Term Slider */}
                    <div className="mb-6">
                        <div className="flex justify-between mb-2">
                            <label className="text-sm text-gray-400">Loan Term</label>
                            <span className="text-amber-400 font-semibold">{loanTerm} months</span>
                        </div>
                        <input 
                            type="range" 
                            min="6" 
                            max="60" 
                            step="6"
                            value={loanTerm}
                            onChange={(e) => setLoanTerm(Number(e.target.value))}
                            className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-amber-500"
                        />
                        <div className="flex justify-between text-xs text-gray-500 mt-1">
                            <span>6 months</span>
                            <span>60 months</span>
                        </div>
                    </div>

                    {/* Calculation Results */}
                    <div className="space-y-4 pt-4 border-t border-amber-900/20">
                        <div className="flex justify-between">
                            <span className="text-gray-400">Interest Rate</span>
                            <span className="text-white font-medium">{interestRate}% APR</span>
                        </div>
                        <div className="flex justify-between">
                            <span className="text-gray-400">Monthly Payment</span>
                            <span className="text-white font-medium">${monthlyPayment}</span>
                        </div>
                        <div className="flex justify-between">
                            <span className="text-gray-400">Total Payment</span>
                            <span className="text-white font-medium">${totalPayment}</span>
                        </div>
                        <div className="flex justify-between pt-4 border-t border-amber-900/20">
                            <span className="text-gray-400">Total Interest</span>
                            <span className="text-amber-400 font-semibold">${(totalPayment - loanAmount).toFixed(2)}</span>
                        </div>
                    </div>
                </div>

                {/* Application Form */}
                <div className="lg:col-span-2 bg-[#1a1a1a] border border-amber-900/20 rounded-xl p-6">
                    <h2 className="text-lg font-semibold text-white mb-6">Application Details</h2>
                    
                    <form className="space-y-6">
                        {/* Personal Information */}
                        <div>
                            <h3 className="text-sm font-medium text-amber-400 mb-4">Personal Information</h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Full Name</label>
                                    <input 
                                        type="text" 
                                        placeholder="John Doe"
                                        className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Email Address</label>
                                    <input 
                                        type="email" 
                                        placeholder="john@example.com"
                                        className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Phone Number</label>
                                    <input 
                                        type="tel" 
                                        placeholder="+1 (555) 000-0000"
                                        className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Date of Birth</label>
                                    <input 
                                        type="date" 
                                        className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Martial status</label>
                                    <select className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all">
                                       <option value="">Select status</option>
                                       <option value="single">Single</option>
                                       <option value="married">Married</option>
                                       <option value="widowed">Widowed</option>
                                       <option value="separated">Separated</option>
                                   </select>
                                </div>
                            </div>
                        </div>

                        {/* Employment Information */}
                        <div>
                            <h3 className="text-sm font-medium text-amber-400 mb-4">Employment Information</h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Employment Status</label>
                                    <select className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all">
                                        <option value="">Select status</option>
                                        <option value="employed">Employed Full-Time</option>
                                        <option value="part-time">Employed Part-Time</option>
                                        <option value="self-employed">Self-Employed</option>
                                        <option value="retired">Retired</option>
                                        <option value="student">Student</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Monthly Income</label>
                                    <input 
                                        type="number" 
                                        placeholder="$0.00"
                                        className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Employer Name</label>
                                    <input 
                                        type="text" 
                                        placeholder="Company Inc."
                                        className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Years at Current Job</label>
                                    <select className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all">
                                        <option value="">Select duration</option>
                                        <option value="0-1">Less than 1 year</option>
                                        <option value="1-3">1-3 years</option>
                                        <option value="3-5">3-5 years</option>
                                        <option value="5+">5+ years</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        {/* Loan Details */}
                        <div>
                            <h3 className="text-sm font-medium text-amber-400 mb-4">Loan Details</h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Loan Purpose</label>
                                    <select className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all">
                                        <option value="">Select purpose</option>
                                        <option value="personal">Personal Expenses</option>
                                        <option value="debt">Debt Consolidation</option>
                                        <option value="home">Home Improvement</option>
                                        <option value="medical">Medical Expenses</option>
                                        <option value="education">Education</option>
                                        <option value="business">Business</option>
                                        <option value="other">Other</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm text-gray-400 mb-2">Requested Amount</label>
                                    <input 
                                        type="text" 
                                        value={`$${loanAmount.toLocaleString()}`}
                                        readOnly
                                        className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-amber-400 font-semibold focus:outline-none"
                                    />
                                </div>
                            </div>
                        </div>

                        {/* Terms Agreement */}
                        <div className="flex items-start gap-3">
                            <input 
                                type="checkbox" 
                                id="terms"
                                className="w-5 h-5 mt-0.5 bg-[#242424] border-amber-900/30 rounded text-amber-500 focus:ring-amber-500 focus:ring-offset-[#1a1a1a]"
                            />
                            <label htmlFor="terms" className="text-sm text-gray-400">
                                I agree to the <a href="#" className="text-amber-400 hover:text-amber-300">Terms and Conditions</a> and authorize CashMe to verify my information and perform a credit check.
                            </label>
                        </div>

                        {/* Submit Button */}
                        <button 
                            type="submit" 
                            className="w-full py-4 px-6 bg-gradient-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:ring-offset-2 focus:ring-offset-[#1a1a1a] transform hover:scale-[1.02] transition-all duration-200 shadow-lg shadow-amber-500/20"
                        >
                            Submit Application
                        </button>

                        <p className="text-center text-xs text-gray-500">
                            Your application will be reviewed within 24 hours. You'll receive an email with the decision.
                        </p>
                    </form>
                </div>
            </div>
        </div>
    )
}