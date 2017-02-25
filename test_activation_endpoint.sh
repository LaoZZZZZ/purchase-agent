#!/bin/bash
echo "activate user."

curl -i -X PUT -d "" 'https://purchase-agent.appspot.com/user/activate/test?activationToken=94f44b63-8f8d-4c49-a9fc-8e7a3cd84c21'
