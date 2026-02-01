import { Form, Link } from 'react-router-dom'
import logo from '../assets/logo.png'

export default function Register() {
    return (
        <div className="w-full max-w-md">
            {/* Card Container */}
            <div className="bg-[#1a1a1a]/80 backdrop-blur-sm border border-amber-900/30 rounded-2xl shadow-2xl p-8">
                
                {/* Logo & Header */}
                <div className="text-center mb-8">
                    <img 
                        src={logo} 
                        alt="CashMe Logo" 
                        className="h-20 mx-auto mb-4"
                    />
                    <h1 className="text-2xl font-bold text-white mb-2">Create an account</h1>
                    <p className="text-gray-400 text-sm">Start your financial journey with CashMe</p>
                </div>

                {/* Form */}
                <Form className="space-y-4">
                    {/* Name Fields */}
                    <div className="grid grid-cols-2 gap-3">
                        <div>
                            <label className="block text-sm font-medium text-gray-300 mb-2">
                                First Name
                            </label>
                            <input 
                                type="text" 
                                placeholder="John"
                                className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all duration-200"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-300 mb-2">
                                Last Name
                            </label>
                            <input 
                                type="text" 
                                placeholder="Doe"
                                className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all duration-200"
                            />
                        </div>
                    </div>

                    {/* Email Field */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Email Address
                        </label>
                        <input 
                            type="email" 
                            placeholder="you@example.com"
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all duration-200"
                        />
                    </div>

                    {/* Password Field */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Password
                        </label>
                        <input 
                            type="password" 
                            placeholder="••••••••"
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all duration-200"
                        />
                    </div>

                    {/* Confirm Password Field */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Confirm Password
                        </label>
                        <input 
                            type="password" 
                            placeholder="••••••••"
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all duration-200"
                        />
                    </div>

                    {/* Terms & Conditions */}
                    <div className="flex items-start">
                        <input 
                            type="checkbox" 
                            id="terms"
                            className="w-4 h-4 mt-0.5 bg-[#242424] border-amber-900/30 rounded text-amber-500 focus:ring-amber-500 focus:ring-offset-[#1a1a1a]"
                        />
                        <label htmlFor="terms" className="ml-2 text-sm text-gray-400">
                            I agree to the{' '}
                            <a href="#" className="text-amber-400 hover:text-amber-300">Terms of Service</a>
                            {' '}and{' '}
                            <a href="#" className="text-amber-400 hover:text-amber-300">Privacy Policy</a>
                        </label>
                    </div>

                    {/* Register Button */}
                    <button 
                        type="submit" 
                        className="w-full py-3 px-4 bg-gradient-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:ring-offset-2 focus:ring-offset-[#1a1a1a] transform hover:scale-[1.02] transition-all duration-200 shadow-lg shadow-amber-500/20"
                    >
                        Create Account
                    </button>
                </Form>

                {/* Divider */}
                <div className="relative my-6">
                    <div className="absolute inset-0 flex items-center">
                        <div className="w-full border-t border-amber-900/30"></div>
                    </div>
                    <div className="relative flex justify-center text-sm">
                        <span className="px-4 bg-[#1a1a1a] text-gray-500">or sign up with</span>
                    </div>
                </div>

                {/* Social Login */}
                <div className="grid grid-cols-2 gap-3">
                    <button className="flex items-center justify-center px-4 py-2.5 border border-amber-900/30 rounded-lg hover:bg-amber-900/20 transition-colors duration-200">
                        <svg className="w-5 h-5 text-white" viewBox="0 0 24 24" fill="currentColor">
                            <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                            <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                            <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                            <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                        </svg>
                        <span className="ml-2 text-sm text-gray-300">Google</span>
                    </button>
                    <button className="flex items-center justify-center px-4 py-2.5 border border-amber-900/30 rounded-lg hover:bg-amber-900/20 transition-colors duration-200">
                        <svg className="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M12 2C6.477 2 2 6.477 2 12c0 4.42 2.865 8.166 6.839 9.489.5.092.682-.217.682-.482 0-.237-.008-.866-.013-1.7-2.782.604-3.369-1.341-3.369-1.341-.454-1.155-1.11-1.462-1.11-1.462-.908-.62.069-.608.069-.608 1.003.07 1.531 1.03 1.531 1.03.892 1.529 2.341 1.087 2.91.831.092-.646.35-1.086.636-1.336-2.22-.253-4.555-1.11-4.555-4.943 0-1.091.39-1.984 1.029-2.683-.103-.253-.446-1.27.098-2.647 0 0 .84-.269 2.75 1.025A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.294 2.747-1.025 2.747-1.025.546 1.377.203 2.394.1 2.647.64.699 1.028 1.592 1.028 2.683 0 3.842-2.339 4.687-4.566 4.935.359.309.678.919.678 1.852 0 1.336-.012 2.415-.012 2.743 0 .267.18.578.688.48C19.138 20.163 22 16.418 22 12c0-5.523-4.477-10-10-10z"/>
                        </svg>
                        <span className="ml-2 text-sm text-gray-300">GitHub</span>
                    </button>
                </div>

                {/* Sign In Link */}
                <p className="mt-8 text-center text-sm text-gray-400">
                    Already have an account?{' '}
                    <Link 
                        to="/login" 
                        className="text-amber-400 font-medium hover:text-amber-300 transition-colors"
                    >
                        Sign in
                    </Link>
                </p>
            </div>
        </div>
    )
}