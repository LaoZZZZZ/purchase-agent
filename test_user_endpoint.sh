#!/bin/bash
echo "create user."

curl -i -X POST -H "Content-Type: application/json" -d '{"username":"luzhao","password":"Sandy@2013","email":"luzhao1986@gmail.com","phone_number":"6623801724","address":"3640 S Sepulveda Blvd, Los Angeles, CA 90034"}' https://purchase-agent.appspot.com/user



