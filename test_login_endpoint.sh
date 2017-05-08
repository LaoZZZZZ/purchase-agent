#!/bin/bash
echo "login user."

curl -i -X GET -H "Accept: application/json" 'https://purchase-agent.appspot.com/user/login?username=luzhao&password=Sandy@2013'
