# moneytransfer
Money Transfer api between accounts

download from: https://github.com/shaposhi/moneytransfer/blob/master/executable/money-transfer-exec.jar

and start with:

java -jar money-transfer-exec.jar 

Or from Project

edu.shapo.exprs.App - API application, just run main() method
port 8080

API url http://localhost:8080/bank/api/transfer/make method POST
body example 
{
    "sourceAccountId": "1", 
    "targetAccountId": "3", 
    "transferAmount": "100.1",
    "initiator": "john"
}



Application for load testing
edu.shapo.exprs.unitint.loadtesttool.ApiLoadTestTool, just run main() method
hardcoded 1000 calls from fixed pool of 10 Threads


