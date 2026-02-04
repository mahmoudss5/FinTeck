import { useLoaderData, useNavigate } from "react-router-dom";
import { useState } from "react";
import { updateLoanStatus } from "../../services/AdminService";

export default function LoanDetailsForAdmin() {
    const loan = useLoaderData();
    const navigate = useNavigate();
    const [isProcessing, setIsProcessing] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', { 
            year: 'numeric', 
            month: 'short', 
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const handleStatusUpdate = async (status) => {
        setIsProcessing(true);
        setError("");
        setSuccess("");

        try {
            await updateLoanStatus(loan.id, status);
            setSuccess(`Loan ${status.toLowerCase()} successfully!`);
            
            // Navigate back to dashboard after 2 seconds
            setTimeout(() => {
                navigate('/home/admin-dashboard');
            }, 2000);
        } catch (err) {
            setError(err.message || `Failed to ${status.toLowerCase()} loan`);
        } finally {
            setIsProcessing(false);
        }
    };

    const getStatusColor = (status) => {
        switch (status) {
            case 'APPROVED':
                return 'bg-green-500/10 text-green-400 border-green-500/20';
            case 'REJECTED':
                return 'bg-red-500/10 text-red-400 border-red-500/20';
            case 'PENDING':
                return 'bg-yellow-500/10 text-yellow-400 border-yellow-500/20';
            default:
                return 'bg-gray-500/10 text-gray-400 border-gray-500/20';
        }
    };

    return (
        <div className="min-h-screen bg-[#0a0a0a] p-6">
            {/* Header */}
            <div className="mb-6">
                <button
                    onClick={() => navigate('/home/admin-dashboard')}
                    className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors mb-4"
                >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                    </svg>
                    Back to Dashboard
                </button>
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold text-white mb-2">Loan Application Details</h1>
                        <p className="text-gray-400">Review and manage loan application #{loan.id}</p>
                    </div>
                    <span className={`px-4 py-2 rounded-lg text-sm font-medium border ${getStatusColor(loan.status)}`}>
                        {loan.status}
                    </span>
                </div>
            </div>

            {/* Error/Success Messages */}
            {error && (
                <div className="mb-6 bg-red-500/10 border border-red-500/20 rounded-lg p-4">
                    <p className="text-red-400">{error}</p>
                </div>
            )}
            {success && (
                <div className="mb-6 bg-green-500/10 border border-green-500/20 rounded-lg p-4">
                    <p className="text-green-400">{success}</p>
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Main Content - Left Side */}
                <div className="lg:col-span-2 space-y-6">
                    {/* Applicant Information */}
                    <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                        <h2 className="text-xl font-semibold text-white mb-4 flex items-center gap-2">
                            <svg className="w-6 h-6 text-amber-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                            </svg>
                            Applicant Information
                        </h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Full Name</p>
                                <p className="text-white font-medium">{loan.fullName}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Email</p>
                                <p className="text-white font-medium">{loan.email || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Phone Number</p>
                                <p className="text-white font-medium">{loan.phoneNumber || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Date of Birth</p>
                                <p className="text-white font-medium">{loan.dateOfBirth || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Address</p>
                                <p className="text-white font-medium">{loan.address || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">City</p>
                                <p className="text-white font-medium">{loan.city || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">State</p>
                                <p className="text-white font-medium">{loan.state || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">ZIP Code</p>
                                <p className="text-white font-medium">{loan.zipCode || 'N/A'}</p>
                            </div>
                        </div>
                    </div>

                    {/* Employment Information */}
                    <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                        <h2 className="text-xl font-semibold text-white mb-4 flex items-center gap-2">
                            <svg className="w-6 h-6 text-amber-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                            </svg>
                            Employment Information
                        </h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Employment Status</p>
                                <p className="text-white font-medium">{loan.employmentStatus || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Employer Name</p>
                                <p className="text-white font-medium">{loan.employerName || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Monthly Income</p>
                                <p className="text-white font-medium">
                                    {loan.monthlyIncome ? `$${parseFloat(loan.monthlyIncome).toLocaleString('en-US', { minimumFractionDigits: 2 })}` : 'N/A'}
                                </p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Job Title</p>
                                <p className="text-white font-medium">{loan.jobTitle || 'N/A'}</p>
                            </div>
                        </div>
                    </div>

                    {/* Loan Details */}
                    <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                        <h2 className="text-xl font-semibold text-white mb-4 flex items-center gap-2">
                            <svg className="w-6 h-6 text-amber-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            Loan Details
                        </h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Requested Amount</p>
                                <p className="text-amber-400 text-2xl font-bold">
                                    ${parseFloat(loan.requestedAmount).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                </p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Loan Purpose</p>
                                <p className="text-white font-medium">{loan.loanPurpose || 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Loan Term</p>
                                <p className="text-white font-medium">{loan.loanTerm ? `${loan.loanTerm} months` : 'N/A'}</p>
                            </div>
                            <div>
                                <p className="text-gray-400 text-sm mb-1">Credit Score</p>
                                <p className="text-white font-medium">{loan.creditScore || 'N/A'}</p>
                            </div>
                        </div>
                    </div>

                    {/* Additional Information */}
                    {loan.additionalInfo && (
                        <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                            <h2 className="text-xl font-semibold text-white mb-4 flex items-center gap-2">
                                <svg className="w-6 h-6 text-amber-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                                Additional Information
                            </h2>
                            <p className="text-gray-300">{loan.additionalInfo}</p>
                        </div>
                    )}
                </div>

                {/* Sidebar - Right Side */}
                <div className="space-y-6">
                    {/* Application Timeline */}
                    <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                        <h2 className="text-lg font-semibold text-white mb-4">Application Timeline</h2>
                        <div className="space-y-4">
                            <div className="flex items-start gap-3">
                                <div className="w-2 h-2 bg-amber-400 rounded-full mt-2"></div>
                                <div>
                                    <p className="text-white font-medium">Application Submitted</p>
                                    <p className="text-gray-400 text-sm">{formatDate(loan.appliedAt)}</p>
                                </div>
                            </div>
                            {loan.updatedAt && loan.updatedAt !== loan.appliedAt && (
                                <div className="flex items-start gap-3">
                                    <div className="w-2 h-2 bg-amber-400 rounded-full mt-2"></div>
                                    <div>
                                        <p className="text-white font-medium">Last Updated</p>
                                        <p className="text-gray-400 text-sm">{formatDate(loan.updatedAt)}</p>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Quick Stats */}
                    <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                        <h2 className="text-lg font-semibold text-white mb-4">Quick Stats</h2>
                        <div className="space-y-3">
                            <div className="flex justify-between items-center">
                                <span className="text-gray-400">Application ID</span>
                                <span className="text-white font-medium">#{loan.id}</span>
                            </div>
                            <div className="flex justify-between items-center">
                                <span className="text-gray-400">User ID</span>
                                <span className="text-white font-medium">#{loan.userId}</span>
                            </div>
                            <div className="flex justify-between items-center">
                                <span className="text-gray-400">Status</span>
                                <span className={`px-2 py-1 rounded text-xs font-medium border ${getStatusColor(loan.status)}`}>
                                    {loan.status}
                                </span>
                            </div>
                        </div>
                    </div>

                    {/* Action Buttons */}
                    {loan.status === 'PENDING' && (
                        <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                            <h2 className="text-lg font-semibold text-white mb-4">Actions</h2>
                            <div className="space-y-3">
                                <button
                                    onClick={() => handleStatusUpdate('APPROVED')}
                                    disabled={isProcessing}
                                    className="w-full px-4 py-3 bg-green-500/10 text-green-400 border border-green-500/20 rounded-lg hover:bg-green-500/20 transition-all duration-200 font-medium disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
                                >
                                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                                    </svg>
                                    {isProcessing ? 'Processing...' : 'Approve Loan'}
                                </button>
                                <button
                                    onClick={() => handleStatusUpdate('REJECTED')}
                                    disabled={isProcessing}
                                    className="w-full px-4 py-3 bg-red-500/10 text-red-400 border border-red-500/20 rounded-lg hover:bg-red-500/20 transition-all duration-200 font-medium disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
                                >
                                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                                    </svg>
                                    {isProcessing ? 'Processing...' : 'Reject Loan'}
                                </button>
                            </div>
                        </div>
                    )}

                    {/* Already Processed */}
                    {loan.status !== 'PENDING' && (
                        <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
                            <h2 className="text-lg font-semibold text-white mb-4">Status</h2>
                            <p className="text-gray-400 text-sm">
                                This loan application has already been {loan.status.toLowerCase()}.
                            </p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}