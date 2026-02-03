export default function LoansTable({ loans, formatDate }) {
    return (
        <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl overflow-hidden">
            <div className="p-6 border-b border-red-900/20">
                <h2 className="text-lg font-semibold text-white">All Loan Applications ({loans.length})</h2>
            </div>
            {loans.length === 0 ? (
                <div className="p-12 text-center">
                    <p className="text-gray-400">No loan applications found</p>
                </div>
            ) : (
                <div className="overflow-x-auto">
                    <table className="w-full">
                        <thead className="bg-[#242424]">
                            <tr className="text-left text-gray-400 text-sm">
                                <th className="px-6 py-4">ID</th>
                                <th className="px-6 py-4">Applicant</th>
                                <th className="px-6 py-4">Amount</th>
                                <th className="px-6 py-4">Purpose</th>
                                <th className="px-6 py-4">Status</th>
                                <th className="px-6 py-4">Applied</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-red-900/10">
                            {loans.map((loan) => (
                                <tr key={loan.id} className="hover:bg-[#242424] transition-colors">
                                    <td className="px-6 py-4 text-gray-400">#{loan.id}</td>
                                    <td className="px-6 py-4 text-white">{loan.fullName}</td>
                                    <td className="px-6 py-4 text-amber-400 font-semibold">
                                        ${parseFloat(loan.requestedAmount).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                                    </td>
                                    <td className="px-6 py-4 text-gray-400">{loan.loanPurpose}</td>
                                    <td className="px-6 py-4">
                                        <span className={`px-2 py-1 rounded text-xs font-medium ${
                                            loan.status === 'APPROVED' ? 'bg-green-500/10 text-green-400 border border-green-500/20' :
                                            loan.status === 'REJECTED' ? 'bg-red-500/10 text-red-400 border border-red-500/20' :
                                            'bg-yellow-500/10 text-yellow-400 border border-yellow-500/20'
                                        }`}>
                                            {loan.status}
                                        </span>
                                    </td>
                                    <td className="px-6 py-4 text-gray-400">{formatDate(loan.appliedAt)}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}
