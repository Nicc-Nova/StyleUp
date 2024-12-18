import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from "./Sidebar";

function Layout() {
  
    return (
      <div style={{ display: 'flex' }}>
        <Sidebar/>
  
        {/* Main content */}
        <div style={{ flex: 1 }}>
          <Outlet />
        </div>
      </div>
    );
  }
  
  export default Layout;