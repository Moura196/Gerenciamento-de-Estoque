import AuthService from '../auth/auth.service';
import api from '../auth/token.interceptor';

class LoginView {
  constructor() {
    this.form = document.getElementById('loginForm');
    this.bindEvents();
  }

  bindEvents() {
    this.form.addEventListener('submit', async (e) => {
      e.preventDefault();
      await this.handleLogin();
    });
  }

  async handleLogin() {
    const formData = {
      username: this.form.matricula.value,
      password: this.form.senha.value
    };

    try {
      const response = await api.post('/login', formData);
      AuthService.saveToken(response.data.token);
      
      const redirectTo = AuthService.isGP() ? 
        '/dashboard-gp.html' : 
        '/dashboard-rt.html';
      
      window.location.href = redirectTo;
    } catch (error) {
      console.error('Login failed:', error);
      alert('Credenciais inválidas');
    }
  }
}

new LoginView();