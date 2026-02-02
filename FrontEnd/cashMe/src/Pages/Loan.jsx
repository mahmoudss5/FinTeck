import { useState, useEffect } from 'react';
import { useAuth } from '../services/AuthProvider';
import { getUserLoans, applyForLoan } from '../services/LoanService';

export default function Loan() {
    const { user } = useAuth();
    const [loanAmount, setLoanAmount] = useState(5000);
    const [loanTerm, setLoanTerm] = useState(12);
    const [myLoans, setMyLoans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState('');
    const [activeTab, setActiveTab] = useState('myLoans'); // 'myLoans' or 'apply'

    // Form state
    const [formData, setFormData] = useState({
        fullName: '',
        email: '',
        phoneNumber: '',
        dateOfBirth: '',
        maritalStatus: '',
        employmentStatus: '',
        monthlyIncome: '',
        employerName: '',
        yearsAtCurrentJob: '',
        loanPurpose: ''
    });

    // Simple interest calculation (for demo)
    const interestRate = 8.5;
    const monthlyPayment = ((loanAmount * (1 + (interestRate / 100) * (loanTerm / 12))) / loanTerm).toFixed(2);
    const totalPayment = (monthlyPayment * loanTerm).toFixed(2);

    useEffect(() => {
        fetchLoans();
    }, [user?.id]);

    const fetchLoans = async () => {
        if (!user?.id) return;
        try {
            setLoading(true);
            const data = await getUserLoans(user.id);
            setMyLoans(data);
        } catch (err) {
            console.error("Error fetching loans:", err);
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        setError(null);
        setSuccessMessage('');

        try {
            const loanData = {
                ...formData,
                monthlyIncome: parseFloat(formData.monthlyIncome),
                requestedAmount: loanAmount
            };
            
            await applyForLoan(loanData);
            setSuccessMessage('Loan application submitted successfully! We will review it within 24 hours.');
            
            // Reset form
            setFormData({
                fullName: '',
                email: '',
                phoneNumber: '',
                dateOfBirth: '',
                maritalStatus: '',
                employmentStatus: '',
                monthlyIncome: '',
                employerName: '',
                yearsAtCurrentJob: '',
                loanPurpose: ''
            });
            
            // Refresh loans list
            await fetchLoans();
            setActiveTab('myLoans');
        } catch (err) {
            setError(err.message || 'Failed to submit loan application');
        } finally {
            setSubmitting(false);
        }
    };

    const getStatusBadge = (status) => {
        const styles = {
            PENDING: 'bg-yellow-500/10 text-yellow-500 border-yellow-500/20',
            APPROVED: 'bg-green-500/10 text-green-500 border-green-500/20',
            REJECTED: 'bg-red-500/10 text-red-500 border-red-500/20',
            UNDER_REVIEW: 'bg-blue-500/10 text-blue-500 border-blue-500/20'
        };
        return styles[status] || styles.PENDING;
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleDateString('en-US', { 
            month: 'short', 
            day: 'numeric', 
            year: 'numeric' 
        });
    };

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold text-white">Loans</h1>
                    <p className="text-gray-400">View your loans and apply for new ones</p>
                </div>
            </div>

            {/* Tab Navigation */}
            <div className="flex gap-2 bg-[#1a1a1a] p-1 rounded-lg w-fit border border-amber-900/20">
                <button
                    onClick={() => setActiveTab('myLoans')}
                    className={`px-4 py-2 rounded-lg font-medium transition-all duration-200 ${
                        activeTab === 'myLoans'
                            ? 'bg-amber-500 text-black'
                            : 'text-gray-400 hover:text-white'
                    }`}
                >
                    My Loan Applications
                </button>
                <button
                    onClick={() => setActiveTab('apply')}
                    className={`px-4 py-2 rounded-lg font-medium transition-all duration-200 ${
                        activeTab === 'apply'
                            ? 'bg-amber-500 text-black'
                            : 'text-gray-400 hover:text-white'
                    }`}
                >
                    Apply for Loan
                </button>
            </div>

            {/* Success Message */}
            {successMessage && (
                <div className="bg-green-500/10 border border-green-500/20 rounded-xl p-4">
                    <p className="text-green-400">{successMessage}</p>
                </div>
            )}

            {/* Error Message */}
            {error && (
                <div className="bg-red-500/10 border border-red-500/20 rounded-xl p-4">
                    <p className="text-red-400">{error}</p>
                </div>
            )}

            {/* My Loans Tab */}
            {activeTab === 'myLoans' && (
                <div className="bg-[#1a1a1a] border border-amber-900/20 rounded-xl overflow-hidden">
                    <div className="p-6 border-b border-amber-900/20">
                        <h2 className="text-lg font-semibold text-white">My Loan Applications</h2>
                        <p className="text-gray-400 text-sm">Track the status of your loan applications</p>
                    </div>

                    {loading ? (
                        <div className="flex items-center justify-center py-12">
                            <div className="w-10 h-10 border-4 border-amber-500 border-t-transparent rounded-full animate-spin"></div>
                        </div>
                    ) : myLoans.length === 0 ? (
                        <div className="p-12 text-center">
                            <svg className="w-16 h-16 mx-auto text-gray-600 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                            </svg>
                            <p className="text-gray-400 mb-4">You haven't applied for any loans yet</p>
                            <button
                                onClick={() => setActiveTab('apply')}
                                className="px-4 py-2 bg-amber-500 text-black font-semibold rounded-lg hover:bg-amber-400 transition-colors"
                            >
                                Apply for a Loan
                            </button>
                        </div>
                    ) : (
                        <div className="divide-y divide-amber-900/10">
                            {myLoans.map((loan) => (
                                <div key={loan.id} className="p-6 hover:bg-[#242424] transition-colors">
                                    <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                                        <div className="flex items-center gap-4">
                                            <div className="w-12 h-12 bg-amber-500/10 rounded-lg flex items-center justify-center">
                                                <svg className="w-6 h-6 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                                                </svg>
                                            </div>
                                            <div>
                                                <p className="text-white font-semibold text-lg">
                                                    ${parseFloat(loan.requestedAmount).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                                </p>
                                                <p className="text-gray-400 text-sm">{loan.loanPurpose}</p>
                                            </div>
                                        </div>
                                        <div className="flex items-center gap-4">
                                            <div className="text-right">
                                                <p className="text-gray-400 text-sm">Applied on</p>
                                                <p className="text-white">{formatDate(loan.appliedAt)}</p>
                                            </div>
                                            <span className={`px-3 py-1 rounded-full text-xs font-medium border ${getStatusBadge(loan.status)}`}>
                                                {loan.status?.replace('_', ' ') || 'PENDING'}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            )}

            {/* Apply for Loan Tab */}
            {activeTab === 'apply' && (
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
                        
                        <form onSubmit={handleSubmit} className="space-y-6">
                            {/* Personal Information */}
                            <div>
                                <h3 className="text-sm font-medium text-amber-400 mb-4">Personal Information</h3>
                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-sm text-gray-400 mb-2">Full Name</label>
                                        <input 
                                            type="text"
                                            name="fullName"
                                            value={formData.fullName}
                                            onChange={handleInputChange}
                                            placeholder="John Doe"
                                            required
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm text-gray-400 mb-2">Email Address</label>
                                        <input 
                                            type="email"
                                            name="email"
                                            value={formData.email}
                                            onChange={handleInputChange}
                                            placeholder="john@example.com"
                                            required
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm text-gray-400 mb-2">Phone Number</label>
                                        <input 
                                            type="tel"
                                            name="phoneNumber"
                                            value={formData.phoneNumber}
                                            onChange={handleInputChange}
                                            placeholder="+1 (555) 000-0000"
                                            required
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm text-gray-400 mb-2">Date of Birth</label>
                                        <input 
                                            type="date"
                                            name="dateOfBirth"
                                            value={formData.dateOfBirth}
                                            onChange={handleInputChange}
                                            required
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm text-gray-400 mb-2">Marital Status</label>
                                        <select 
                                            name="maritalStatus"
                                            value={formData.maritalStatus}
                                            onChange={handleInputChange}
                                            required
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        >
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
                                        <select 
                                            name="employmentStatus"
                                            value={formData.employmentStatus}
                                            onChange={handleInputChange}
                                            required
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        >
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
                                            name="monthlyIncome"
                                            value={formData.monthlyIncome}
                                            onChange={handleInputChange}
                                            placeholder="0.00"
                                            required
                                            min="1"
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm text-gray-400 mb-2">Employer Name</label>
                                        <input 
                                            type="text"
                                            name="employerName"
                                            value={formData.employerName}
                                            onChange={handleInputChange}
                                            placeholder="Company Inc."
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm text-gray-400 mb-2">Years at Current Job</label>
                                        <select 
                                            name="yearsAtCurrentJob"
                                            value={formData.yearsAtCurrentJob}
                                            onChange={handleInputChange}
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        >
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
                                        <select 
                                            name="loanPurpose"
                                            value={formData.loanPurpose}
                                            onChange={handleInputChange}
                                            required
                                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all"
                                        >
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
                                    required
                                    className="w-5 h-5 mt-0.5 bg-[#242424] border-amber-900/30 rounded text-amber-500 focus:ring-amber-500 focus:ring-offset-[#1a1a1a]"
                                />
                                <label htmlFor="terms" className="text-sm text-gray-400">
                                    I agree to the <a href="#" className="text-amber-400 hover:text-amber-300">Terms and Conditions</a> and authorize CashMe to verify my information and perform a credit check.
                                </label>
                            </div>

                            {/* Submit Button */}
                            <button 
                                type="submit"
                                disabled={submitting}
                                className="w-full py-4 px-6 bg-linear-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:ring-offset-2 focus:ring-offset-[#1a1a1a] transform hover:scale-[1.02] transition-all duration-200 shadow-lg shadow-amber-500/20 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100"
                            >
                                {submitting ? 'Submitting...' : 'Submit Application'}
                            </button>

                            <p className="text-center text-xs text-gray-500">
                                Your application will be reviewed within 24 hours. You'll receive an email with the decision.
                            </p>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}