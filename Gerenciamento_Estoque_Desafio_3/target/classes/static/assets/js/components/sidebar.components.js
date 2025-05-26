import DashboardView from '../views/dashboard.view';
import AuthService from '../auth/auth.service';

class SidebarComponent {
  constructor() {
    this.sidebarItems = document.querySelectorAll('.sidebar-item');
    this.bindEvents();
  }

  bindEvents() {
    this.sidebarItems.forEach(item => {
      item.addEventListener('click', () => {
        const section = item.dataset.section;
        DashboardView.loadSection(section);
        this.setActiveItem(item);
      });
    });

    // Sair
    document.getElementById('logoutBtn').addEventListener('click', () => {
      AuthService.removeToken();
      window.location.href = '/login.html';
    });
  }

  setActiveItem(activeItem) {
    this.sidebarItems.forEach(item => item.classList.remove('active'));
    activeItem.classList.add('active');
  }
}

export default SidebarComponent;