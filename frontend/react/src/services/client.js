import axios from 'axios';

export async function getCustomers() {
    try {
        const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer`);
        console.log('Data:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error:', error.message);
        throw error;
    }
}