// Ticket Status Constants
export const TICKET_STATUS = {
    OPEN: 'OPEN',
    IN_PROGRESS: 'IN_PROGRESS',
    RESOLVED: 'RESOLVED',
    CLOSED: 'CLOSED'
};

// Ticket Category Constants
export const TICKET_CATEGORY = {
    ACCOUNT_ISSUE: 'ACCOUNT_ISSUE',
    TRANSACTION_PROBLEM: 'TRANSACTION_PROBLEM',
    TECHNICAL_SUPPORT: 'TECHNICAL_SUPPORT',
    BILLING_INQUIRY: 'BILLING_INQUIRY',
    SECURITY_CONCERN: 'SECURITY_CONCERN',
    GENERAL_QUESTION: 'GENERAL_QUESTION',
    FEATURE_REQUEST: 'FEATURE_REQUEST',
    OTHER: 'OTHER'
};

// Helper function to format category names for display
export const formatCategoryName = (category) => {
    const categoryMap = {
        [TICKET_CATEGORY.ACCOUNT_ISSUE]: 'Account Issue',
        [TICKET_CATEGORY.TRANSACTION_PROBLEM]: 'Transaction Problem',
        [TICKET_CATEGORY.TECHNICAL_SUPPORT]: 'Technical Support',
        [TICKET_CATEGORY.BILLING_INQUIRY]: 'Billing Inquiry',
        [TICKET_CATEGORY.SECURITY_CONCERN]: 'Security Concern',
        [TICKET_CATEGORY.GENERAL_QUESTION]: 'General Question',
        [TICKET_CATEGORY.FEATURE_REQUEST]: 'Feature Request',
        [TICKET_CATEGORY.OTHER]: 'Other'
    };
    return categoryMap[category] || category;
};

// Helper function to format status names for display
export const formatStatusName = (status) => {
    const statusMap = {
        [TICKET_STATUS.OPEN]: 'Open',
        [TICKET_STATUS.IN_PROGRESS]: 'In Progress',
        [TICKET_STATUS.RESOLVED]: 'Resolved',
        [TICKET_STATUS.CLOSED]: 'Closed'
    };
    return statusMap[status] || status;
};

// Get status badge color
export const getStatusColor = (status) => {
    const colorMap = {
        [TICKET_STATUS.OPEN]: 'blue',
        [TICKET_STATUS.IN_PROGRESS]: 'yellow',
        [TICKET_STATUS.RESOLVED]: 'green',
        [TICKET_STATUS.CLOSED]: 'gray'
    };
    return colorMap[status] || 'gray';
};
