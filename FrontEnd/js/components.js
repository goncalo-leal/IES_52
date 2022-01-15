class Navbar extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
            <nav class="main-header navbar navbar-expand navbar-white navbar-light">
            <!-- Left navbar links -->
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
                    </li>
                </ul>
    
            <!-- Right navbar links -->
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a class="nav-link" data-widget="fullscreen" href="login.html" role="button" id="loginBttn">
                            Login
                        </a>
                    </li>
                </ul>
            </nav>
        `
    }
}


class SideBar extends HTMLElement {
    connectedCallback() {
        this.innerHTML =`
        <aside class="main-sidebar sidebar-dark-primary elevation-4">
            <!-- Brand Logo -->
            <a href="index.html" class="brand-link text-center">
                <span class="brand-text font-weight-bold">ShopAholytics</span>
            </a>
        
            <!-- Sidebar -->
            <div class="sidebar">
                <!-- Sidebar user panel (optional) -->
                <div class="user-panel mt-3 pb-3 mb-3 d-flex">
                    <div class="info">
                        <a id="sm_name" class="d-block"></a>
                    </div>
                </div>
        
                <!-- Sidebar Menu -->
                <nav class="mt-2">
                    <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
                        <!-- Add icons to the links using the .nav-icon class
                            with font-awesome or any other icon font library -->
                        <li class="nav-item">
                            <a href="/index.html" class="nav-link">
                                <i class="nav-icon fa fa-circle"></i>
                                <p>
                                    Home
                                </p>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="/statistics.html" class="nav-link">
                                <i class="nav-icon fa fa-circle"></i>
                                <p>
                                    Statistics
                                </p>
                            </a>
                        </li>
                        <li class="nav-item" id="menu-toggle">
                            <a href="#" class="nav-link">
                                <i class="nav-icon fa fa-circle"></i>
                                <p>
                                    Management
                                    <i class="fas fa-angle-left right"></i>
                                </p>
                            </a>
                            <ul class="nav nav-treeview">
                                <li class="nav-item">
                                    <a href="/user_management.html" class="nav-link">
                                        <i class="far fa-circle nav-icon"></i>
                                        <p>Manage Users</p>
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a href="/add_user.html" class="nav-link">
                                        <i class="far fa-circle nav-icon"></i>
                                        <p>Add User</p>
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a href="/store_management.html" class="nav-link">
                                        <i class="far fa-circle nav-icon"></i>
                                        <p>Manage Stores</p>
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a href="/add_store.html" class="nav-link">
                                        <i class="far fa-circle nav-icon"></i>
                                        <p>Add Store</p>
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </nav>
                <!-- /.sidebar-menu -->
                <div class="fixed-bottom">
                    <ul class="nav nav-pills nav-sidebar flex-column">
                        <!-- Add icons to the links using the .nav-icon class
                            with font-awesome or any other icon font library -->
                        <li class="nav-item">
                            <a href="/account_settings.html" class="nav-link">
                                <i class="nav-icon fa fa-cog"></i>
                                <p>
                                    Account Settings
                                </p>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <!-- /.sidebar -->
        </aside>
      `

      var page = window.location.pathname;

      if (["/add_store.html", "/store_management.html", "/add_user.html", "/user_management.html"].includes(page)) {
          $("#menu-toggle").addClass("menu-open");
      }

      $(`a[href="${page}"]`).addClass("active");
    }
}

class Footer extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
        <footer class="main-footer">
            <strong>ShopAholytics &copy; 2021 <a href="https://github.com/goncalo-leal/IES_52"><i class="fab fa-github"></i></a>.</strong>
                All rights reserved.
            <div class="float-right d-none d-sm-inline-block">
                <b>Version</b> 1.0 | <b>Iteration</b> 1
            </div>
        </footer>
        `
    }
}

customElements.define('main-nav', Navbar)
customElements.define('side-nav', SideBar)
customElements.define('main-foot', Footer)