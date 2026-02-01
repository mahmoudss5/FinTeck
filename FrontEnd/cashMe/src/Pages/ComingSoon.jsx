import { Link } from 'react-router-dom';

export default function ComingSoon() {
    return (
        <div className="min-h-[60vh] flex items-center justify-center">
            <div className="text-center max-w-md mx-auto px-4">
                {/* Icon */}
                <div className="w-24 h-24 mx-auto mb-8 bg-amber-500/10 rounded-full flex items-center justify-center">
                    <svg className="w-12 h-12 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
                    </svg>
                </div>

                {/* Title */}
                <h1 className="text-3xl font-bold text-white mb-4">Coming Soon</h1>

                {/* Description */}
                <p className="text-gray-400 mb-8">
                    We're working hard to bring you this feature. Stay tuned for updates!
                </p>

                {/* Progress indicator */}
                <div className="mb-8">
                    <div className="flex items-center justify-center gap-2 mb-2">
                        <span className="text-amber-400 text-sm font-medium">In Development</span>
                    </div>
                    <div className="w-full h-2 bg-gray-700 rounded-full overflow-hidden">
                        <div className="h-full w-1/3 bg-gradient-to-r from-amber-600 to-yellow-500 rounded-full animate-pulse"></div>
                    </div>
                </div>

                {/* Back Button */}
                <Link
                    to="/home"
                    className="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 transition-all duration-200 shadow-lg shadow-amber-500/20"
                >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                    </svg>
                    Back to Dashboard
                </Link>

                {/* Additional info */}
                <p className="mt-8 text-gray-500 text-sm">
                    Have suggestions? <a href="#" className="text-amber-400 hover:text-amber-300">Contact us</a>
                </p>
            </div>
        </div>
    );
}
