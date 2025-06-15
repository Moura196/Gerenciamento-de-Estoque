
async function checkAuth(requiredRole) {
    if (window.DEV_MODE === true) {
        console.log('Modo desenvolvimento - autenticação bypassada');
        return Promise.resolve(true);
    }
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login.html';
        throw new Error('No token found');
    }
    const response = await fetch('/api/auth/validate', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    
    if (!response.ok) {
        localStorage.removeItem('token');
        window.location.href = '/login.html';
        throw new Error('Token invalid');
    }
    
    return true;
}
async function fetchWithAuth(url, options = {}) {
    await checkAuth();
    const token = localStorage.getItem('token');
    options.headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...options.headers
    };

    return fetch(url, options);
}
window.fetchWithAuth = fetchWithAuth;
window.checkAuth = checkAuth; 