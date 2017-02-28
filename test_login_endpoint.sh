#!/bin/bash
echo "login user."

curl -i -X GET -H "Accept: application/json" 'https://purchase-agent.appspot.com/user/login?username=test&password=1234'
