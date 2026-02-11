import { API_BASE_URL, getHeaders } from "./config";
import { getWalletTransactions } from "./TransactionService";



export const deactivateWallet = async (walletId) => {
   console.log('recived wallet id from deactivate wallet service '+walletId)
   const response = await fetch(`${API_BASE_URL}/wallets/api/deactivate/${walletId}`, {
      method: "PUT",
      headers: getHeaders(),
   });

   if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || `HTTP error! status: ${response.status}`);
   }

   const result = await response.json();
   return result;
}

export const transferMoney = async (data,idempotencyKey) => {
   console.log('Sending transfer request with data:', data)
   console.log('Headers:', {
      ...getHeaders(),
      'Idempotency-Key':idempotencyKey
   })
   
   const response =await fetch(`${API_BASE_URL}/wallets/api/transfer`,{
   method:"PUT",
   headers:{
      ...getHeaders(),
      'Idempotency-Key':idempotencyKey
   },
   body:JSON.stringify(data)
   })     
   
   if (!response.ok) {
      const contentType = response.headers.get("content-type");
      let errorMessage = `HTTP error! status: ${response.status}`;
      
      if (contentType && contentType.includes("application/json")) {
         const error = await response.json();
         console.error('Backend error response:', error);
         errorMessage = error.message || error.error || errorMessage;
      } else {
         const errorText = await response.text();
         console.error('Backend error text:', errorText);
         errorMessage = errorText || errorMessage;
      }
      
      throw new Error(errorMessage);
   }
   
   const result=await response.json()
   return result
}

export const createWallet = async (data) => {

   console.log( 'the wallet data from the create wallet service '+data.currency+' '+data.initialBalance+' '+data.active)

   const response =await fetch(`${API_BASE_URL}/wallets/api/create`,{
    method:"POST",
    headers:getHeaders(),
    body:JSON.stringify(data)
   })     
   
   if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || `HTTP error! status: ${response.status}`)
   }
   
   const result=await response.json()
   return result
}


export const getAllWalletsforCurrentUser = async () => {
   const response =await fetch(`${API_BASE_URL}/wallets/api/my-wallets`,{
    method:"GET",
    headers:getHeaders(),
   })     
   
   if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || `HTTP error! status: ${response.status}`)
   }
   
   const result=await response.json()
   return result
}

export const getWalletById = async ({params}) =>{
 const id=params.id
 const walletTransactions=await getWalletTransactions(id)
 const response=await fetch(`${API_BASE_URL}/wallets/api/${id}`,{
    method:"GET",
    headers:getHeaders(),
 })     
 
 if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || `HTTP error! status: ${response.status}`)
 } 
 
 const result=await response.json()
 

 const walletData={
    wallet:result,
    transactions:walletTransactions
 }

 return walletData

}  

