import { useEffect } from "react";
import { useAuth } from "../../context/AuthProvider";
import { useNavigate, useSearchParams } from "react-router-dom";

export default function OAuth2Redirect() {
    const { loginWithToken } = useAuth(); 
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    useEffect(() => {
        const handleOAuth2Redirect = async () => {
            const token = searchParams.get("token");
            
            if (token) {
                try {
                    await loginWithToken(token);
                    navigate("/home");
                } catch (error) {
                    console.error("OAuth2 login failed:", error);
                    navigate("/login");
                }
            } else {
                console.error("No token found in URL");
                navigate("/login");
            }
        };
        
        handleOAuth2Redirect();
    }, [searchParams, loginWithToken, navigate]);

    return <div className="text-white text-center mt-20">Redirecting...</div>;
}