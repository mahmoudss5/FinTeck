import { Outlet } from 'react-router-dom'
import Nav from '../Components/Nav'
import Footer from '../Components/Footer'

export default function RootLayout() {
    return (
        <div className="min-h-screen bg-[#242424] flex flex-col">
            <Nav />
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 flex-grow w-full">
                <Outlet />
            </main>
            <Footer />
        </div>
    )
}