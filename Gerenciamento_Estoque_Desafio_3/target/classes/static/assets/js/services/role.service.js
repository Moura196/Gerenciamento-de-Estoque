import AuthService from '../auth/auth.service';

class RoleService {
    static PERMISSIONS = {
        RT: {
            sections: ['projetos', 'itens', 'fornecedores'],
            defaultSection: 'projetos'
        }
    };

    static initRoleBasedUI() {
        const payload = AuthService.getTokenPayload();
        if (payload) {
            document.body.classList.add(payload.role.toLowerCase());
            this.setProfileBadge(payload);
        }
    }

    static setProfileBadge(payload) {
        const profileDiv = document.createElement('div');
        profileDiv.className = `profile-badge ${payload.role.toLowerCase()}`;
        profileDiv.innerHTML = `
            <span class="user-name">${payload.username}</span>
            <span class="user-role">${payload.role}</span>
        `;
        document.querySelector('.sidebar-header').appendChild(profileDiv);
    }

    static getSidebarTemplate() {
        return AuthService.isGP() ? 
            '/templates/partials/sidebar-gp.html' : 
            '/templates/partials/sidebar-rt.html';
    }

    static getSectionPath(section) {
        const role = AuthService.getTokenPayload()?.role;
        if (!this.PERMISSIONS[role]?.sections.includes(section)) {
            section = this.PERMISSIONS[role].defaultSection;
        }
        return `/templates/sections/${section}/${role}.html`;
    }

    static getAvailableSections() {
        const role = AuthService.getTokenPayload()?.role;
        return this.PERMISSIONS[role]?.sections || [];
    }
}

export default RoleService;