/**
 * API client for the online store
 * This file contains functions for interacting with the REST API endpoints
 */

// Base URL for API endpoints
const API_BASE_URL = '/api';

// Order API
const OrderAPI = {
    // Get all orders
    getAllOrders: async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders`);
            if (!response.ok) {
                throw new Error('Failed to fetch orders');
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching orders:', error);
            throw error;
        }
    },

    // Get order by ID
    getOrderById: async (orderId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders/${orderId}`);
            if (!response.ok) {
                throw new Error('Failed to fetch order');
            }
            return await response.json();
        } catch (error) {
            console.error(`Error fetching order ${orderId}:`, error);
            throw error;
        }
    },

    // Get orders by user ID
    getOrdersByUserId: async (userId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders/user/${userId}`);
            if (!response.ok) {
                throw new Error('Failed to fetch user orders');
            }
            return await response.json();
        } catch (error) {
            console.error(`Error fetching orders for user ${userId}:`, error);
            throw error;
        }
    },

    // Get orders by status
    getOrdersByStatus: async (status) => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders/status/${status}`);
            if (!response.ok) {
                throw new Error('Failed to fetch orders by status');
            }
            return await response.json();
        } catch (error) {
            console.error(`Error fetching orders with status ${status}:`, error);
            throw error;
        }
    },

    // Create a new order
    createOrder: async (orderData) => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderData)
            });
            if (!response.ok) {
                throw new Error('Failed to create order');
            }
            return await response.text();
        } catch (error) {
            console.error('Error creating order:', error);
            throw error;
        }
    },

    // Update an existing order
    updateOrder: async (orderId, orderData) => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders/${orderId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderData)
            });
            if (!response.ok) {
                throw new Error('Failed to update order');
            }
            return await response.text();
        } catch (error) {
            console.error(`Error updating order ${orderId}:`, error);
            throw error;
        }
    },

    // Update order status
    updateOrderStatus: async (orderId, status) => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders/${orderId}/status?status=${status}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to update order status');
            }
            return await response.text();
        } catch (error) {
            console.error(`Error updating status for order ${orderId}:`, error);
            throw error;
        }
    },

    // Delete an order
    deleteOrder: async (orderId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders/${orderId}`, {
                method: 'DELETE'
            });
            if (!response.ok) {
                throw new Error('Failed to delete order');
            }
            return await response.text();
        } catch (error) {
            console.error(`Error deleting order ${orderId}:`, error);
            throw error;
        }
    },

    // Get orders count
    getOrdersCount: async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders/count`);
            if (!response.ok) {
                throw new Error('Failed to fetch orders count');
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching orders count:', error);
            throw error;
        }
    },

    // Get orders count by user
    getOrdersCountByUser: async (userId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/orders/count/user/${userId}`);
            if (!response.ok) {
                throw new Error('Failed to fetch user orders count');
            }
            return await response.json();
        } catch (error) {
            console.error(`Error fetching orders count for user ${userId}:`, error);
            throw error;
        }
    }
};

// Product API
const ProductAPI = {
    // Get all products
    getAllProducts: async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/products`);
            if (!response.ok) {
                throw new Error('Failed to fetch products');
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching products:', error);
            throw error;
        }
    },

    // Get product by ID
    getProductById: async (productId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/products/${productId}`);
            if (!response.ok) {
                throw new Error('Failed to fetch product');
            }
            return await response.json();
        } catch (error) {
            console.error(`Error fetching product ${productId}:`, error);
            throw error;
        }
    }
};

// User API
const UserAPI = {
    // Get all users
    getAllUsers: async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/users`);
            if (!response.ok) {
                throw new Error('Failed to fetch users');
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching users:', error);
            throw error;
        }
    },

    // Get user by ID
    getUserById: async (userId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/users/${userId}`);
            if (!response.ok) {
                throw new Error('Failed to fetch user');
            }
            return await response.json();
        } catch (error) {
            console.error(`Error fetching user ${userId}:`, error);
            throw error;
        }
    }
};

// Export the API objects
window.OrderAPI = OrderAPI;
window.ProductAPI = ProductAPI;
window.UserAPI = UserAPI;