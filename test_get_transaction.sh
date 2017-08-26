#!/bin/bash
echo "create single transaction."

curl -i -X GET -H "authorization: eyJ1c2VybmFtZSI6Imx1emhhbyIsInBhc3N3b3JkIjoiU2FuZHlAMjAxMyIsImV4cGlyZV90aW1lIjoiMjAxNy0wOC0yN1QyMDoxNzoyMi4zNzNaIiwiYXV0aF90aWNrZXQiOiJjODdmM2Q5ZC0yNTE0LTRkMmItOGViZS1iNzM3N2M2MzM1NGMifQ=="  'https://purchase-agent.appspot.com/transactions/single/695f1e62-b85c-44b2-8b12-29621723809f'

