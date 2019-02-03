# moneytransfer
Money Transfer api between accounts

edu.shapo.exprs.App - API application, just run main() method

API url http://localhost:8080/bank/api/transfer/make method POST

body example 
{
    "sourceAccountId": "1", 
    "targetAccountId": "3", 
    "transferAmount": "100.1",
    "initiator": "john"
}

Application for real testing

edu.shapo.testtransfer.ApiRealTest, just run main() method

hardcoded 1000 calls from fixed pool of 10 Threads


