import { useState } from 'react';
import { TICKET_CATEGORY, formatCategoryName } from '../../constants/ticketConstants';

export default function TicketForm({ onSubmit, onCancel, loading }) {
    const [formData, setFormData] = useState({
        subject: '',
        description: '',
        ticketCategory: TICKET_CATEGORY.GENERAL_QUESTION
    });
    const [errors, setErrors] = useState({});

    const validateForm = () => {
        const newErrors = {};
        if (!formData.subject.trim()) {
            newErrors.subject = 'Subject is required';
        } else if (formData.subject.length > 255) {
            newErrors.subject = 'Subject must not exceed 255 characters';
        }
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        try {
            await onSubmit(formData);
            // Reset form
            setFormData({
                subject: '',
                description: '',
                ticketCategory: TICKET_CATEGORY.GENERAL_QUESTION
            });
            setErrors({});
        } catch (err) {
            console.error('Error submitting form:', err);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        // Clear error for this field
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: null
            }));
        }
    };

    return (
        <div className="bg-[#1a1a1a] border border-red-900/20 rounded-xl p-6">
            <div className="flex items-center justify-between mb-6">
                <h3 className="text-xl font-bold text-white">Create New Ticket</h3>
                <button
                    onClick={onCancel}
                    className="text-gray-400 hover:text-white"
                >
                    ✕
                </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-400 mb-2">
                        Subject <span className="text-red-500">*</span>
                    </label>
                    <input
                        type="text"
                        name="subject"
                        value={formData.subject}
                        onChange={handleChange}
                        className={`w-full px-4 py-2 bg-[#242424] border ${
                            errors.subject ? 'border-red-500' : 'border-red-900/20'
                        } text-white rounded-lg focus:outline-none focus:border-red-500`}
                        placeholder="Brief description of your issue"
                        maxLength={255}
                    />
                    {errors.subject && (
                        <p className="text-red-400 text-sm mt-1">{errors.subject}</p>
                    )}
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-400 mb-2">
                        Category
                    </label>
                    <select
                        name="ticketCategory"
                        value={formData.ticketCategory}
                        onChange={handleChange}
                        className="w-full px-4 py-2 bg-[#242424] border border-red-900/20 text-white rounded-lg focus:outline-none focus:border-red-500"
                    >
                        {Object.values(TICKET_CATEGORY).map(category => (
                            <option key={category} value={category}>
                                {formatCategoryName(category)}
                            </option>
                        ))}
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-400 mb-2">
                        Description
                    </label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        rows={6}
                        className="w-full px-4 py-2 bg-[#242424] border border-red-900/20 text-white rounded-lg focus:outline-none focus:border-red-500 resize-none"
                        placeholder="Provide detailed information about your issue..."
                    />
                </div>

                <div className="flex gap-3 pt-2">
                    <button
                        type="submit"
                        disabled={loading}
                        className="flex-1 px-6 py-2 bg-red-600 hover:bg-red-700 disabled:bg-red-600/50 text-white rounded-lg transition-colors font-medium"
                    >
                        {loading ? 'Creating...' : 'Create Ticket'}
                    </button>
                    <button
                        type="button"
                        onClick={onCancel}
                        className="px-6 py-2 bg-[#242424] hover:bg-[#2a2a2a] text-gray-400 rounded-lg transition-colors"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}
