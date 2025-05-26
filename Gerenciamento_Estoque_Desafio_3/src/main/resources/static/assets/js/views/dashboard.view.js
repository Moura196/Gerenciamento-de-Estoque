import AuthService from '../auth/auth.service';
import RoleService from '../services/role.service';
import SidebarComponent from '../components/sidebar.component';

class DashboardView {
  constructor() {
    if (!AuthService.isAuthenticated()) {
      window.location.href = '/login.html';
      return;
    }

    this.init();
  }

  async init() {
    RoleService.initRoleBasedUI();
    await this.loadSidebar();
    this.loadInitialSection();
  }

  async loadSidebar() {
    const sidebarTemplate = RoleService.getSidebarTemplate();
    const response = await fetch(sidebarTemplate);
    document.getElementById('sidebar').innerHTML = await response.text();
    new SidebarComponent();
  }

  loadInitialSection() {
    this.loadSection('projetos');
  }

  async loadSection(section) {
    const sectionPath = RoleService.getSectionPath(section);
    try {
      const response = await fetch(sectionPath);
      document.getElementById('main-content').innerHTML = await response.text();
      
      await this.loadSectionScript(section);
    } catch (error) {
      console.error(`Erro ao carregar seção ${section}:`, error);
    }
  }

  async loadSectionScript(section) {
    try {
      const scriptPath = `/assets/js/views/${section}.view.js`;
      const response = await fetch(scriptPath);
      if (response.ok) {
        const script = document.createElement('script');
        script.src = scriptPath;
        document.body.appendChild(script);
      }
    } catch (error) {
      console.error(`Erro ao carregar script da seção ${section}:`, error);
    }
  }
}

new DashboardView();