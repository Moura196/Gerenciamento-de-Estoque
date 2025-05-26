class AuthService {
  //Em desenvolvimento retirei a autenticação.
  static getTokenPayload() {
    return {
      username: "dev@localhost",
      role: "GP", //Ou RT para o perfil.
    };
    AuthService.saveToken(mockAuth.token);
    window.location.href = '/dashboard-gp.html';
    return;
  }



  // static TOKEN_KEY = 'estoque_jwt';

  // static getToken() {
  //   return localStorage.getItem(this.TOKEN_KEY);
  // }

  // static saveToken(token) {
  //   localStorage.setItem(this.TOKEN_KEY, token);
  // }

  // static removeToken() {
  //   localStorage.removeItem(this.TOKEN_KEY);
  // }

  // static getTokenPayload() {
  //   const token = this.getToken();
  //   if (!token) return null;
    
  //   try {
  //     const payload = JSON.parse(atob(token.split('.')[1]));
  //     return {
  //       username: payload.sub,
  //       role: payload.permissao // 'GP' ou 'RT'
  //     };
  //   } catch (e) {
  //     console.error('Token inválido', e);
  //     return null;
  //   }
  // }

  // static isAuthenticated() {
  //   return !!this.getToken();
  // }

  // static isGP() {
  //   const payload = this.getTokenPayload();
  //   return payload?.role === 'GP';
  // }
}

export default AuthService;