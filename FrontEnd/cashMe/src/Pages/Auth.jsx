import { Outlet } from 'react-router-dom'

export default function Auth() {
    return(
      <main className="bg-[#242424] h-screen flex justify-center items-center px-4">
          <Outlet />
      </main>
    )
}