curl -X POST \
https://purchase-agent.appspot.com/customers \
-H 'authorization: eyJ1c2VybmFtZSI6Imx1emhhbyIsInBhc3N3b3JkIjoiU2FuZHlAMjAxMyIsImV4cGlyZV90aW1lIjoiMjAxNy0wOC0yN1QyMDoxNzoyMi4zNzNaIiwiYXV0aF90aWNrZXQiOiJjODdmM2Q5ZC0yNTE0LTRkMmItOGViZS1iNzM3N2M2MzM1NGMifQ==' \
-H 'cache-control: no-cache' \
-H 'content-type: application/json' \
-H 'postman-token: 32440acb-4b89-804f-965e-f6ebdb01ad01' \
-d '{
"customer_name":"LaoZZZZZ",
"phone_number":"6626094211",
"address":"123 street, los angeles, ca 90042",
"wechat":"laozzzzz"
}'
