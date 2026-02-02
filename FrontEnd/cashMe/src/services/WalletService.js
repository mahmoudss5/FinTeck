import { API_BASE_URL, getHeaders } from "./config";
import { getWalletTransactions } from "./TransactionService";


// to do make a loader function  to fetch all wallets and pass it to the wallets page
// and avoid using the wallets that returned to the user 
export const deactivateWallet = async (walletId) => {
   const response = await fetch(`${API_BASE_URL}/wallets/api/deactive/${walletId}`, {
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
   console.log('recived data from transfer money service '+data.senderWalletId+' '+data.receiverUsername+' '+data.amount+' '+data.currency)
   const response =await fetch(`${API_BASE_URL}/wallets/api/transfer`,{
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


export const getAllWallets = async () => {
   const response =await fetch(`${API_BASE_URL}/wallets/api/all`,{
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