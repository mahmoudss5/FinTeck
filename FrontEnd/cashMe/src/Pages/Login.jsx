import { Form, Link } from 'react-router-dom'
import logo from '../assets/logo.png'
import { useAuth } from '../services/AuthProvider';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { API_BASE_URL } from '../services/config';

export default function Login() {
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const {login} = useAuth();
  const navigate = useNavigate();
 
    const handleSubmit = async (event) => {
        event.preventDefault();
        // Handle form submission logic here
        const data = new FormData(event.target);
        const email = data.get('email');
        const password = data.get('password');

        try {
            setError('');
            setIsSubmitting(true);
            await login(email, password);
            navigate('/home');
        } catch (error) {
            setError(error.message);
        } finally {
            setIsSubmitting(false);
        }    
    }


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
                    <h1 className="text-2xl font-bold text-white mb-2">Welcome back</h1>
                    <p className="text-gray-400 text-sm">Sign in to your account</p>
                </div>

                {/* Form */}
                <Form onSubmit={handleSubmit} className="space-y-5">
                    {/* Email Field */}
                    <div>
                        <label className="block text-sm font-medium text-gray-300 mb-2">
                            Email Address
                        </label>
                        <input 
                            type="email" 
                            name='email'
                            placeholder="you@example.com"
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all duration-200"
                        />
                    </div>

                    {/* Password Field */}
                    <div>
                        <div className="flex items-center justify-between mb-2">
                            <label className="block text-sm font-medium text-gray-300">
                                Password
                            </label>
                            <a href="#" className="text-sm text-amber-400 hover:text-amber-300 transition-colors">
                                Forgot password?
                            </a>
                        </div>
                        <input 
                            type="password" 
                            placeholder="••••••••"
                            name='password'
                            className="w-full px-4 py-3 bg-[#242424] border border-amber-900/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:border-transparent transition-all duration-200"
                        />
                    </div>

                    {/* Remember Me */}
                    <div className="flex items-center">
                        <input 
                            type="checkbox" 
                            id="remember"
                            className="w-4 h-4 bg-[#242424] border-amber-900/30 rounded text-amber-500 focus:ring-amber-500 focus:ring-offset-[#1a1a1a]"
                        />
                        <label htmlFor="remember" className="ml-2 text-sm text-gray-400">
                            Remember me for 30 days
                        </label>
                    </div>

                    {/* Error Message */}
                    {error && (
                        <div className="p-3 bg-red-500/20 border border-red-500/30 rounded-lg">
                            <p className="text-red-400 text-sm">{error}</p>
                        </div>
                    )}

                    {/* Login Button */}
                    <button 
                        type="submit" 
                        disabled={isSubmitting}
                        className="w-full py-3 px-4 bg-linear-to-r from-amber-600 via-yellow-500 to-amber-600 text-black font-semibold rounded-lg hover:from-amber-500 hover:via-yellow-400 hover:to-amber-500 focus:outline-none focus:ring-2 focus:ring-amber-500 focus:ring-offset-2 focus:ring-offset-[#1a1a1a] transform hover:scale-[1.02] transition-all duration-200 shadow-lg shadow-amber-500/20"
                    >
                        {isSubmitting ? 'Signing in...' : 'Sign In'}
                    </button>
                </Form>

                {/* Divider */}
                <div className="relative my-6">
                    <div className="absolute inset-0 flex items-center">
                        <div className="w-full border-t border-amber-900/30"></div>
                    </div>
                    <div className="relative flex justify-center text-sm">
                        <span className="px-4 bg-[#1a1a1a] text-gray-500">or continue with</span>
                    </div>
                </div>

                {/* Social Login */}
                <div className="grid grid-cols-2 gap-3">
                    <a href={`${API_BASE_URL}/oauth2/authorization/github`} className="w-full">
                    <button className="w-full flex items-center justify-center px-4 py-2.5 border border-amber-900/30 rounded-lg hover:bg-amber-900/20 transition-colors duration-200">
                        <svg className="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M12 2C6.477 2 2 6.477 2 12c0 4.42 2.865 8.166 6.839 9.489.5.092.682-.217.682-.482 0-.237-.008-.866-.013-1.7-2.782.604-3.369-1.341-3.369-1.341-.454-1.155-1.11-1.462-1.11-1.462-.908-.62.069-.608.069-.608 1.003.07 1.531 1.03 1.531 1.03.892 1.529 2.341 1.087 2.91.831.092-.646.35-1.086.636-1.336-2.22-.253-4.555-1.11-4.555-4.943 0-1.091.39-1.984 1.029-2.683-.103-.253-.446-1.27.098-2.647 0 0 .84-.269 2.75 1.025A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.294 2.747-1.025 2.747-1.025.546 1.377.203 2.394.1 2.647.64.699 1.028 1.592 1.028 2.683 0 3.842-2.339 4.687-4.566 4.935.359.309.678.919.678 1.852 0 1.336-.012 2.415-.012 2.743 0 .267.18.578.688.48C19.138 20.163 22 16.418 22 12c0-5.523-4.477-10-10-10z"/>
                        </svg>
                        <span className="ml-2 text-sm text-gray-300">GitHub</span>
                    </button>
                    </a>
                </div>

                {/* Sign Up Link */}
                <p className="mt-8 text-center text-sm text-gray-400">
                    Don't have an account?{' '}
                    <Link 
                        to="/register" 
                        className="text-amber-400 font-medium hover:text-amber-300 transition-colors"
                    >
                        Create account
                    </Link>
                </p>
            </div>
        </div>
    )
}