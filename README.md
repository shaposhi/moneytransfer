# Money transfer
*money transfer api between accounts*

## How to start
- Option 1: download from [link][1] and start with `java -jar money-transfer-exec.jar `

- Option 2: start main class in edu.shapo.exprs.App in cloned project


## How to use
During startup application load into memory accounts with ID from 1 to 11, with initial amount of 1000.

API url for get accounts statuses http://localhost:8080/bank/api/stat/allaccstatus
Method GET

API url for get number of successful transfers http://localhost:8080/bank/api/stat/transactioncount
Method GET

API url for make transfer http://localhost:8080/bank/api/transfer/make 
Method: POST
body example 
`{"sourceAccountId": "1","targetAccountId": "3","transferAmount": "100.1","initiator": "john"}`

## How to test
In clonned project you can find class
`edu.shapo.exprs.unitint.loadtesttool.ApiLoadTestTool`
This is small application that run 1000 calls from fixed pool of 10 Theads, randomly generating values for src, dst accounts and transfer value. Just run main method.

You can also run unit and integration test that starts server and call it's api.


#### Short comments
- Requirements number 2, about simplisity, it is a great topic for discuss, because from one handside, this application could have only some classes with static methods, that do all work, but from my opinion if something will be added in requiremens this approach will be not easy to extend(it is my opinion and i remember about YAGNI). 
- I decided to create more infrastructure(one of the reasons, because I have time for this and fun to do it), and i think my solution is become more close to "ready to work", just add more rich persistant layer. So that is why I add DI(Guice) and support for H2 db, but it was more easy and fast to add couple of acounts at init step of application by java code.



[1]: https://github.com/shaposhi/moneytransfer/blob/master/executable/money-transfer-exec.jar "executable"
