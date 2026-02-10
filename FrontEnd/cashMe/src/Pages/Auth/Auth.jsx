import { Outlet } from 'react-router-dom'

export default function Auth() {
  return (
    <main className="bg-[#242424] min-h-screen flex justify-center items-center px-4 py-8">
      <Outlet />
    </main>
  )
}