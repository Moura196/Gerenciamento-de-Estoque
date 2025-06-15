if (!window.fetchWithAuth) {
    console.error('Erro crítico: auth.js não foi carregado corretamente');
    window.handleAuthError();
    throw new Error('Dependência auth.js não carregada');
}

//Carrega página
function initDashboard() {  
    checkAuth().then(() => {
        setupDashboard();
        loadUsersSection();
    }).catch(error => {
        console.error("Authentication check failed:", error);
        window.handleAuthError();
    });
}
//Confere elementos e chama setupEventlistners
async function setupDashboard() {
    const btnUsers = document.getElementById('btnUsers');
    const contentSection = document.getElementById('contentSection');
    
    if (!btnUsers || !contentSection) {
        console.error('Elementos não encontrados!');
        return;
    }

    btnUsers.addEventListener('click', async () => {
        await loadUsersSection();
    });

    setupEventListeners();
}
//confirmação da presença do <scrip></scrip> na página
function loadScript(src, callback) {
    return new Promise((resolve, reject) => {
        const script = document.createElement('script');
        script.src = src;
        script.onload = () => {
            if (callback) callback();
            resolve();
        };
        script.onerror = () => {
            console.error(`Erro ao carregar o script: ${src}`);
            reject(new Error(`Falha ao carregar ${src}`));
        };
        document.head.appendChild(script);
    });
}
//Monta a sessão users
async function loadUsersSection() {
    try {
        const contentSection = document.getElementById('contentSection');
        if (!contentSection) {
            throw new Error('Elemento contentSection não encontrado');
        }
        const response = await fetch('/static/sections/users.html');
        if (!response.ok) throw new Error(`Erro HTTP! status: ${response.status}`);
        
        contentSection.innerHTML = await response.text();
        await new Promise(resolve => setTimeout(resolve, 100));
        await loadScript('/static/js/users.js');
        if (typeof window.initUsers !== 'function') {
            throw new Error('Função initUsers não disponível');
        }
           window.initUsers();
        } catch (error) {
        console.error('Erro ao carregar seção de usuários:', error);
        showError(`Falha ao carregar usuários: ${error.message}`);
    }
}
//Valida autenticação
async function checkAuth() {
    const token = localStorage.getItem('token');
    if (!token) {
        throw new Error('No token found');
    }
    
    try {
        const response = await fetch('/api/auth/validate', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (!response.ok) {
            localStorage.removeItem('token');
            throw new Error('Token invalid');
        }
        return true;
    } catch (error) {
        localStorage.removeItem('token');
        throw error;
    }
}
//Mostra erros
function showError(message) {
    const contentSection = document.getElementById('contentSection');
    if (contentSection) {
        contentSection.innerHTML = `
            <div class="p-4 text-red-700 bg-red-100 rounded">
                ${message}
            </div>
        `;
    }
}
//Listners de eventos da página
function setupEventListeners() {
    const toggleSidebar = document.getElementById("toggleSidebar");
    if (toggleSidebar) {
        toggleSidebar.addEventListener("click", function() {
            const sidebar = document.getElementById("sidebar");
            const texts = document.querySelectorAll(".text");
            sidebar.classList.toggle("w-12");
            sidebar.classList.toggle("items-center");  
            texts.forEach(text => text.classList.toggle("hidden")); 
        });
    }

    document.querySelectorAll(".menu-btn").forEach(button => {
        button.addEventListener("click", function() {
            document.getElementById("content").innerHTML = `<h2 class='text-xl font-bold'>${this.dataset.section}</h2>`;
        });
    });

    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function() {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
        });
    }
}
//Evento de confirmação initDashbord()
document.addEventListener('DOMContentLoaded', function() {
    if (window.initDashboard) {
        initDashboard();
    } else {
        console.error('initDashboard não está disponível!');
    }
});

window.initDashboard = initDashboard;
window.loadUsersSection = loadUsersSection;