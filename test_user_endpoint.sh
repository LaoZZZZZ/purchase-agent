#!/bin/bash
echo "create user."

curl -i -X POST -H "Content-Type: application/json" -d '{"username":"test","password":"1234","email":"luzhao1986@gmail.com","phone_number":"6623801724","address":"8 perigee dr, stony brook, ny, 11790"}' https://purchase-agent.appspot.com/user



