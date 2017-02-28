#!/bin/bash
echo "activate user."

curl -i -X PUT -H "authorization: eyJ1c2VybmFtZSI6InRlc3QiLCJwYXNzd29yZCI6IjEyMzQiLCJleHBpcmVfdGltZSI6IjIwMTctMDMtMDFUMTA6MTU6MTkuNzM2WiIsImF1dGhfdGlja2V0IjoiOGY4NzdmMjYtZjI0OC00ZGEyLWI5ZTItNTc4NjIzZTQ2NDc4In0=" -d "" 'https://purchase-agent.appspot.com/user/activate/test?activationToken=94f44b63-8f8d-4c49-a9fc-8e7a3cd84c21'
