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

export const transferMoney = async (data) => {
   console.log('recived data from transfer money service '+data.senderWalletId+' '+data.receiverUserName+' '+data.amount+' '+data.currency)
   const response =await fetch(`${API_BASE_URL}/wallets/api/transfer`,{
    method:"PUT",
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

