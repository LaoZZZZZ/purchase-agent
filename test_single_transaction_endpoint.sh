#!/bin/bash
echo "activate user."

curl -i -X GET -H "authorization: eyJ1c2VybmFtZSI6InRlc3QiLCJwYXNzd29yZCI6IjEyMzQiLCJleHBpcmVfdGltZSI6IjIwMTctMDMtMTNUMjE6NTM6MTYuMDU1WiIsImF1dGhfdGlja2V0IjoiZDQxMjExZDQtM2VhZi00YmU2LTk4MWQtZGYzMTI0MDlmNTY2In0=" -d "" 'https://purchase-agent.appspot.com/transactions/94f44b63-8f8d-4c49-a9fc-8e7a3cd84c21'
